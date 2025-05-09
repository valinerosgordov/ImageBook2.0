package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;

public interface PriceRepository {
	List<Album> loadAlbums();

	List<Color> loadColors();

	Album getAlbum(Integer productId);
}
