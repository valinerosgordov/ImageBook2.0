package ru.imagebook.shared.model;

public class AlbumOrderImpl extends OrderImpl<Album> implements AlbumOrder {
	private static final long serialVersionUID = -826825260437658449L;

	AlbumOrderImpl() {}

	public AlbumOrderImpl(Album album) {
		super(album);
	}
}
