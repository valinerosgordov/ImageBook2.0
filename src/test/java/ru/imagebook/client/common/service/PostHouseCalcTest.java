package ru.imagebook.client.common.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;

public class PostHouseCalcTest {
	private PostHouseCalc postHouseCalc;

	@Before
	public void setup() {
		postHouseCalc = new PostHouseCalc();
	}

	@Test
	public void getCost_normal() {
		// Real data
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 245), is(96));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 1960), is(231));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 180), is(89));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 210), is(93));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 705), is(132));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 530), is(118));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 32), is(83));
		assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 0), is(83));
	}

	@Test
	public void getCost_firstClass() {
		assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 0), is(174));
		assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 245), is(212));
		assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 1960), is(533));
	}

    @Test
    public void getCost_fewPackages() {
        assertThat(postHouseCalc.getCost(PostHouseType.NORMAL, 25320), is(2163));
        assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 5130), is(1446));
        assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 2060), is(706));
        assertThat(postHouseCalc.getCost(PostHouseType.FIRST_CLASS, 2160), is(725));
    }

	@Test(expected = IllegalArgumentException.class)
	public void getCost_belowZero() {
		postHouseCalc.getCost(PostHouseType.NORMAL, -10);
	}
	
	@SuppressWarnings("unchecked")
    @Test
	public void isFirstClassAllowed() {
        Order<Product> order = createOrder(290, 200);
        when(order.getQuantity()).thenReturn(2);

        Set<?> orders = ImmutableSet.of(order);
        
        Bill bill = mock(Bill.class);
        when(bill.getOrders()).thenReturn((Set<Order<?>>) orders);
        
        assertTrue(postHouseCalc.isFirstClassAllowed(bill));

        when(order.getQuantity()).thenReturn(3);
        assertFalse(postHouseCalc.isFirstClassAllowed(bill));
	}
	
	@SuppressWarnings("unchecked")
    @Test
	public void isFirstClassAllowed_severalOrders() {
	    // 140 x 205 
	    Order<Product> order1 = createOrder(140, 205);
	    when(order1.getQuantity()).thenReturn(3);
	    
	    // 200 x 200
	    Order<Product> order2 = createOrder(200, 200);
	    when(order2.getQuantity()).thenReturn(2);
	    
	    Set<?> orders = ImmutableSet.of(order1, order2);
        
        Bill bill = mock(Bill.class);
        when(bill.getOrders()).thenReturn((Set<Order<?>>) orders);
	    
	    assertTrue(postHouseCalc.isFirstClassAllowed(bill));
	    
	    when(order2.getQuantity()).thenReturn(3);
	    assertFalse(postHouseCalc.isFirstClassAllowed(bill));
	}
	
	@SuppressWarnings("unchecked")
    private Order<Product> createOrder(int blockWidth, int blockHeight) {
	    Product product = mock(Product.class);
        when(product.getBlockWidth()).thenReturn(blockWidth);
        when(product.getBlockHeight()).thenReturn(blockHeight);
    
        Order<Product> order = mock(Order.class);
        when(order.getProduct()).thenReturn(product);
        
        return order;
	}
}