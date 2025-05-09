package ru.imagebook.server.api.pickbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.server.service.pickbook.PickbookUserService;

/**

 * @since 20.11.2014
 */
@Controller
@RequestMapping("/pickbook/users")
public class PickbookUserController {

    @Autowired
    private PickbookUserService pickbookUserService;

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void loadUser(@RequestBody XUser xUser) {
        pickbookUserService.createUser(xUser);
    }
}
