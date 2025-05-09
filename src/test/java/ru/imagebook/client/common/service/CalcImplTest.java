package ru.imagebook.client.common.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.ProductType;
import ru.imagebook.shared.model.pricing.PricingData;

public class CalcImplTest {
	private Order<Album> order;
	private PricingData data;
	private Album album;

	@Before
	public void setup() {
		album = new AlbumImpl();
		album.setType(4);
		album.setNumber(6);
		album.setMultiplicity(8);

		order = new AlbumOrderImpl(album);
		order.setPageCount(100);

		Color color = new Color();
		color.setNumber(0);
		order.setColor(color);

		order.setCoverLamination(0);
		order.setPageLamination(0);

		data = new PricingData();
		data.set("TAPPH", 0d);
		data.set("TAPC", 0d);
		data.set("ПоздравительныйСебестоимость", 0d);
		data.set("ПоздравительныйСтоимость", 0d);
	}

	@Test
	public void getPrintingHousePrice() {
		CalcImpl calc = new CalcImpl(order, data);
		
		album.setPhBasePrice(165);
		album.setPhPagePrice(5);
		album.setPhCoverLaminationPrice(30);
		
		order.setPageCount(23);
		assertThat(calc.getPrintingHousePrice(), is(280));
		
		order.setPageCount(17);
		assertThat(calc.getPrintingHousePrice(), is(250));
		
		album.setPhBasePrice(170);
		album.setPhPagePrice(14);
		album.setPhCoverLaminationPrice(30);
		order.setPageCount(17);
		assertThat(calc.getPrintingHousePrice(), is(408));
	}

	@Test
	public void getPrintingHousePrice_coverLamination() {
		album.setPhBasePrice(165);
		album.setPhPagePrice(5);
		album.setPhCoverLaminationPrice(30);

		order.setPageCount(23);
		
		CalcImpl calc = new CalcImpl(order, data);
		
		order.setCoverLamination(CoverLamination.NONE);
		assertThat(calc.getPrintingHousePrice(), is(280));
		
		order.setCoverLamination(CoverLamination.GLOSSY);
		assertThat(calc.getPrintingHousePrice(), is(310));
		
		order.setCoverLamination(CoverLamination.MATTE);
		assertThat(calc.getPrintingHousePrice(), is(310));
	}

	@Test
	public void getPrintingHousePrice_pageLamination() {
		album.setPhBasePrice(165);
		album.setPhPagePrice(5);
		album.setPhPageLaminationPrice(10);

		order.setPageCount(23);

		CalcImpl calc = new CalcImpl(order, data);

		order.setPageLamination(PageLamination.NONE);
		assertThat(calc.getPrintingHousePrice(), is(280));

		order.setPageLamination(PageLamination.GLOSSY);
		assertThat(calc.getPrintingHousePrice(), is(510));

		order.setPageLamination(PageLamination.MATT);
		assertThat(calc.getPrintingHousePrice(), is(510));
	}

	@Test
	public void getAlbumPrice_coverLamination() {
		album.setBasePrice(165);
		album.setPagePrice(5);
		album.setCoverLaminationPrice(30);
		album.setType(ProductType.CLIP);

		order.setPageCount(23);

		CalcImpl calc = new CalcImpl(order, data);

		order.setCoverLamination(CoverLamination.NONE);
		assertThat(calc.getAlbumPrice(), is(280d));

		order.setCoverLamination(CoverLamination.GLOSSY);
		assertThat(calc.getAlbumPrice(), is(310d));

		order.setCoverLamination(CoverLamination.MATTE);
		assertThat(calc.getAlbumPrice(), is(310d));
	}

	@Test
	public void getAlbumPrice_pageLamination() {
		album.setBasePrice(165);
		album.setPagePrice(5);
		album.setPageLaminationPrice(10);

		order.setPageCount(23);

		CalcImpl calc = new CalcImpl(order, data);

		order.setPageLamination(PageLamination.NONE);
		assertThat(calc.getAlbumPrice(), is(280d));

		order.setPageLamination(PageLamination.GLOSSY);
		assertThat(calc.getAlbumPrice(), is(510d));

		order.setPageLamination(PageLamination.MATT);
		assertThat(calc.getAlbumPrice(), is(510d));
	}
}
