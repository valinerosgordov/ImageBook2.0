package ru.imagebook.server.service;

import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import ru.imagebook.server.repository.ActionRepository;
import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.StatusRequestState;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.imagebook.shared.model.app.RequestStateResult;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

public class ActionServiceImpl implements ActionService {
	@Autowired
	private ActionRepository repository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private UserService userService;
	@Autowired
	private MessageSource messages;
	@Autowired
	private AuthService authService;
	@Autowired
	private ProductService productService;

	@Override
	public List<BonusAction> loadActions(final String query) {
		return repository.loadActions(query);
	}

	@Override
	public List<BonusAction> loadActions(final Vendor vendor, final String query) {
		return repository.loadActions(vendor, query);
        }

        public List<Album> loadAlbums() {
		return productService.loadAlbums();
	}

	@Override
	public BonusAction getBonusActionWithAlbums(final BonusAction bonusAction) {
		return repository.getBonusActionWithAlbums(bonusAction);
	}

	private Vendor usersVendor() {
		int userId = authService.getCurrentUserId();
		User user = userService.getUser(userId);
		return user.getVendor();
	}

	private void authorizeAction(BonusAction action, boolean update) {
		Vendor userVendor = usersVendor();
		if (userVendor.getType() == VendorType.IMAGEBOOK && !update) {
			return;
		}
		if (!action.getVendor().equals(userVendor)) {
			throw new AccessDeniedError();
		}
	}

	@Override
	public void addAction(BonusAction action) {
		Vendor vendor = usersVendor();
		action.setVendor(vendor);
		repository.saveAction(action);
	}

	@Override
	public void updateAction(BonusAction modified) {
		BonusAction action = repository.getAction(modified.getId());
		authorizeAction(action, true);
		action.setName(modified.getName());
		action.setDateStart(modified.getDateStart());
		action.setDateEnd(modified.getDateEnd());
		action.setLevel1(modified.getLevel1());
		action.setLevel2(modified.getLevel2());
		action.setPermanentLevel(modified.getPermanentLevel());
		action.setDiscount1(modified.getDiscount1());
		action.setDiscount2(modified.getDiscount2());
		action.setRepeatal(modified.isRepeatal());
		action.setDiscountSum(modified.getDiscountSum());
		action.setCodeLength(modified.getCodeLength());
		action.setAlbums(modified.getAlbums());
	}

	@Override
	public void deleteActions(List<Integer> ids) {
		for (Integer id : ids) {
			BonusAction action = repository.getAction(id);
			authorizeAction(action, true);
		}
		repository.deleteActions(ids);
	}

	@Override
	public void deleteBonusCodesFromAction(final List<Integer> ids, final int id) {
		if (ids == null || ids.isEmpty()) throw new IllegalArgumentException("list of codes must be specified");
		BonusAction action = repository.getAction(id);
		authorizeAction(action, true);
		repository.deleteBonusCodes(ids);
	}

	@Override
	public Map<BonusCode, List<Order<?>>> loadOrdersForBonusAction(int bonusActionId) {
		final BonusAction action = repository.getAction(bonusActionId);
		Map<BonusCode, List<Order<?>>> map = new LinkedHashMap<BonusCode, List<Order<?>>>();
		for (BonusCode code : action.getCodes()) {
			final List<Order<?>> orders = orderRepository.getCodeOrders(code);
			map.put(code, orders);
		}
		return map;
	}

	@Override
	public void showCodes(int actionId, Writer writer) {
		BonusAction action = repository.getAction(actionId);
		authorizeAction(action, false);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("action", action);
		freeMarker.process("codes.ftl", Locales.RU, writer);
	}

	private String makeRandomString(int length, String alphabet, Random random) {
		char[] string = new char[length];

		for (int i = 0; i < length; i++) {
			int n = random.nextInt(alphabet.length());
			string[i] = alphabet.charAt(n);
		}

		return new String(string);
	}

	@Override
	public void generateCodes(int actionId, int quantity) {
		Random random = new Random();
		BonusAction action = repository.getAction(actionId);
		authorizeAction(action, false);

		int length = 8;
		if (action.getCodeLength() != null) {
			length = action.getCodeLength();
		}

		int i = 0;
		while (i < quantity) {
			String s = makeRandomString(length, "0123456789", random);
			if (repository.countCodesStartingWith(s) > 0)
				continue;

			BonusCode code = new BonusCode();
			code.setNumber(action.getCodes().size());
			code.setCode(s);
			action.addCode(code);

			i++;
		}
	}

