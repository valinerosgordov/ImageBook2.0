package ru.imagebook.client.common.service.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SecurityService {
	private final UserService userService;

	private Map<Integer, Set<String>> actions = new HashMap<Integer, Set<String>>();

	@Inject
	public SecurityService(UserService userService) {
		this.userService = userService;

		Set<String> userActions = new HashSet<String>();
		actions.put(Role.USER, userActions);

		Set<String> operatorActions = new HashSet<String>();
		operatorActions.add(Actions.ADMIN);
		operatorActions.add(Actions.ORDERS_VIEW);
		operatorActions.add(Actions.ORDERS_MANAGEMENT);
		operatorActions.add(Actions.ORDERS_VIEW_PRINTING_PRICE);
		operatorActions.add(Actions.BILLS);
		operatorActions.add(Actions.BILLS_UPDATE);
		operatorActions.add(Actions.USERS_VIEW);
		operatorActions.add(Actions.USERS_MANAGEMENT);
		operatorActions.add(Actions.USERS_ADD);
		operatorActions.add(Actions.USERS_DELETE);
		operatorActions.add(Actions.USERS_MODIFY_VENDOR_AND_USERNAME);
		operatorActions.add(Actions.REQUESTS);
		operatorActions.add(Actions.DISCOUNTS);
		operatorActions.add(Actions.REQUEST_STATUS);
		operatorActions.add(Actions.CREATE_ALBUMS);
		actions.put(Role.OPERATOR, operatorActions);

		Set<String> adminActions = new HashSet<String>();
		adminActions.add(Actions.ADMIN);
		adminActions.add(Actions.PRODUCTS);
		adminActions.add(Actions.VENDORS);
		adminActions.add(Actions.MAILING);
		adminActions.add(Actions.MANAGEMENT);
		adminActions.add(Actions.DELIVERY_ASSEMBLY);
		adminActions.add(Actions.DELIVERY_ORDER);
		adminActions.add(Actions.VIEW_ALL_VENDORS);
		actions.put(Role.ADMIN, adminActions);

		Set<String> siteAdminActions = new HashSet<String>();
		siteAdminActions.add(Actions.ADMIN);
		siteAdminActions.add(Actions.SITE);
		actions.put(Role.SITE_ADMIN, siteAdminActions);

		Set<String> deliveryManagerActions = new HashSet<String>();
		deliveryManagerActions.add(Actions.ADMIN);
		deliveryManagerActions.add(Actions.DELIVERY);
		adminActions.add(Actions.DELIVERY_ASSEMBLY);
		actions.put(Role.DELIVERY_MANAGER, deliveryManagerActions);

		Set<String> finishingManagerActions = new HashSet<String>();
		finishingManagerActions.add(Actions.ADMIN);
		finishingManagerActions.add(Actions.FINISHING);
		actions.put(Role.FINISHING_MANAGER, finishingManagerActions);

		Set<String> vendorActions = new HashSet<String>();
		vendorActions.add(Actions.ADMIN);
		vendorActions.add(Actions.ORDERS_VIEW);
		vendorActions.add(Actions.BILLS);
		vendorActions.add(Actions.USERS_VIEW);
		vendorActions.add(Actions.USERS_MANAGEMENT);
		vendorActions.add(Actions.DISCOUNTS);
		vendorActions.add(Actions.MAILING);
		vendorActions.add(Actions.ORDERS_VENDOR_OPEN);
		actions.put(Role.VENDOR, vendorActions);
	}

	public boolean isAllowed(String action) {
		if (isRoot())
			return true;

		User user = userService.getUser();
		for (Role role : user.getRoles()) {
			Set<String> roleActions = actions.get(role.getType());
			if (roleActions.contains(action))
				return true;
		}
		return false;
	}

	public boolean hasRole(int type) {
		if (isRoot())
			return true;

		User user = userService.getUser();
		for (Role role : user.getRoles()) {
			if (role.getType() == type)
				return true;
		}
		return false;
	}

	public boolean isRoot() {
		User user = userService.getUser();
		for (Role role : user.getRoles()) {
			if (role.getType() == Role.ROOT)
				return true;
		}
		return false;
	}
}
