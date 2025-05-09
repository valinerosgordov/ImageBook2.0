package ru.imagebook.server.service;

import ru.imagebook.server.model.Photographer;

public interface RegisterService {
    void register(String name, String email, String captcha, String module);

    Integer register(Photographer photographer);
}
