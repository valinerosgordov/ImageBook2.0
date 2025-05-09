package ru.imagebook.client.common.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.junit.Test;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumImpl;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public class CostCalculatorImplTest {
	@SuppressWarnings("unchecked")
	private Order<Product> createNonTrialOrder() {
		Product product = mock(Product.class);
		when(product.isTrialAlbum()).thenReturn(false);
		Order<Product> order = mock(Order.class);
		when(order.getProduct()).thenReturn(product);
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);
		return order;
	}

	@SuppressWarnings("unchecked")
	@Test
	public void calculateTrialAlbumCost() {
		Calc calc = mock(Calc.class);
		when(calc.getImagebookPrice()).thenReturn(50);

		Product product = mock(Product.class);
		when(product.isTrialAlbum()).thenReturn(true);

		Order<Product> order = mock(Order.class);
		when(order.getProduct()).thenReturn(product);
		when(order.getQuantity()).thenReturn(10);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0);
		assertThat(calculator.calculateCost(order), is(10 * 50));
	}

	@Test
	public void calculateCostWithoutDiscount() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 0)).thenReturn(100);
		when(calc.getImagebookPrice()).thenReturn(40);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0);
		assertThat(calculator.calculateCost(order), is(100));
	}

	@Test
	public void calculateCostUsingLevel() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 4)).thenReturn(70);
		when(calc.getIBX(3, 5)).thenReturn(60);
		when(calc.getImagebookPrice()).thenReturn(40);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getLevel()).thenReturn(5);
		CostCalculator calculator = new CostCalculatorImpl(calc, 4, 0);
		assertThat(calculator.calculateCost(order), is(60));

		order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getLevel()).thenReturn(4);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		calculator = new CostCalculatorImpl(calc, 5, 0);
		assertThat(calculator.calculateCost(order), is(60));
	}

	@Test
	public void calculateCostUsingDiscountPc() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 0)).thenReturn(1000);
		when(calc.getImagebookPrice()).thenReturn(200);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getDiscountPc()).thenReturn(20);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 10);
		assertThat(calculator.calculateCost(order), is((int) ((200 * 3) * 0.8)));

		order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getDiscountPc()).thenReturn(20);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		calculator = new CostCalculatorImpl(calc, 0, 30);
		assertThat(calculator.calculateCost(order), is((int) ((200 * 3) * 0.7)));
	}

	@Test
	public void calculateCostUsingDiscountSum() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 0)).thenReturn(500);
		when(calc.getImagebookPrice()).thenReturn(200);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getDiscountSum()).thenReturn(150);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0);
		assertThat(calculator.calculateCost(order), is(350));
	}

	@Test
	public void calculateCostCheckDiscountSumOnlyAppliedToLevelDiscount() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 0)).thenReturn(1000);
		when(calc.getImagebookPrice()).thenReturn(200);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getDiscountSum()).thenReturn(150);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0);
		assertThat(calculator.calculateCost(order), is(600));
	}

	@Test
	public void calculateCostWhenDiscountSumIsTooHigh() {
		Calc calc = mock(Calc.class);
		when(calc.getIBX(3, 0)).thenReturn(500);
		when(calc.getImagebookPrice()).thenReturn(200);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);
		when(order.getDiscountSum()).thenReturn(700);
		
		User user = mock(User.class);
		when(order.getUser()).thenReturn(user);
		Vendor vendor = mock(Vendor.class);
		when(user.getVendor()).thenReturn(vendor);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0);
		assertThat(calculator.calculateCost(order), is(0));
	}
	
	@Test
	public void calculateCostWhenSpecialOfferIsEnabled() {
        Calc calc = mock(Calc.class);
        when(calc.getImagebookPrice()).thenReturn(200);
        when(calc.getPrintingHousePrice()).thenReturn(150);
        
        int productQuantity = 5;
        
        Album album = spy(new AlbumImpl());
        when(album.isHasSpecialOffer()).thenReturn(true);
        when(album.getMinAlbumsCountForDiscount()).thenReturn(4);
        when(album.getImagebookDiscount()).thenReturn(20);
        when(album.getPhDiscount()).thenReturn(30);
        
        @SuppressWarnings("unchecked")
        Order<Album> order = mock(Order.class);
        when(order.getProduct()).thenReturn(album);
        when(order.getQuantity()).thenReturn(productQuantity);

        when(album.isSpecialOfferEnabled(productQuantity)).thenCallRealMethod();
        
        CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0, 0,
            album.isSpecialOfferEnabled(productQuantity));
        assertThat(calculator.calculateCost(order), is(800));
        assertThat(calculator.calculatePhCost(order), is(525));
	}

	@Test
	public void calculateCostWithUserAlbumDiscount() {
		Calc calc = mock(Calc.class);
		when(calc.getImagebookPrice()).thenReturn(200);

		Order<Product> order = createNonTrialOrder();
		when(order.getQuantity()).thenReturn(3);

		CostCalculator calculator = new CostCalculatorImpl(calc, 0, 0, 10, false);
		assertThat(calculator.calculateCost(order), is(540));
	}
}
