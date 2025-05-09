package ru.imagebook.client.admin.view.finishing;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Cover;
import ru.imagebook.shared.model.ProductType;

public class CoverUtil {
	public static String getCoverName(AlbumOrder order, String locale) {
		Album album = order.getProduct();
		String name = album.getCoverName();
		Integer type = album.getType();
		if (name == null) {
			if (type == ProductType.EVERFLAT_WHITE_MARGINS
					|| type == ProductType.EVERFLAT_FULL_PRINT
					|| type == ProductType.HARD_COVER_WHITE_MARGINS
					|| type == ProductType.HARD_COVER_FULL_PRINT
					|| type == ProductType.TABLET
					|| type == ProductType.CLIP) {
				name = Cover.values.get(album.getCover()).get(locale);
			} else {
				name = order.getColor().getName().get(locale);
			}
		}
		return name;
	}
}
