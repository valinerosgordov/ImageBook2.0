package ru.imagebook.server.service.editor;


import ru.imagebook.shared.model.BonusAction;

public class EditorServiceCommonError extends RuntimeException {
	private static final long serialVersionUID = 2L;

	final String errorMessage;

	public EditorServiceCommonError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() { return this.errorMessage; }
}
