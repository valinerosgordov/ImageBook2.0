package ru.imagebook.server.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import static ru.imagebook.server.service.FilePathConfig.PRODUCT_PHOTO_ENTITY;
import static ru.imagebook.server.service.FilePathConfig.PRODUCT_TYPE_PHOTO_ENTITY;
import ru.imagebook.server.service.FileService;

@Controller
@RequestMapping("/product")
public class ProductImageController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/type/{productType}/photo", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getTypeImage(@PathVariable Integer productType, HttpServletResponse response) throws IOException {
        String imageFileName = productType + ".jpg";
        getPhoto(imageFileName, PRODUCT_TYPE_PHOTO_ENTITY, response);
    }

    @RequestMapping(value = "/photo/{name:.*}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void getImage(@PathVariable String name, HttpServletResponse response) throws IOException {
        getPhoto(name, PRODUCT_PHOTO_ENTITY, response);
    }

    private void getPhoto(String name, String productTypePhotoEntity, HttpServletResponse response)
        throws IOException {
        InputStream inputStream = fileService.getStreamOfFile(productTypePhotoEntity, name);
        OutputStream outputStream = response.getOutputStream();
        IOUtils.copy(inputStream, outputStream);
    }
}
