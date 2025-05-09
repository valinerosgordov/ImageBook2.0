package ru.imagebook.server.service2.app.delivery.sdek;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.server.repository.BillRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryOrder;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryResponse;
import ru.imagebook.server.service2.app.delivery.sdek.model.ResponseOrder;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.CalculatePriceRequest;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.DsSendState;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.freemarker.FreeMarker;

@Service
public class SDEKTransferServiceImpl implements SDEKTransferService {
    private Logger logger = Logger.getLogger(getClass());

    private static final Function<Bill, Integer> BILLS_TO_MAP_FUNCTION = new Function<Bill, Integer>() {
        @Nullable
        @Override
        public Integer apply(@Nullable Bill input) {
            assert input != null;
            return input.getId();
        }
    };

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private SDEKClient sdekClient;

    @Autowired
    private SDEKHelper sdekHelper;

    @Autowired
    private MessageSource messages;

    private final Locale locale = new Locale(Locales.RU);

    @Transactional
    @Override
    public void transfer() {
        List<Bill> readyBills = billRepository.loadReadyToTransferToSDEK();
        if (readyBills.isEmpty()) {
            logger.info("No bills were found for sending to SDEK");
            return;
        }
        logger.info(format("Prepare %s bills for sending to SDEK", readyBills.size()));

        try {
            DeliveryRequest deliveryRequest = sdekHelper.createDeliveryRequest(readyBills.size());

            for (Bill bill : readyBills) {
                SDEKDeliveryType deliveryType = SDEKDeliveryType.COURIER.name().equals(bill.getSdekDeliveryType())
                    ? SDEKDeliveryType.COURIER : SDEKDeliveryType.PICKUP;
                logger.debug(format("Bill[id=%s] Adding %s for sending to SDEK", bill.getId(), deliveryType));
                DeliveryOrder deliveryOrder = sdekHelper.createDeliveryOrder(bill, deliveryType);

                logger.debug(format("Calculate price of transfer in SDEK in Bill[id=%s]", bill.getId()));
                CalculatePriceRequest calculatePriceRequest = sdekHelper.createCalculateTariffRequest(bill, deliveryType, deliveryOrder);
                deliveryOrder.setTariffTypeCode(sdekClient.sendCalculationRequest(calculatePriceRequest).getResult().getTariffId());
                logger.debug(format("Result of calculation in SDEK for Bill[id=%s]", bill.getId()));

                deliveryRequest.addOrder(deliveryOrder);
            }

            DeliveryResult deliveryResult = sendRequest(readyBills, deliveryRequest);
            logger.info(format("%s bills were sent to SDEK [success: %s, failed: %s]", readyBills.size(),
                deliveryResult.sent, deliveryResult.failed));
        } catch (HttpClientErrorException e) {
            handleError(readyBills, e, e.getResponseBodyAsString());
        } catch (Exception e) {
            handleError(readyBills, e, messages.getMessage("sdekUnknownError", null, locale));
        }
    }

    private DeliveryResult sendRequest(List<Bill> bills, DeliveryRequest deliveryRequest) {
        DeliveryResult result = new DeliveryResult();

        DeliveryResponse response = sdekClient.sendRequestIM(deliveryRequest);

        Map<Integer, Bill> billsById = Maps.uniqueIndex(bills, BILLS_TO_MAP_FUNCTION);
        for (ResponseOrder item : response.getOrders()) {
            logger.debug(item);

            if (item.getNumber() == null) { // skip common messages
                continue;
            }

            Bill bill = billsById.get(parseInt(item.getNumber()));

            if (item.getErrCode() == null && item.getDispatchNumber() != null) {
                bill.setDsSendState(DsSendState.SENT);
                bill.setDsSendingId(String.valueOf(item.getDispatchNumber()));
                bill.setDsErrorMessage(null);

                logger.debug(format("Sending for bill [billId=%s] has created successfully in SDEK [orderId=%s]",
                    bill.getId(), bill.getDsSendingId()));
                result.sent++;
            } else if (item.getErrCode() != null) {
                handleError(bill, null, messages.getMessage("sdekFailureError",
                    new Object[] {item.getErrCode(), item.getMsg()}, locale));
                result.failed++;
            }
        }

        return result;
    }

    private void handleError(Collection<Bill> billsToSend, Exception e, String errorMsg) {
        for (Bill bill : billsToSend) {
            handleError(bill, e, errorMsg);
        }
    }

    private void handleError(Bill billToSend, Exception e, String errorMsg) {
        logger.error(format("Couldn't send bill with id=%s to SDEK: %s", billToSend.getId(), errorMsg), e);
        billToSend.setDsSendState(DsSendState.FAILURE);
        billToSend.setDsErrorMessage(errorMsg);
        notifyAdminAboutBillTransferFailure(billToSend);
    }

    private void notifyAdminAboutBillTransferFailure(Bill failedBill) {
        try {
            String subject = messages.getMessage(
                "billTransferToSDEKFailedSubject", new Object[]{failedBill.getId()}, locale);

            FreeMarker freeMarker = new FreeMarker(getClass());
            freeMarker.set("billId", failedBill.getId());
            String errorMsg;
            if (StringUtils.isNotEmpty(failedBill.getDsErrorMessage())) {
                errorMsg = failedBill.getDsErrorMessage();
            } else {
                errorMsg = messages.getMessage("sdekUnknownError", new Object[0], locale);
            }
            freeMarker.set("errorMsg", errorMsg);
            boolean isCourierDelivery = SDEKDeliveryType.COURIER.name().equals(failedBill.getSdekDeliveryType());
            freeMarker.set("deliveryType", isCourierDelivery
                ? messages.getMessage("sdekCourierType", new Object[0], locale)
                : messages.getMessage("sdekPickupType", new Object[0], locale));

            if (!isCourierDelivery) {
                freeMarker.set("pickupPointId", failedBill.getSdekPickupPointId());
                freeMarker.set("pickupPointAddress", failedBill.getSdekPickupPointAddress());
            }
            String html = freeMarker.process("sdekTransferFailed.ftl", Locales.RU);

            notifyService.notifyAdmin(subject, html);
        } catch (Exception e) {
            logger.error("Couldn't send admin notification", e);
        }
    }

    private class DeliveryResult {
        private int sent;
        private int failed;
    }
}

