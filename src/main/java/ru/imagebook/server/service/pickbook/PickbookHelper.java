package ru.imagebook.server.service.pickbook;

import org.springframework.stereotype.Service;

import ru.imagebook.server.model.importing.XFtp;
import ru.imagebook.shared.model.Order;


@Service
public interface PickbookHelper {
    void downloadJpegs(Order order, XFtp ftp, String jpegFolder);
}
