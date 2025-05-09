package ru.imagebook.server.service.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.client.app.service.ProcessRemoteService;
import ru.imagebook.server.repository.ProcessRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.minogin.core.server.hibernate.Dehibernate;

@Service
public class ProcessRemoteServiceImpl implements ProcessRemoteService {
    @Autowired
	private ProcessRepository repository;

    @Autowired
    private AuthService authService;

    @Dehibernate
    @Override
    public List<Order<?>> loadProcessingOrders() {
        int userId = authService.getCurrentUserId();
        return repository.loadOrders(userId);
    }
}
