package ru.imagebook.server.service.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.client.app.service.SentRemoteService;
import ru.imagebook.server.repository.SentRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.Dehibernate;

@Service
public class SentRemoteServiceImpl implements SentRemoteService {
    @Autowired
	private SentRepository repository;

    @Autowired
    private AuthService authService;

    @Dehibernate
    @Override
    public List<Order<?>> loadSentOrders() {
        int userId = authService.getCurrentUserId();
        return repository.loadOrders(userId);
    }
}
