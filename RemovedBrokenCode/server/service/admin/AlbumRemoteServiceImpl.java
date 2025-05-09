package ru.imagebook.server.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.AlbumRemoteService;
import ru.imagebook.server.repository.UserRepository;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.admin.UsersResult;
import ru.minogin.core.server.hibernate.Dehibernate;

/**
 * Created by rifat on 11.01.17.
 */
@Service
public class AlbumRemoteServiceImpl implements AlbumRemoteService {
    @Autowired
    private UserRepository userRepository;

    @Dehibernate
    @Transactional
    @Override
    public UsersResult loadUsers(int offset, int limit, String query){
        List<User> users = userRepository.loadUsers(offset, limit, query);
        long usersCount = userRepository.countUsers(query);
        return new UsersResult(users, usersCount);
    }
}