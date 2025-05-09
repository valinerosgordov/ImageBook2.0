package ru.imagebook.server.service2.app.delivery;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.DsSendState;
import ru.imagebook.shared.model.app.PickPointData;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.i18n.locale.Locales;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Boykov
 */
class PickPointHelperImpl implements PickPointHelper {

	private static final int POSTAGE_TYPE_STANDARD = 10001;
	private static final int GETTING_TYPE_COURIER = 101;
	private static final int PAY_TYPE = 1; // should always be 1 according to the API
	public static final String KEY_EDTN = "EDTN";

	private Logger logger = Logger.getLogger(getClass());


	@Autowired
	@Qualifier("pickPointRestTemplate")
	private RestTemplate restTemplate;
	@Autowired
	private DeliveryConfig config;
	@Autowired
	private CoreFactory coreFactory;
	@Autowired
	private PickpointSendingIdGenerator sendingIdGenerator;
	private ru.imagebook.client.common.service.order.OrderService clientOrderService;

	@PostConstruct
	private void init() {
		clientOrderService = new ru.imagebook.client.common.service.order.OrderService(coreFactory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String login() {
		String sessionId = null;

		JSONObject loginJson = new JSONObject();
		loginJson.put("Login", config.getPickPointLogin());
		loginJson.put("Password", config.getPickPointPassword());

		try {
			logger.debug("Logging in...");

			String result = makePost("login", loginJson);

			JSONObject parsedResult = (JSONObject) JSONValue.parse(result);
			sessionId = (String) parsedResult.get(KEY_SESSION_ID);
			String errorMsg = (String) parsedResult.get(KEY_ERROR_MESSAGE);
			if (!Strings.isNullOrEmpty(errorMsg)) {
				throw new RuntimeException(String.format("Unable to login: %s", errorMsg));
			}

			if (Strings.isNullOrEmpty(sessionId)) {
				throw new RuntimeException("Unable to login, there is no sessionId");
			}
		} catch (Exception e) {
			logger.error("Unable to login to PickPoint", e);
			Exceptions.rethrow(e);
		}

		logger.debug(String.format("Login successful, sessionId=[%s]", sessionId));
		return sessionId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean logout(String sessionId) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(sessionId), "sessionId should be provided");

		boolean success = false;

		logger.debug(String.format("Trying to logout. SessionId=[%s]", sessionId));
		JSONObject logoutJson = new JSONObject();
		logoutJson.put("SessionId", sessionId);

		try {
			String result = makePost("logout", logoutJson);

			JSONObject parsedResult = (JSONObject) JSONValue.parse(result);
			success = (boolean) parsedResult.get("Success");

		} catch (Exception e) {
			logger.error("Logout unsuccessful", e);
		}

		if (success) {
			logger.debug("logout successful");
		} else {
			logger.error("logout unsuccessful");
		}
		return success;
	}


	@Cacheable("pickPointGetZones")
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, PickPointData> getZones() {
		logger.debug("Loading zones from PickPoint");
		String sessionId = login();

		Map<String, PickPointData> zonesInfo = new HashMap<>();
		try {
			JSONObject requestJson = new JSONObject();
			requestJson.put("SessionId", sessionId);
			requestJson.put("FromCity", IMAGEBOOK_WAREHOUSE_CITY);
			requestJson.put("ToPT", "");

			String result = makePost("getzone", requestJson);

			JSONObject parsedResult = (JSONObject) JSONValue.parse(result);
			//{"Error":null,"Zones":[{"DeliveryMax":2,"DeliveryMin":1,"FromCity":"Москва","Koeff":1,
			// "ToCity":"Санкт-Петербург","ToPT":"7801-001","Zone":"0"}]}
			String errorMsg = (String) parsedResult.get(KEY_ERROR);
			if (!Strings.isNullOrEmpty(errorMsg)) {
				throw new RuntimeException(String.format("Can't get zones: %s, ", errorMsg));
			}
			JSONArray zones = (JSONArray) parsedResult.get("Zones");
			if (zones == null || zones.isEmpty()) {
				throw new RuntimeException("Zones array is empty");
			}
			for (Object zoneObj : zones) {
				JSONObject zone = (JSONObject) zoneObj;
				PickPointData pickPointData = new PickPointData();
				pickPointData.setPostamateID((String) zone.get("ToPT"));
				pickPointData.setRateZone((String) zone.get("Zone"));
				pickPointData.setTrunkCoeff(Double.valueOf(String.valueOf(zone.get("Koeff"))));
				pickPointData.setTimeMin(Integer.valueOf(String.valueOf(zone.get("DeliveryMin"))));
				pickPointData.setTimeMax(Integer.valueOf(String.valueOf(zone.get("DeliveryMax"))));
				zonesInfo.put(pickPointData.getPostamateID(), pickPointData);
			}
		} catch (Exception e) {
			logger.error("Can't get zoneInfo: %s", e);
			Exceptions.rethrow(e);
		} finally {
			logout(sessionId);
		}

		logger.info("pickPoint Zones loaded successfully");
		return zonesInfo;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<Bill> createSendings(List<Bill> bills) {
		List<Bill> processedBills = new ArrayList<>();

		JSONArray sendings = formSendingsJson(bills, processedBills);
		if (sendings.isEmpty()) {
			return processedBills;
		}

		String sessionId = null;
		JSONObject packageToSendJSON = new JSONObject();
		try {
			sessionId = login();

			packageToSendJSON.put("SessionId", sessionId);
			packageToSendJSON.put("Sendings", sendings);

			String result = makePost("createsending", packageToSendJSON);

			JSONObject parsedResult = (JSONObject) JSONValue.parse(result);
			/*
				{
					“CreatedSendings”:
					[
						{
							“EDTN”:		”<Идентификатор отправления, используемый для ответа >”,
							“InvoiceNumber”:	”<Номер отправления PickPoint>”,
							“Barcode”:	”<Штрихкод отправления>”
						}
					]
					“RejectedSendings”:
					[
						{
							“EDTN”:		”<Идентификатор отправления, используемый для ответа >”,
							“ErrorCode”:	<Код ошибки>,
							“ErrorMessage”:	”<Описание ошибки>”
						}
					]
				}
			*/
			JSONArray createdSendings = (JSONArray) parsedResult.get("CreatedSendings");
			for (Object createdSending : createdSendings) {
				JSONObject sending = (JSONObject) createdSending;
				String sendingId = (String) sending.get(KEY_EDTN);
				Optional<Bill> foundBill = findBillByImagebookSendingId(bills, sendingId);
				if (!foundBill.isPresent()) {
					logger.error(String.format("Can't find bill by Imagebook sending id [%s] from response", sendingId));
					continue;
				}
				foundBill.get().setDsSendingId((String) sending.get("InvoiceNumber"));
				foundBill.get().setDsSendState(DsSendState.SENT);
				processedBills.add(foundBill.get());
			}
			JSONArray rejectedSendings = (JSONArray) parsedResult.get("RejectedSendings");
			for (Object rejectedSending : rejectedSendings) {
				JSONObject sending = (JSONObject) rejectedSending;
				String sendingId = (String) sending.get(KEY_EDTN);
				Optional<Bill> foundBill = findBillByImagebookSendingId(bills, sendingId);
				if (!foundBill.isPresent()) {
					logger.error(String.format("Can't find bill by Imagebook sending id [%s] from response", sendingId));
					continue;
				}
				foundBill.get().setDsErrorMessage((String) sending.get("ErrorMessage"));
				foundBill.get().setDsSendState(DsSendState.FAILURE);
				processedBills.add(foundBill.get());
			}
		} catch (Exception e) {
			logger.error("Can't create sendings in PickPoint", e);
			logger.error("JSON which caused the error: " + packageToSendJSON.toJSONString());
			Exceptions.rethrow(e);
		} finally {
			if (!Strings.isNullOrEmpty(sessionId)) {
				logout(sessionId);
			}
		}
		return processedBills;
	}

	private Optional<Bill> findBillByImagebookSendingId(List<Bill> bills, final String sendingId) {
		return Iterables.tryFind(bills, new Predicate<Bill>() {
			@Override
			public boolean apply(Bill bill) {
				return sendingId.equals(bill.getSendingId());
			}
		});
	}

	/**
	 * Forms Sendings JSON which has the following format:
	 * <p/>
	 * <p/>
	 * <pre>
	 * 		[
	 *            {
	 * 			"EDTN":		"<Идентификатор отправления, используемый для ответа>",
	 * 				"IKN": 		"<ИКН – номер договора>",
	 * 				"Invoice":
	 *               {
	 * 					"SenderCode":	"<Номер КО клиента>",
	 * 					"BarCode": 	"<Штрих код>",
	 * 					"GCBarCode": 	"<Клиентский штрих-код>",
	 * 					"Description":	"<Описание отправления, обязательное поле>",
	 * 					"RecipientName":	"<Имя получателя>",
	 * 					"PostamatNumber":	"<Номер постамата>",
	 * 					"MobilePhone": 	"<Номер телефона получателя>",
	 * 					"Email": 		"<Адрес электронной почты получателя>",
	 * 					"PostageType": 	<Тип услуги>,
	 * 					"GettingType": 	<Тип сдачи отправления>,
	 * 					"PayType": 	<Тип оплаты>,
	 * 					"Sum": 		<Сумма к оплате>,
	 * 					"InsuareValue":	<Страховка>,
	 * 					"Width": 		<Ширина>,
	 * 					"Height": 		<Высота>,
	 * 					"Depth": 		<Глубина>",
	 * 					"ClientReturnAddress":	"<Адрес клиентского возврата>"
	 *                   {
	 * 						"CityName":	"<Название города>",
	 * 						"RegionName":	"<Название региона>",
	 * 						"Address":	"<Текстовое описание адреса>",
	 * 						"FIO":		"<ФИО контактного лица>",
	 * 						"PostCode":	"<Почтовый индекс>",
	 * 						"Organisation":	"<Наименование организации>",
	 * 						"PhoneNumber":	"<Контактный телефон, обязательное поле>",
	 * 						"Comment":	"<Комментарий>"
	 *                   },
	 * 					"UnclaimedReturnAddress":	"<Адрес возврата невостребованного>"
	 *                   {
	 * 						"CityName":	"<Название города>",
	 * 						"RegionName":	"<Название региона>",
	 * 						"Address":	"<Текстовое описание адреса>",
	 * 						"FIO":		"<ФИО контактного лица>",
	 * 						"PostCode":	"<Почтовый индекс>",
	 * 						"Organisation":	"<Наименование организации>",
	 * 						"PhoneNumber":	"<Контактный телефон, обязательное поле>",
	 * 						"Comment":	"<Комментарий>"
	 *                   },
	 * 					"SubEncloses":	<Субвложимые>
	 * 					[
	 *                       {
	 * 							"Line":		"<Номер>",
	 * 							"ProductCode":	"<Код продукта>",
	 * 							"GoodsCode":	"<Код товара>",
	 * 							"Name":		"<Наименование>",
	 * 							"Price":		<Стоимость>
	 *                       }
	 * 					]
	 *               }
	 *           }
	 * 		]
	 * 	</pre>
	 */
	@SuppressWarnings("unchecked")
	private JSONArray formSendingsJson(List<Bill> bills, List<Bill> processedBills) {
		JSONArray sendings = new JSONArray();

		JSONObject clientReturnAddress = new JSONObject();
		clientReturnAddress.put("CityName", config.getPickPointReturnAddressCity());
		clientReturnAddress.put("RegionName", config.getPickPointReturnAddressRegion());
		clientReturnAddress.put("Address", config.getPickPointReturnAddressStreetAddress());
		clientReturnAddress.put("FIO", config.getPickPointReturnAddressContactPerson());
		clientReturnAddress.put("PostCode", config.getPickPointReturnAddressZip());
		clientReturnAddress.put("Organisation", config.getPickPointReturnAddressOrganisation());
		clientReturnAddress.put("PhoneNumber", config.getPickPointReturnAddressPhone());
		clientReturnAddress.put("Comment", config.getPickPointReturnAddressComment());

		for (Bill bill : bills) {
			try {
				Address deliveryAddress = getDeliveryAddress(bill);

				JSONObject sending = new JSONObject();
				sendings.add(sending);
				String imageBookSendingId = sendingIdGenerator.generateImageBookSendingId(bill);
				bill.setSendingId(imageBookSendingId);
				sending.put(KEY_EDTN, imageBookSendingId);
				sending.put("IKN", config.getPickPointIkn());
				JSONObject invoice = new JSONObject();
				sending.put("Invoice", invoice);
				invoice.put("SenderCode", imageBookSendingId);
				invoice.put("BarCode", "");
				invoice.put("GCBarCode", "");
				invoice.put("Description", "Фотокниги");
				invoice.put("RecipientName", deliveryAddress.getFullName());
				if (Strings.isNullOrEmpty(bill.getPickpointPostamateID())) {
					throw new RuntimeException("Postamate ID shouldn't be empty");
				}
				invoice.put("PostamatNumber", bill.getPickpointPostamateID());
				invoice.put("MobilePhone", deliveryAddress.getPhone());
				invoice.put("Email", deliveryAddress.getEmail());
				invoice.put("PostageType", POSTAGE_TYPE_STANDARD);
				invoice.put("GettingType", GETTING_TYPE_COURIER);
				invoice.put("PayType", PAY_TYPE);
				invoice.put("Sum", 0); // should be 0 for prepayment
				invoice.put("ClientReturnAddress", clientReturnAddress);
				invoice.put("UnclaimedReturnAddress", clientReturnAddress);
				JSONArray subEncloses = new JSONArray();
				invoice.put("SubEncloses", subEncloses);
				int subEncloseCounter = 1;
				for (Order<?> order : bill.getOrders()) {
					JSONObject subEnclose = new JSONObject();
					subEnclose.put("Line", String.valueOf(subEncloseCounter));
					subEnclose.put("ProductCode", clientOrderService.getOrderArticle(order));
					subEnclose.put("GoodsCode", "");  // leave it empty, optional
					subEnclose.put("Name", order.getProduct().getName().getNonEmptyValue(Locales.RU));
					subEnclose.put("Price", order.getPrice());
					subEncloseCounter++;
					subEncloses.add(subEnclose);
				}
			} catch (Exception e) {
				logger.error(String.format("Can't send sending for Bill with id %s to PickPoint, it will be skipped",
						bill.getId()), e);
				bill.setDsSendState(DsSendState.FAILURE);
				if (Strings.isNullOrEmpty(bill.getDsErrorMessage())) {
					bill.setDsErrorMessage("Неизвестная ошибка. " +
							(!Strings.isNullOrEmpty(e.getMessage()) ? e.getMessage() : ""));
				}
				processedBills.add(bill);
			}
		}
		return sendings;
	}

	private Order getFirstOrder(Bill bill) {
		if (!bill.getOrders().iterator().hasNext()) {
			bill.setDsErrorMessage("Счет не содержит заказов");
			throw new IllegalStateException(String.format("Bill id=%s has no orders", bill.getId()));
		}
		return bill.getOrders().iterator().next();
	}

	private Address getDeliveryAddress(Bill bill) {
		Order order = getFirstOrder(bill);
		Address address = order.getAddress();
		if (address == null) {
			bill.setDsErrorMessage("В счете нет адреса доставки");
			throw new IllegalStateException(
					String.format("Order order should have address, Bill,id=%s, order.id=%s", bill.getId(),
							order.getId()));
		}
		return address;
	}

	private HttpEntity<String> createJSONRequest(JSONObject json) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8");
		return new HttpEntity<>(json.toJSONString(), headers);
	}

	private String makePost(String apiPath, JSONObject requestJson) {
		if (Level.TRACE.equals(logger.getEffectiveLevel())) {
			logger.trace(String.format("request JSON: %s", requestJson.toJSONString()));
		}

		String result = restTemplate.postForObject(
				config.getPickPointUrl() + apiPath,
				createJSONRequest(requestJson), String.class);

		if (Level.TRACE.equals(logger.getEffectiveLevel())) {
			logger.trace(String.format("response: %s", result));
		}
		return result;
	}
}
