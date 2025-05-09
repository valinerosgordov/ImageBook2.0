package ru.imagebook.server.servlet.integration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.common.service.CalcImpl;
import ru.imagebook.server.model.integration.catalog.CatalogItem;
import ru.imagebook.server.service.site.SiteConfig;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.text.StringUtil;

/**
 * 
 * @author Svyatoslav Gulin
 * 
 */
public class CatalogItemConverter {

	private String locale;
	private Map<Integer, Color> colors;
	private PricingData pricingData;
	private SiteConfig config;

	public CatalogItemConverter(String locale, Map<Integer, Color> colors,
			PricingData pricingData, SiteConfig config) {
		super();
		this.locale = locale;
		this.colors = colors;
		this.pricingData = pricingData;
		this.config = config;
	}

	public List<CatalogItem> convert(Collection<Album> albums) {
		List<CatalogItem> items = new ArrayList<CatalogItem>();

		if ((albums != null) && (!albums.isEmpty())) {
			for (Album album : albums) {
				items.add(convert(album));
			}
		}
		
		return items;
	}

	private CatalogItem convert(Album album) {
		CatalogItem product = new CatalogItem();

		product.setId(album.getId());
		product.setBlockFormat(album.getBlockFormat());
		// product.setCategoryId(categoryId);
		product.setCoverLamRange(album.getCoverLamRange());
		product.setCoverText(ProductType.values.get(album.getType())
				.get(locale));
		product.setMaxPageCount(album.getMaxPageCount());
		product.setName(album.getName().get(locale));
		try {
			product.setMaxPrice(calculateProductMaxPrice(album, colors,
					pricingData));
		} catch (NullPointerException e) {
			product.setMaxPrice(0);
			e.printStackTrace();
			// ignore
		}

		product.setMinPageCount(album.getMinPageCount());
		try {
			product.setMinPrice(calculateProductMinPrice(album, colors,
					pricingData));
		} catch (NullPointerException e) {
			product.setMinPrice(0);
			e.printStackTrace();
			// ignore
		}
		product.setPageLamRange(album.getPageLamRange());
		product.setPaperText(Paper.values.get(album.getPaper()).get(locale));

		product.setCoverLaminationText(buildCoverLaminationText(album, locale));
		product.setPageLaminationText(buildPageLaminationText(album, locale));

		product.setMainImagePath(config.getFilesUrl()+  "/dir/image/item/" + album.getId() + ".jpg");
		product.setImages(getImages(album.getId()));
		
		return product;
	}

	private List<String> getImages(Integer itemId) {
		
		File folder = new File(config.getFilesPath() + "/dir/image/item/" + itemId);
		String[] files = folder.list();
		
		List<String> filePaths = new ArrayList<String>();
		if (files != null) {
			Arrays.sort(files);
			for (String fileName : files) {
				filePaths.add(config.getFilesUrl()+  "/dir/image/item/"  + itemId + "/" + fileName);
			}
		} 
		
		return filePaths; 
	}
	
	private String buildCoverLaminationText(Album album, String locale) {
		List<String> coverLams = new ArrayList<String>();

		for (int coverLam : album.getCoverLamRange()) {
			coverLams.add(CoverLamination.values.get(coverLam).get(locale));
		}

		return StringUtil.implode(" / ", coverLams);
	}

	private String buildPageLaminationText(Album album, String locale) {
		List<String> coverLams = new ArrayList<String>();

		for (int coverLam : album.getPageLamRange()) {
			coverLams.add(PageLamination.values.get(coverLam).get(locale));
		}

		return StringUtil.implode(" / ", coverLams);
	}

	private int calculateProductMinPrice(Album album,
			Map<Integer, Color> colors, PricingData pricingData) {
		AlbumOrder order = new AlbumOrderImpl(album);

		if ((album.getColorRange() != null)
				&& (!album.getColorRange().isEmpty())) {
			int color = album.getColorRange().get(0);
			order.setColor(colors.get(color));
		}

		order.setCoverLamination(album.getCoverLamRange().get(0));
		order.setPageLamination(album.getPageLamRange().get(0));

		order.setPageCount(album.getMinPageCount());
		CalcImpl calc = new CalcImpl(order, pricingData);

		int minPrice;
		if (album.isTrialAlbum()) {
			minPrice = 0;
		} else {
			minPrice = calc.getImagebookPrice();
		}

		return minPrice;
	}

	private int calculateProductMaxPrice(Album album,
			Map<Integer, Color> colors, PricingData pricingData) {
		AlbumOrder order = new AlbumOrderImpl(album);

		if ((album.getColorRange() != null)
				&& (!album.getColorRange().isEmpty())) {
			int color = album.getColorRange().get(0);
			order.setColor(colors.get(color));
		}

		order.setPageCount(album.getMaxPageCount());

		List<Integer> pageLamRange = album.getPageLamRange();
		order.setPageLamination(pageLamRange.get(pageLamRange.size() - 1));

		List<Integer> coverLamRange = album.getCoverLamRange();
		order.setCoverLamination(coverLamRange.get(coverLamRange.size() - 1));

		CalcImpl calc = new CalcImpl(order, pricingData);

		return calc.getImagebookPrice();
	}
}
