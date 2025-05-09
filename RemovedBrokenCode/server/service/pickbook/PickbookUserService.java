package ru.imagebook.server.service.pickbook;

import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;


public interface PickbookUserService {
    void createUser(XUser xUser);

    User getOrCreateUser(XUser xUser, Vendor vendor);
}
