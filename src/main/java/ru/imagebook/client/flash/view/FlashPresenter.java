package ru.imagebook.client.flash.view;

import ru.imagebook.shared.model.flash.Flash;

public interface FlashPresenter {

	void codeButtonClicked();

	void generateButtonClicked();

	void deleteFlashButtonClicked(Flash flash);

	void flashDeletionConfirmed();
}
