package ru.imagebook.client.common.service;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;


public class MultishipBoxCalcTest {

    @Test
    public void calcBoxDimensions() {
        // horizontal album
        assertThat(MultishipBoxCalc.getBoxHeight(10), is(30));
        assertThat(MultishipBoxCalc.getBoxWidth(250), is(30));
        assertThat(MultishipBoxCalc.getBoxLength(200), is(25));

        assertThat(MultishipBoxCalc.getBoxHeight(3), is(9));
        assertThat(MultishipBoxCalc.getBoxWidth(261), is(31));
        assertThat(MultishipBoxCalc.getBoxLength(200), is(25));

        // vertical album
        assertThat(MultishipBoxCalc.getBoxHeight(3), is(9));
        assertThat(MultishipBoxCalc.getBoxWidth(200), is(25));
        assertThat(MultishipBoxCalc.getBoxLength(250), is(30));

        assertThat(MultishipBoxCalc.getBoxHeight(5), is(15));
        assertThat(MultishipBoxCalc.getBoxWidth(210), is(26));
        assertThat(MultishipBoxCalc.getBoxLength(300), is(35));

        // square album
        assertThat(MultishipBoxCalc.getBoxHeight(7), is(21));
        assertThat(MultishipBoxCalc.getBoxWidth(290), is(34));
        assertThat(MultishipBoxCalc.getBoxLength(290), is(34));
        assertEquals(MultishipBoxCalc.getBoxWidth(290), MultishipBoxCalc.getBoxLength(290));

        assertThat(MultishipBoxCalc.getBoxHeight(2), is(6));
        assertThat(MultishipBoxCalc.getBoxWidth(140), is(19));
        assertThat(MultishipBoxCalc.getBoxLength(140), is(19));
        assertEquals(MultishipBoxCalc.getBoxWidth(140), MultishipBoxCalc.getBoxLength(140));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calcBoxDimension_sevralOrdersInBill() {
        Order<Product> order1 = createOrder(210, 300);
        when(order1.getQuantity()).thenReturn(3);

        Order<Product> order2 = createOrder(200, 200);
        when(order2.getQuantity()).thenReturn(2);

        Set<?> orders = ImmutableSet.of(order1, order2);

        Bill bill = mock(Bill.class);
        when(bill.getOrders()).thenReturn((Set<Order<?>>) orders);

        int maxAlbumLength = 0;
        int maxAlbumWidth = 0;
        int totalOrdersQuantity = 0;

        for (Order<?> order : bill.getOrders()) {
            totalOrdersQuantity += order.getQuantity();
            maxAlbumLength = Math.max(maxAlbumLength, order.getProduct().getBlockHeight());
            maxAlbumWidth = Math.max(maxAlbumWidth, order.getProduct().getBlockWidth());
        }

        assertThat(MultishipBoxCalc.getBoxHeight(totalOrdersQuantity), is(15));
        assertThat(MultishipBoxCalc.getBoxWidth(maxAlbumWidth), is(26));
        assertThat(MultishipBoxCalc.getBoxLength(maxAlbumLength), is(35));
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
