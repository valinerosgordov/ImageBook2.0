package ru.imagebook.server.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.imagebook.client.admin.ctl.action.ActionMessages;
import ru.imagebook.client.admin.ctl.bill.BillMessages;
import ru.imagebook.client.admin.ctl.delivery.DeliveryMessages;
import ru.imagebook.client.admin.ctl.finishing.FinishingMessages;
import ru.imagebook.client.admin.ctl.mailing.MailingMessages;
import ru.imagebook.client.admin.ctl.pricing.PricingMessages;
import ru.imagebook.client.admin.ctl.product.ProductMessages;
import ru.imagebook.client.admin.ctl.request.RequestMessages;
import ru.imagebook.client.admin.ctl.site.SiteMessages;
import ru.imagebook.client.admin.ctl.user.UserMessages;
import ru.imagebook.client.admin.ctl.vendor.VendorMessages;
import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.pages.PagesMessages;
import ru.imagebook.server.ctl.editor.RemoteEditorMessages;
import ru.imagebook.server.ctl.finishing.RemoteFinishingMessages;
import ru.imagebook.server.ctl.flash.FlashMessages;
import ru.imagebook.server.ctl.qiwi.QiwiMessages;
import ru.imagebook.server.repository.SecurityRepository;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.flow.download.RemoteDownloadMessages;
import ru.saasengine.client.ctl.auth.AuthMessages;

public class SecurityServiceImpl implements SecurityService {
	private final SecurityRepository repository;
	private Map<Integer, Set<String>> messages = new HashMap<Integer, Set<String>>();

