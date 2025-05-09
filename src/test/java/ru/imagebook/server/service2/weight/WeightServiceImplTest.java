package ru.imagebook.server.service2.weight;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Formats;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.ProductType;

public class WeightServiceImplTest {
	@Test
	public void getWeight() {
	    WeightServiceImpl service = new WeightServiceImpl();

		Album album = mock(Album.class);
		AlbumOrder order = mock(AlbumOrder.class);
		when(order.getProduct()).thenReturn(album);
		when(order.getId()).thenReturn(0);

		// non exists BB
        when(album.getType()).thenReturn(-1);
        when(album.getNumber()).thenReturn(Formats.CC_06);        
        when(order.getPageCount()).thenReturn(8);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(0));

        // non exists CC
        when(album.getType()).thenReturn(ProductType.EVERFLAT_WHITE_MARGINS);
        when(album.getNumber()).thenReturn(-1);
        when(order.getProduct()).thenReturn(album);
        when(order.getPageCount()).thenReturn(8);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(0));
		
        // all product types
        when(album.getType()).thenReturn(ProductType.EVERFLAT_WHITE_MARGINS);
        when(album.getNumber()).thenReturn(Formats.CC_06);
        when(order.getProduct()).thenReturn(album);
        when(order.getPageCount()).thenReturn(8);
        when(order.getPageLamination()).thenReturn(PageLamination.GLOSSY);
        assertThat(service.getItemWeight(order), is(378));
		
		when(album.getType()).thenReturn(ProductType.EVERFLAT_FULL_PRINT);
        when(album.getNumber()).thenReturn(Formats.CC_08);
        when(order.getPageCount()).thenReturn(16);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(624));
        
        when(album.getType()).thenReturn(ProductType.PANORAMIC);
        when(album.getNumber()).thenReturn(Formats.CC_01);
        when(order.getPageCount()).thenReturn(12);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(50));
        
        when(album.getType()).thenReturn(ProductType.HARD_COVER_WHITE_MARGINS);
        when(album.getNumber()).thenReturn(Formats.CC_03);
        when(order.getPageCount()).thenReturn(10);
        when(order.getPageLamination()).thenReturn(PageLamination.MATT);
        assertThat(service.getItemWeight(order), is(294));
        
        when(album.getType()).thenReturn(ProductType.HARD_COVER_FULL_PRINT);
        when(album.getNumber()).thenReturn(Formats.CC_10);
        when(order.getPageCount()).thenReturn(32);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(1024));

        when(album.getType()).thenReturn(ProductType.STANDARD);
        when(album.getNumber()).thenReturn(Formats.CC_01);
        when(order.getPageCount()).thenReturn(12);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(50));
        
        when(album.getType()).thenReturn(ProductType.SPRING);
        when(album.getNumber()).thenReturn(Formats.CC_07);
        when(order.getPageCount()).thenReturn(20);
        when(order.getPageLamination()).thenReturn(PageLamination.GLOSSY);
        assertThat(service.getItemWeight(order), is(342));

        when(album.getType()).thenReturn(ProductType.SPRING);
        when(album.getNumber()).thenReturn(Formats.CC_31);
        when(order.getPageCount()).thenReturn(20);
        when(order.getPageLamination()).thenReturn(PageLamination.GLOSSY);
        assertThat(service.getItemWeight(order), is(342));
        
        when(album.getType()).thenReturn(ProductType.CLIP);
        when(album.getNumber()).thenReturn(Formats.CC_05);
        when(order.getPageCount()).thenReturn(12);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(189));
        
        when(album.getType()).thenReturn(ProductType.TRIAL);
        when(album.getNumber()).thenReturn(Formats.CC_99);
        when(order.getPageCount()).thenReturn(40);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(198));
        
        when(album.getType()).thenReturn(ProductType.TABLET);
        when(album.getNumber()).thenReturn(Formats.CC_07);
        when(order.getPageCount()).thenReturn(6);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(410));

        when(album.getType()).thenReturn(ProductType.TABLET);
        when(album.getNumber()).thenReturn(Formats.CC_06);
        when(order.getPageCount()).thenReturn(4);
        when(order.getPageLamination()).thenReturn(PageLamination.GLOSSY);
        assertThat(service.getItemWeight(order), is(392));

        when(album.getType()).thenReturn(ProductType.CONGRATULATORY);
        when(album.getNumber()).thenReturn(Formats.CC_32);
        when(order.getPageCount()).thenReturn(56);
        when(order.getPageLamination()).thenReturn(PageLamination.NONE);
        assertThat(service.getItemWeight(order), is(772));

		when(order.getQuantity()).thenReturn(17);
		assertThat(service.getTotalWeight(order), is(772 * 17));
	}
}
