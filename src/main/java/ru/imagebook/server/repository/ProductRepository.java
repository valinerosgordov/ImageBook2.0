package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Product;

public interface ProductRepository {
	Product getProduct(int productId);

	List<Album> loadAlbums();

	List<Color> loadColors();

	void saveAlbum(Album album);

	void deleteAlbums(List<Integer> ids);

	void updateAlbum(Album album);

	void saveOrUpdate(Color color);

	void deleteColors(List<Color> colors);
	
	Album getAlbumByProductTypeAndNumber(int type, int number);

	List<Product> loadProducts(int offset, int limit, String query);

	long countProducts(String query);
}
