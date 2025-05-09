package ru.imagebook.shared.model.editor;

public interface Position extends Rectangle {
	@Override
	Position copy();
}
