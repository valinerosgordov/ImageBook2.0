package ru.imagebook.server.web.admin;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import ru.imagebook.server.service.FilePathConfig;
import ru.imagebook.server.service.FileService;
import ru.imagebook.shared.service.admin.product.ProductImageFileUploadedMessage;
import ru.minogin.core.server.push.PushService;

@Controller
public class ProductImageController {
    @Autowired
    private FileService fileService;

    @Autowired
    private PushService pushService;

    @RequestMapping(value = "/product/uploadImageFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void upload(@RequestParam("image") MultipartFile file) throws IOException {
        ProductImageFileUploadedMessage message = new ProductImageFileUploadedMessage();
        String fileName = fileService.saveFile(FilePathConfig.PRODUCT_PHOTO_ENTITY, file);
        message.setSourceFile(file.getOriginalFilename());
        message.setPhotoPath(fileName);
        pushService.sendToAuthenticatedUser(message);
    }
}
