package ru.imagebook.server.service2.flash;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.flash.Flash;

public interface XFlashService {
	void generateFlash(Order<?> order, int flashWidth);

	List<Flash> loadFlashes(Order<?> order);

	void deleteFlash(Order<?> order, int width);

	int getHeight(Album album, int width);
}
