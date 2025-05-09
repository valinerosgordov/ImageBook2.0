package ru.imagebook.server.service.auth;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.XUser;
import ru.minogin.auth.server.service.SecurityService;

public interface XSecurityService extends SecurityService<XUser> {

    void checkOrderOwner(Order<?> order);

    void checkOrderOwner(Order<?> order, int currentUserId);
}
