package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;

public interface ColorView {
	void showColors(List<Color> colors);

	void confirmDeleteColors(List<Color> colors);

	void alertColorUsed();
}
