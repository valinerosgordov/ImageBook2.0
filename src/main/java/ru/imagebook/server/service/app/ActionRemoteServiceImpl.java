package ru.imagebook.server.service.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.client.app.service.ActionRemoteService;
import ru.imagebook.server.service.ActionService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.StatusRequest.Source;


@Service
public class ActionRemoteServiceImpl implements ActionRemoteService {
    @Autowired
    private ActionService actionService;

    @Autowired
    private AuthService authService;

    @Override
    public Integer getRequestState() {
        int userId = authService.getCurrentUserId();
        return actionService.getRequestState(userId);
    }

    @Override
    public void createBonusStatusRequest(String request) {
        int userId = authService.getCurrentUserId();
        actionService.createStatusRequest(request, userId, Source.APP);
    }
}
