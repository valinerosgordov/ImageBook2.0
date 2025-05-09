package ru.imagebook.client.flash.view.old;

import java.util.List;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.flash.Flash;

public interface FlashView {
	void showPublishPanel(Order<?> order);

	void confirmPublication();

	void showParamForm(int minWidth, int maxWidth);

	int getFlashWidth();

	void alertInvalidValue();

	void alertWrongValue();

	void setFlashWidth(int width);

	void hideParamForm();

	void showProgress();

	void hideProgress();

	void showFlashes(List<Flash> flashes);

	void confirmFlashDeletion();

	void alertFlashExists();

	void alertTooManyFlashes(int maxFlashes);

	void setPresenter(FlashPresenter presenter);
}
