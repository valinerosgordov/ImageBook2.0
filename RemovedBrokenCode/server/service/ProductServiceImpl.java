package ru.imagebook.server.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;

public class ProductServiceImpl implements ProductService {
	private final ProductRepository repository;

	public ProductServiceImpl(ProductRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public List<Album> loadAlbums() {
		return repository.loadAlbums();
	}

	@Override
	@Transactional
	public List<Color> loadColors() {
		return repository.loadColors();
	}
	
	@Override
	public void addAlbum(Album album) {
		repository.saveAlbum(album);
	}
	
	@Override
	public void updateAlbum(Album album) {
		repository.updateAlbum(album);
	}
	
	@Override
	public void deleteAlbums(List<Integer> ids) {
		repository.deleteAlbums(ids);
	}
	
	@Override
	public void updateColors(List<Color> colors) {
		for (Color color : colors) {
			repository.saveOrUpdate(color);
		}
	}
	
	@Override
	public void deleteColors(List<Color> colors) {
		repository.deleteColors(colors);
	}
}
