package ru.imagebook.client.admin.ctl.product;

import java.util.Collection;
import java.util.List;

import ru.imagebook.client.admin.view.product.AlbumPresenter;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.User;

public interface AlbumView {
	void showAlbumsSection();

	void showAlbums(List<Album> albums, String locale);

	void showUsers(List<User> users, int offset, long total, String locale);

	void showAddForm(Album album, String locale, Collection<String> locales);

	void showColorRangeField(List<Color> colors, String locale);

	void setPresenter(AlbumPresenter presenter);

	void hideAddForm();

	void confirmDeleteAlbums(List<Album> albums);

	void alertNoAlbumsToDelete();

	void alertAlbumUsed();

	void showEditForm(Album album, String locale, Collection<String> locales);

	void hideEditForm();

	Album getSelectedAlbum();

	void productSelectionEmpty();
}
