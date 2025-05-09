package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;

public interface ProductService {
	List<Album> loadAlbums();

	List<Color> loadColors();

	void addAlbum(Album album);

	void deleteAlbums(List<Integer> ids);

	void updateAlbum(Album album);

	void updateColors(List<Color> colors);

	void deleteColors(List<Color> colors);
}
