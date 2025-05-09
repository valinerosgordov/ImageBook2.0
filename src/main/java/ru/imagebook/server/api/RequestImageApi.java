package ru.imagebook.server.api;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.imagebook.server.service.FileService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by zinchenko on 27.09.14.
 */
@Controller
@RequestMapping("/image")
public class RequestImageApi {

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/{entity}/{name:.*}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getImage(HttpServletResponse response, @PathVariable String entity,
                         @PathVariable String name) throws IOException {
        InputStream inputStream = fileService.getStreamOfFile(entity, name);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }

    public FileService getFileService() {
        return fileService;
    }

    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
