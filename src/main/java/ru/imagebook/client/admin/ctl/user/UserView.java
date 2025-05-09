package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.imagebook.client.admin.view.user.UserPresenter;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.gxt.form.ObjectFieldCallback;

public interface UserView {
	void setPresenter(UserPresenter presenter);

	void alertNoUsersSelected();

	void alertInvitationAlreadySent();

	void showUsersSection();

	void alertAlbumDiscountExists();

	void hideAddForm();

	void reload();

	void hideEditForm();

	void confirmDeleteUsers(List<User> users);

	void alertCannotDeleteOwnUser();

	void alertNoUsersToDelete();

	void alertUserExists();

	void showUsers(List<User> users, int offset, int total, String locale);

	void showEditForm(User user, String locale, List<Vendor> vendors);

	void showAddForm(User user, String locale, List<Vendor> vendors);

	void showProducts(List<Product> products, int offset, long total, ObjectFieldCallback<Product> callback);
}
