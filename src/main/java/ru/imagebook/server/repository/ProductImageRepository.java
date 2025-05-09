package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.ProductImage;

public interface ProductImageRepository extends Repository<ProductImage, Integer> {
    List<ProductImage> loadPhotos(Integer productId, int offset, int limit);

    List<ProductImage> loadPhotos(Integer productId);

    long countPhotos(Integer productId);

    List<ProductImage> loadPhotos(List<Integer> ids);

    void deletePhotos(List<ProductImage> images);
}