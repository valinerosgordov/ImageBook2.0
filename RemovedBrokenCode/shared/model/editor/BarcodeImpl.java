package ru.imagebook.shared.model.editor;

public class BarcodeImpl extends RectangleImpl implements Barcode {
	private static final long serialVersionUID = 642364780949532811L;

	public BarcodeImpl() {}

	public BarcodeImpl(Barcode prototype) {
		super(prototype);
	}

	@Override
	public Barcode copy() {
		return new BarcodeImpl(this);
	}

	@Override
	public void accept(ComponentVisitor visitor) {
		visitor.visit(this);
	}
}
