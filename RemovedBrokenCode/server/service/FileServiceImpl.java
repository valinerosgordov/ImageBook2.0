package ru.imagebook.server.service;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by zinchenko on 27.09.14.
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = Logger.getLogger(FileServiceImpl.class);

    public static final String UNDERSCORE = "_";
    @Autowired
    private FilePathConfig filePathConfig;

    @Override
    public InputStream getStreamOfFile(String entity, String name) throws FileNotFoundException {
        String path = filePathConfig.getPath(entity);
        String fileName = path + File.separator + name;
        File file = new File(fileName);
        if(!file.exists()){
            throw new FileNotFoundException("Can't find file: " + fileName);
        }
        return new FileInputStream(file);
    }

    @Override
    public String saveFile(String entity, MultipartFile file) throws IOException {
        String newName = System.currentTimeMillis() + UNDERSCORE + file.getOriginalFilename();
        String path = filePathConfig.getPath(entity);
        File f = new File(path + File.separator + newName);
        OutputStream outputStream = null;
        try {
            outputStream = FileUtils.openOutputStream(f);
            outputStream.write(file.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            new RuntimeException("Error while saving file recommendation file", e);
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return newName;
    }

    @Override
    public void deleteFile(String entity, String fileName) {
        String path = filePathConfig.getPath(entity);
        File file = new File(path + File.separator + fileName);
        file.delete();
    }

    public FilePathConfig getFilePathConfig() {
        return filePathConfig;
    }

    public void setFilePathConfig(FilePathConfig filePathConfig) {
        this.filePathConfig = filePathConfig;
    }
}
