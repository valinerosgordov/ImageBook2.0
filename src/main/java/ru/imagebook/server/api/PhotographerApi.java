package ru.imagebook.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.imagebook.server.model.Photographer;
import ru.imagebook.server.service.ActionService;
import ru.imagebook.server.service.MainConfig;
import ru.imagebook.server.service.RegisterService;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.StatusRequest.Source;
import ru.minogin.core.client.common.AccessDeniedError;

/**

 * @since 2017-09-19
 */
@Controller
@RequestMapping("/photographer")
public class PhotographerApi {
    @Autowired
    private RegisterService registerService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private MainConfig mainConfig;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(@Validated Photographer photographer, @RequestParam("appCode") String appCode) {
        if (!mainConfig.getAppCode().equals(appCode)) {
            throw new AccessDeniedError();
        }

        Integer userId = registerService.register(photographer);
        actionService.createStatusRequest(photographer.getPortfolio(), userId, Source.SITE_FORM);
    }
}