package ru.imagebook.server.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.XUser;
import ru.minogin.auth.server.service.SecurityServiceImpl;
import ru.saasengine.client.service.auth.AuthError;

@Service("xSecurityService")
public class XSecurityServiceImpl extends SecurityServiceImpl<XUser> implements XSecurityService {

    @Autowired
    private AuthService authService;

    @Transactional
    @Override
    public void checkOrderOwner(Order<?> order) {
        checkOrderOwner(order, authService.getCurrentUserId());
    }

    @Override
    public void checkOrderOwner(Order<?> order, int currentUserId) {
        User user = order.getUser();
        if (user.getId() != currentUserId) {
            throw new AuthError();
        }
    }
}
