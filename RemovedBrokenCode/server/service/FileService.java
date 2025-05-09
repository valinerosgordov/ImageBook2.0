package ru.imagebook.server.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zinchenko on 27.09.14.
 */
public interface FileService {

    InputStream getStreamOfFile(String entity, String name) throws FileNotFoundException;

    String saveFile(String entity, MultipartFile file) throws IOException;

    void deleteFile(String entity, String fileName);

}
