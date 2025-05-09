package ru.imagebook.server.service.admin;

import static java.util.Collections.singletonList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.ProductImageRemoteService;
import ru.imagebook.server.repository.ProductImageRepository;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.server.service.FilePathConfig;
import ru.imagebook.server.service.FileService;
import ru.imagebook.server.service.site.SiteService;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductImage;
import ru.minogin.core.client.gxt.grid.LoadResult;
import ru.minogin.core.server.hibernate.Dehibernate;
import ru.minogin.core.server.push.PushService;

@Service
public class ProductImageRemoteRemoteServiceImpl implements ProductImageRemoteService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private PushService pushService;

    @Autowired
    private SiteService siteService;

    @Transactional
    @Dehibernate
    @Override
    public LoadResult<ProductImage> loadPhotos(int productId, int offset, int limit) {
        List<ProductImage> images = productImageRepository.loadPhotos(productId, offset, limit);
        long count = productImageRepository.countPhotos(productId);
        setImagesPath(images);
        return new LoadResult<>(images, offset, count);
    }

    private void setImagesPath(List<ProductImage> images) {
        for (ProductImage image : images) {
            image.setPath(getPhotoPath(image));
        }
    }

    @Transactional
    @Override
    public List<ProductImage> loadPhotos(int productId) {
        List<ProductImage> images = productImageRepository.loadPhotos(productId);
        setImagesPath(images);
        return images;
    }

    @Transactional
    @Override
    public void addPhoto(int productId, ProductImage image) {
        Product product = productRepository.getProduct(productId);
        image.setProduct(product);
        productImageRepository.save(image);
    }

    @Transactional
    @Override
    public void updateImage(ProductImage modified) {
        ProductImage image = productImageRepository.find(modified.getId());
        image.setNumber(modified.getNumber());
        image.setDescription(modified.getDescription());
        if (image.getImage() != null && !image.getImage().equals(modified.getImage())) {
            deletePhotoFiles(singletonList(image));
        }
        image.setSourceFile(modified.getSourceFile());
        image.setImage(modified.getImage());
    }

    private void deletePhotoFiles(List<ProductImage> productImages) {
        for (ProductImage productImage : productImages) {
            fileService.deleteFile(FilePathConfig.PRODUCT_PHOTO_ENTITY, productImage.getImage());
        }
    }

    @Override
    public String getPhotoPath(ProductImage image) {
        return siteService.getNonLocalizedUrl("/api/product/photo/") + image.getImage();
    }

    @Override
    public String getProductTypePhotoPath(Integer productType) {
        return siteService.getNonLocalizedUrl("/api/product/type/") + productType + "/photo";
    }

    @Transactional
    @Override
    public void deletePhotos(List<Integer> ids) {
        List<ProductImage> photos = productImageRepository.loadPhotos(ids);
        deletePhotoFiles(photos);
        productImageRepository.deletePhotos(photos);
    }
}