	@Override
	public List<StatusRequest> loadStatusRequests() {
		return repository.loadStatusRequests();
	}

	@Override
	public void sendStatusCode(final int requestId, String email) {
		final StatusRequest request = repository.getRequest(requestId);
		if (request.getState() != StatusRequestState.NEW)
			throw new RuntimeException("Illegal request state");

		request.setState(StatusRequestState.APPROVED);

		User user = request.getUser();
		Vendor vendor = user.getVendor();

		String subject = messages.getMessage("statusCode", new Object[] { vendor.getName() }, new Locale(user.getLocale()));

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("data", vendor);
		String url = "http://" + vendor.getOfficeUrl() + "/bonusStatus?a=" + requestId + "&b=" + request.getCode();
		freeMarker.set("url", url);
		String html = freeMarker.process("statusCode.ftl", user.getLocale());

		notifyService.notifyUserToSeparateEmail(user, subject, html, email);
	}

	@Override
	public void createStatusRequest(String text, int userId, StatusRequest.Source requestSource) {
		final User user = userService.getUser(userId);
		Vendor vendor = user.getVendor();

		StatusRequest request = repository.getLastRequest(user);
		if (request != null && request.getState() != StatusRequestState.REJECTED) {
			throw new AccessDeniedError();
		}

		request = new StatusRequest();
		request.setUser(user);
		request.setRequest(text);
		request.setCode(UUID.randomUUID().toString());
		request.setSource(requestSource);
		repository.save(request);

		String subject = messages.getMessage("statusRequestCreated", new Object[0], new Locale(Locales.RU));
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("user", user.getFullName());
		String html = freeMarker.process("statusRequestCreated.ftl", Locales.RU);
		notifyService.notifyVendorAdmin(vendor, subject, html);
	}

	@Override
	public int getRequestState(int userId) {
		User user = userService.getUser(userId);
		if (user.getLevel() == 8) {
			return RequestStateResult.ACTIVATED;
		}

		StatusRequest request = repository.getLastRequest(user);
		if (request == null || request.getState() == StatusRequestState.REJECTED) {
			return RequestStateResult.NEW;
		} else if (request.getState() == StatusRequestState.NEW) {
			return RequestStateResult.PROCESSING;
		} else if (request.getState() == StatusRequestState.APPROVED) {
			return RequestStateResult.APPROVED;
		} else if (request.getState() == StatusRequestState.ACTIVATED) {
			return RequestStateResult.ACTIVATED;
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void rejectRequest(int requestId, final String reason) {
		StatusRequest request = repository.getRequest(requestId);
		if (request.getState() != StatusRequestState.NEW)
			throw new RuntimeException("Illegal request state");

		request.setState(StatusRequestState.REJECTED);

		User user = request.getUser();
		Vendor vendor = user.getVendor();

		String subject = messages.getMessage("statusRequestRejected", new Object[] { vendor.getName() },
				new Locale(user.getLocale()));

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("reason", reason);
		String html = freeMarker.process("statusRequestRejected.ftl", user.getLocale());

		notifyService.notifyUser(user, subject, html);
	}

	@Override
	public void activateRequest(int requestId, String code, Writer writer) {
		final StatusRequest request = repository.getRequest(requestId);
		if (request.getState() != StatusRequestState.APPROVED)
			throw new RuntimeException("Illegal request state");

		if (!request.getCode().equals(code))
			throw new AccessDeniedError();

		request.setState(StatusRequestState.ACTIVATED);

		User user = request.getUser();
		user.setLevel(8);
		user.setPhotographerByLevel(8);

		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.process("bonusStatusActivation.ftl", Locales.RU, writer);
	}

	@Override
	public boolean addCodes(int actionId, String codes) {
		String lines[] = codes.split("\\r?\\n");
		
		BonusAction action = repository.getAction(actionId);
		authorizeAction(action, false);

		for (String code : lines) {
// if (repository.countCodesStartingWith(code) > 0) {
// return false;
// }
			
			BonusCode bonusCode = new BonusCode();
			bonusCode.setNumber(action.getCodes().size());
			bonusCode.setCode(code);
			action.addCode(bonusCode);
		}

		return true;
	}
}
