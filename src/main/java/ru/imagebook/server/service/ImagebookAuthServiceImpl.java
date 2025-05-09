package ru.imagebook.server.service;

import ru.imagebook.server.repository.AuthRepository;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.server.service.auth.AuthConfig;
import ru.imagebook.server.service2.auth.AuthServiceImpl;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;
import ru.saasengine.client.model.auth.AbstractUserAccount;
import ru.saasengine.client.model.auth.Credentials;

import java.util.Set;

public class ImagebookAuthServiceImpl extends AuthServiceImpl {
    private final CoreFactory coreFactory;
    private final String commonPasswordHash;

    public ImagebookAuthServiceImpl(AuthConfig config, AuthRepository repository,
                                    CoreFactory coreFactory, VendorService vendorService, UserRepository userRepository) {
        super(config, repository, coreFactory, vendorService, userRepository);

        this.coreFactory = coreFactory;

        commonPasswordHash = repository.loadCommonPasswordHash();
    }

    @Override
    protected boolean isAuthenticCredentials(Credentials credentials,
                                             AbstractUserAccount account) {
        boolean isAuth = super.isAuthenticCredentials(credentials, account);
        if (isAuth)
            return true;

        UserAccount userAccount = (UserAccount) account;
        User user = userAccount.getUser();
        Set<Role> roles = user.getRoles();
        if (roles.isEmpty() || roles.size() > 1)
            return false;

        Role role = roles.iterator().next();
        if (role.getType() != Role.USER)
            return false;

        Hasher hasher = coreFactory.getHasher();
        return hasher.check(credentials.getPassword(), commonPasswordHash);
    }
}
