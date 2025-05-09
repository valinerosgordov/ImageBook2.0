package ru.imagebook.shared.model.editor;

public interface ComponentVisitor {
	void visit(Image image);

	void visit(Barcode barcode);

	void visit(Position position);

	void visit(SafeArea safeArea);
}