	public SecurityServiceImpl(SecurityRepository repository) {
		this.repository = repository;	

		Set<String> userMessages = new HashSet<String>();
		userMessages.add(AuthMessages.CONNECT);
		userMessages.add(RemoteDownloadMessages.REQUEST_FILE);
		userMessages.add(RemoteDownloadMessages.DELETE_FILE);
		userMessages.add(QiwiMessages.QIWI_PAY);
		userMessages.add(FlashMessages.SHOW_FLASH_XML);
		userMessages.add(FlashMessages.SHOW_FLASH_IMAGE);
		userMessages.add(FileMessages.LOAD_FOLDERS);
		userMessages.add(FileMessages.LOAD_IMAGES);
		userMessages.add(FileMessages.ADD_IMAGE);
		userMessages.add(FileMessages.SHOW_NOTIFICATION);
		userMessages.add(FileMessages.CREATE_FOLDER);
		userMessages.add(FileMessages.EDIT_FOLDER);
		userMessages.add(FileMessages.DELETE_FOLDER);
		userMessages.add(FileMessages.DELETE_IMAGE);
		userMessages.add(FileMessages.DISPOSE);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.LOAD_PRODUCTS);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.CREATE_ORDER);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.COPY_ORDER);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.LOAD_ORDERS);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.OPEN_ORDER);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.PROCESS_ORDER);
		userMessages
				.add(ru.imagebook.client.editor.ctl.order.OrderMessages.CLOSE_ORDER);
		userMessages.add(PagesMessages.CHANGE_PAGE_COUNT);
		userMessages.add(PagesMessages.CLEAR_SPREAD);
		userMessages.add(RemoteEditorMessages.SHOW_PREVIEW);
		userMessages.add(RemoteEditorMessages.SHOW_COMPONENT);
		userMessages.add(RemoteEditorMessages.UPLOAD);
		messages.put(Role.USER, userMessages);

		Set<String> operatorMessages = new HashSet<String>();
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.LOAD_ORDERS);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.LOAD_DATA_FOR_ADD_FORM);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.LOAD_USERS);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.ADD_ORDER);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.UPDATE_ORDER);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.APPLY_FILTER);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.NOTIFY_NEW_ORDERS);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.NOTIFY_ORDERS_ACCEPTED);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.REGENERATE_ORDER_REQUEST);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.REGENERATE_ORDER);
		operatorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.GENERATE_PDF);
		operatorMessages.add(UserMessages.LOAD_DATA);
		operatorMessages.add(UserMessages.LOAD_USERS);
		operatorMessages.add(UserMessages.ADD_USER);
		operatorMessages.add(UserMessages.UPDATE_USER);
		operatorMessages.add(UserMessages.DELETE_USERS);
		operatorMessages.add(UserMessages.SEND_INVITATION);
		operatorMessages.add(RequestMessages.LOAD_REQUESTS);
		operatorMessages.add(RequestMessages.LOAD_BASKET_ORDERS);
		operatorMessages.add(RequestMessages.LOAD_NON_BASKET_ORDERS);
		operatorMessages.add(RequestMessages.DELETE);
		operatorMessages.add(RequestMessages.SEND_DAILY_REQUEST);
		operatorMessages.add(RequestMessages.SEND_WEEKLY_BOOK_REQUEST);
		operatorMessages.add(RequestMessages.PUT_TO_BASKET);
		operatorMessages.add(RequestMessages.REMOVE_FROM_BASKET);
		operatorMessages.add(RequestMessages.CREATE_REQUEST);
		operatorMessages.add(RequestMessages.UPDATE_REQUEST);
		operatorMessages.add(RequestMessages.PRINT_REQUEST);
		operatorMessages.add(RequestMessages.BOOK_REQUEST);
		operatorMessages.add(RequestMessages.URGENT_REQUEST);
		operatorMessages.add(RequestMessages.ACT);
		operatorMessages.add(RequestMessages.CLOSE_REQUESTS);
		operatorMessages.add(BillMessages.LOAD_BILLS);
		operatorMessages.add(BillMessages.MARK_PAID);
		operatorMessages.add(BillMessages.UPDATE_BILL);
		operatorMessages.add(BillMessages.DELETE_BILLS);
		operatorMessages.add(ActionMessages.LOAD_STATUS_REQUESTS);
		operatorMessages.add(ActionMessages.SEND_STATUS_CODE);
		operatorMessages.add(ActionMessages.REJECT_REQUEST);
		messages.put(Role.OPERATOR, operatorMessages);

		Set<String> siteAdminMessages = new HashSet<String>();
		siteAdminMessages.add(SiteMessages.LOAD_SECTIONS);
		siteAdminMessages.add(SiteMessages.SAVE_SECTION);
		siteAdminMessages.add(SiteMessages.ADD_SECTION);
		siteAdminMessages.add(SiteMessages.DELETE_SECTIONS);
		siteAdminMessages.add(SiteMessages.LOAD_TOP_SECTIONS);
		siteAdminMessages.add(SiteMessages.ADD_TOP_SECTION);
		siteAdminMessages.add(SiteMessages.SAVE_TOP_SECTION);
		siteAdminMessages.add(SiteMessages.DELETE_TOP_SECTIONS);
		siteAdminMessages.add(SiteMessages.ADD_PHRASE);
		siteAdminMessages.add(SiteMessages.LOAD_PHRASES);
		siteAdminMessages.add(SiteMessages.SAVE_PHRASE);
		siteAdminMessages.add(SiteMessages.DELETE_PHRASES);
		siteAdminMessages.add(SiteMessages.LOAD_FOLDERS);
		siteAdminMessages.add(SiteMessages.ADD_FOLDER);
		siteAdminMessages.add(SiteMessages.ADD_DOCUMENT);
		siteAdminMessages.add(SiteMessages.SAVE_FOLDER);
		siteAdminMessages.add(SiteMessages.SAVE_DOCUMENT);
		siteAdminMessages.add(SiteMessages.DELETE_FOLDERS);
		siteAdminMessages.add(SiteMessages.DELETE_DOCUMENTS);
		siteAdminMessages.add(SiteMessages.ADD_BANNER);
		siteAdminMessages.add(SiteMessages.LOAD_BANNERS);
		siteAdminMessages.add(SiteMessages.SAVE_BANNER);
		siteAdminMessages.add(SiteMessages.DELETE_BANNERS);
		siteAdminMessages.add(SiteMessages.LOAD_DIR_SECTION_DATA);
		siteAdminMessages.add(SiteMessages.LOAD_DIR_SECTIONS);
		siteAdminMessages.add(SiteMessages.ADD_DIR_SECTION_1);
		siteAdminMessages.add(SiteMessages.SAVE_DIR_SECTION1);
		siteAdminMessages.add(SiteMessages.ADD_DIR_SECTION_2);
		siteAdminMessages.add(SiteMessages.SAVE_DIR_SECTION2);
		siteAdminMessages.add(SiteMessages.ADD_DIR_SECTION_3);
		siteAdminMessages.add(SiteMessages.SAVE_DIR_SECTION3);
		siteAdminMessages.add(SiteMessages.DELETE_DIR_SECTIONS);
		messages.put(Role.SITE_ADMIN, siteAdminMessages);

		Set<String> adminMessages = new HashSet<String>();
		adminMessages.add(ProductMessages.LOAD_ALBUMS);
		adminMessages.add(ProductMessages.LOAD_COLORS_FOR_ALBUM);
		adminMessages.add(ProductMessages.ADD_ALBUM);
		adminMessages.add(ProductMessages.UPDATE_ALBUM);
		adminMessages.add(ProductMessages.DELETE_ALBUMS);
		adminMessages.add(ProductMessages.LOAD_COLORS);
		adminMessages.add(ProductMessages.UPDATE_COLORS);
		adminMessages.add(ProductMessages.DELETE_COLORS);
		adminMessages.add(PricingMessages.LOAD_PRICING_DATA);
		adminMessages.add(PricingMessages.SAVE_PRICING_DATA);
		adminMessages.add(ActionMessages.LOAD_ACTIONS);
		adminMessages.add(ActionMessages.ADD_ACTION);
		adminMessages.add(ActionMessages.UPDATE_ACTION);
		adminMessages.add(ActionMessages.DELETE_ACTIONS);
		adminMessages.add(ActionMessages.SHOW_ACTIONS_SECTION_WITH_VENDORS);
		adminMessages.add(ActionMessages.LOAD_VENDOR_ACTIONS);
		adminMessages.add(ActionMessages.GENERATE_CODES);
		adminMessages.add(ActionMessages.ADD_CODES);
		adminMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.DELETE_ORDERS);
		adminMessages.add(ru.imagebook.server.ctl.action.ActionMessages.SHOW_CODES);
		adminMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.PUBLISH_WEB_FLASH);
		adminMessages.add(VendorMessages.LOAD_VENDORS);
		adminMessages.add(VendorMessages.ADD_VENDOR);
		adminMessages.add(VendorMessages.UPDATE_AGENT);
		adminMessages.add(MailingMessages.LOAD_MAILINGS);
		adminMessages.add(MailingMessages.ADD_MAILING);
		adminMessages.add(MailingMessages.UPDATE_MAILING);
		adminMessages.add(MailingMessages.DELETE_MAILINGS);
		adminMessages.add(MailingMessages.TEST_MAILING);
		adminMessages.add(MailingMessages.SEND_MAILING);
		messages.put(Role.ADMIN, adminMessages);

		Set<String> deliveryManagerMessages = new HashSet<String>();
		deliveryManagerMessages.add(DeliveryMessages.LOAD_ORDERS);
		deliveryManagerMessages.add(DeliveryMessages.LOAD_TYPE_ORDERS);
		deliveryManagerMessages.add(DeliveryMessages.ADD_ORDER);
		deliveryManagerMessages.add(DeliveryMessages.REMOVE_ORDERS);
		deliveryManagerMessages.add(DeliveryMessages.FIND_ORDER);
		deliveryManagerMessages.add(DeliveryMessages.DELIVER);
		deliveryManagerMessages
				.add(ru.imagebook.server.ctl.delivery.DeliveryMessages.PRINT);
		messages.put(Role.DELIVERY_MANAGER, deliveryManagerMessages);

		Set<String> finishingManagerMessages = new HashSet<String>();
		finishingManagerMessages.add(FinishingMessages.LOAD_ORDERS);
		finishingManagerMessages.add(FinishingMessages.FINISH_ORDER);
		finishingManagerMessages.add(FinishingMessages.ORDER_SCANNED);
		finishingManagerMessages.add(RemoteFinishingMessages.SHOW_PREVIEW);
		messages.put(Role.FINISHING_MANAGER, finishingManagerMessages);

		Set<String> vendorMessages = new HashSet<String>();
		vendorMessages
				.add(ru.imagebook.client.admin.ctl.order.OrderMessages.LOAD_ORDERS);
		vendorMessages.add(UserMessages.LOAD_DATA);
		vendorMessages.add(UserMessages.LOAD_USERS);
		vendorMessages.add(UserMessages.NO_VENDOR_NO_USERNAME_UPDATE_USER);
		vendorMessages.add(UserMessages.SEND_INVITATION);
		vendorMessages.add(BillMessages.LOAD_BILLS);
		vendorMessages.add(BillMessages.MARK_PAID);
		vendorMessages.add(BillMessages.DELETE_BILLS);
		vendorMessages.add(ActionMessages.LOAD_ACTIONS);
		vendorMessages.add(ActionMessages.ADD_ACTION);
		vendorMessages.add(ActionMessages.UPDATE_ACTION);
		vendorMessages.add(ActionMessages.DELETE_ACTIONS);
		vendorMessages.add(ActionMessages.GENERATE_CODES);
		vendorMessages.add(ActionMessages.ADD_CODES);
		vendorMessages.add(ru.imagebook.server.ctl.action.ActionMessages.SHOW_CODES);
		vendorMessages.add(MailingMessages.LOAD_MAILINGS);
		vendorMessages.add(MailingMessages.ADD_MAILING);
		vendorMessages.add(MailingMessages.UPDATE_MAILING);
		vendorMessages.add(MailingMessages.DELETE_MAILINGS);
		vendorMessages.add(MailingMessages.TEST_MAILING);
		vendorMessages.add(MailingMessages.SEND_MAILING);
		vendorMessages.add(ru.imagebook.client.admin.ctl.order.OrderMessages.LOAD_DATA_FOR_ADD_FORM);
		vendorMessages.add(ru.imagebook.client.admin.ctl.order.OrderMessages.VENDOR_UPDATE_ORDER);
		messages.put(Role.VENDOR, vendorMessages);
	}

	@Override
	public void checkAccess(User user, Message message) {
		if (isRoot(user))
			return;

		String messageType = message.getMessageType();
		for (Role role : user.getRoles()) {
			Set<String> roleMessages = messages.get(role.getType());
			if (roleMessages.contains(messageType))
				return;
		}

		ServiceLogger.warn("Denied: " + message.getClass() + " - "
				+ message.getMessageType());
		//throw new AccessDeniedError();
	}

	@Override
	public boolean isRoot(User user) {
		for (Role role : user.getRoles()) {
			if (role.getType() == Role.ROOT)
				return true;
		}
		return false;
	}

	@Override
	public boolean hasRole(User user, int type) {
		if (isRoot(user))
			return true;

		for (Role role : user.getRoles()) {
			if (role.getType() == type)
				return true;
		}
		return false;
	}

	@Override
	public void enableFilters(User user) {
		if (isRoot(user))
			return;

		if (hasRole(user, Role.VENDOR))
			repository.enableVendorFilters(user.getVendor());
	}

	@Override
	public boolean hasRoleAndIsNotRoot(User user, int type) {
		return !isRoot(user) && hasRole(user, type);
	}
}
