package ru.imagebook.client.flash.view.old;

import ru.imagebook.shared.model.flash.Flash;

public interface FlashPresenter {
	void publishFieldClicked();

	void unpublishConfirmed();

	void publicationConfirmed();

	void codeButtonClicked();

	void generateButtonClicked();

	void deleteFlashButtonClicked(Flash flash);

	void deletionConfirmed();
}
