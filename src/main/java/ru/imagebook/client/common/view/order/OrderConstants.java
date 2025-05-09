package ru.imagebook.client.common.view.order;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;

import java.util.Date;

public interface OrderConstants extends Messages {
	String number();

	String order();

	String state();

	String date();

    String action();

	String moveToBasket();

	String newOrders();

	String basket();

	String removeFromBasket();

	String quantity();

	String orderButton();

	String bills();

	String billNumber();

	String billDate();

	String pay();

    String processOrder();

    String edit();

    String copy();

	String delete();

	String publish();

	String selectPaymentMethod();

	String cover();

	String flash();

	String flashLink();

	String price();

	String cost();

	String noOrders();

	String basketIsEmpty();

	String ordersTitle();

	String billsTitle();
	
	String billPaidHeading();
	
	String billPaidText();

	String confirmOrderDeletion();

	String confirmOrdersDeletion();

	String discount();

	String total();

	String orderDeletedHeading();

	String ordersDeletedHeading();

	String orderDeletedText();

	String ordersDeletedText();

	String noBills();

	String orderId();

	String confirmBillDeletion();

	String paymentMethods();

	String receiptMethod();

	String receiptTitle();

	String receiptDoc();

	String receiptPDF();

	String receiptName();

	String receiptAddress();

	String openReceipt();

	String rejectCommentLinkTitle();

	String rejectCommentBoxTitle();

	String lastName();

	String name();

	String surname();

	String phone();

	String nextButton();

	String deliveryHeading();

	String otherAddress();

	String newOrdersHint();

	String costWoDiscount();

	String color();

	String coverLam();

	String pageLam();

	String editParamsAnchor();

	String paramBoxHeading();

	String roboMethod();

	String unblockPopups();

	String code();

	String codeLink();

	String codeBoxHeading();

	String confirmActionCodeBoxHeading();

	String codeField();

	String applyCodeButton();

	String confirmActionCodeButton();

	String bonusDiscount();

	String bonusCode();

	String incorrectCode();

	String incorrectCodePeriodFromTo(String dateStart, String dateEnd);

	String incorrectCodePeriodFrom(String dateStart);

	String codeAlreadyUsed();

	String acceptCheckBox();

	String trialPriceText();

	String orderTrialButton();

	String qiwiMethod();

	String qiwiTitle();

	String qiwiUserNameField();

	String qiwiPayAnchor();

	String qiwiHint();
	
	String mailRuMethod();
	
	String mailRuHint();

	String emptyUserName();

	String qiwiPhone();

	String anchorHintTitle();

	String deliveryCode();

	String flashGenerationState();

	String newState();

	String directOrderButton();

	String darberryButton();

	String type();

	String otherButton();

	String numberField();

	String verificationCodeField();

	String darberryHint();

	String noDeliveryMethod();

	String flashGeneration();

    String flashGenerationError();

	String rejected();

	String format();

	String priceWoDiscount();

    String editOrderFailed();

    String copyOrderFailed();

    String processOrderFailed();

	String publishOrderFailed();

	String activationCodePaymentTypeMoneyWithDateEnd(int paymentValue, String dateEnd);

	String activationCodePaymentTypeMoney(int paymentValue);

	String activationCodePaymentTypePercentWithDateEnd(int percentValue, String dateEnd);

	String activationCodePaymentTypePercent(int percentValue);

	String codeNotAvailableForProduct();

    String bonusCodeNotSpecified();

    String bonusCodeNotSpecifiedConfirm();
}
