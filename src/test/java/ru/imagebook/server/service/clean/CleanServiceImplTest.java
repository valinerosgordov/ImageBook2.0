package ru.imagebook.server.service.clean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_14_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_20_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_30_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_60_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_7_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_IN_90_DAYS;
import static ru.imagebook.server.service.clean.CleanService.NOT_PAID_ORDER_STORAGE_PERIOD_DAYS;
import ru.imagebook.server.repository.CleanRepository;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderImpl;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.StorageState;


@RunWith(MockitoJUnitRunner.class)
public class CleanServiceImplTest {
    private static final int TEST_ORDER_IMPORT_ID = 1;

    @Spy
    @InjectMocks
    private CleanServiceImpl cleanService;

    @Mock
    private CleanRepository repository;

    @Mock
    private PickbookClient pickbookClient;

    @Test
    public void testCleanDeletedOrders() {
        Order<?> order = mock(OrderImpl.class);
        when(order.getState()).thenReturn(OrderState.DELETED);
        when(repository.loadStorageOrders()).thenReturn(new ArrayList<Order<?>>(Arrays.asList(order)));
        doNothing().when(cleanService).cleanOrder(any(Order.class));

        cleanService.cleanOrders();

        verifyCleanOrder(order);
    }

    @Test
    public void testCleanSentOrdersExceededSentOrdersStoragePeriod() {
        Order<?> order = mock(OrderImpl.class);
        when(order.getState()).thenReturn(OrderState.SENT);
        when(repository.loadStorageOrders()).thenReturn(new ArrayList<Order<?>>(Arrays.asList(order)));
        when(order.getSentDate())
                .thenReturn(DateUtils.addDays(new Date(), -(CleanService.SENT_ORDER_STORAGE_PERIOD_DAYS + 1)));
        doNothing().when(cleanService).cleanOrder(any(Order.class));

        cleanService.cleanOrders();

        verifyCleanOrder(order);
    }

    private void verifyCleanOrder(Order<?> order) {
        verify(cleanService).cleanOrders();
        verify(cleanService).cleanOrder(order);
        verifyNoMoreInteractions(cleanService);
    }

    @Test
    public void testCleanSentOrdersNonExceededSentOrdersStoragePeriod() {
        Order<?> order = mock(OrderImpl.class);
        when(order.getState()).thenReturn(OrderState.SENT);
        when(repository.loadStorageOrders()).thenReturn(new ArrayList<Order<?>>(Arrays.asList(order)));
        when(order.getSentDate())
                .thenReturn(DateUtils.addDays(new Date(), -CleanService.SENT_ORDER_STORAGE_PERIOD_DAYS));
        doNothing().when(cleanService).cleanOrder(any(Order.class));

        cleanService.cleanOrders();

        verify(cleanService).cleanOrders();
        verify(cleanService, never()).cleanOrder(order);
        verifyNoMoreInteractions(cleanService);
    }

    @Test
    public void testCleanNotPaidOrders() {
        Order<?> order = mock(OrderImpl.class);
        when(order.getState()).thenReturn(OrderState.NEW);
        when(repository.loadStorageOrders()).thenReturn(new ArrayList<Order<?>>(Arrays.asList(order)));
        doNothing().when(cleanService).cleanNotPaidOrders(any(Order.class), any(DateTime.class));

        cleanService.cleanOrders();

        verify(cleanService).cleanOrders();
        verify(cleanService).cleanNotPaidOrders(any(Order.class), any(DateTime.class));
        verifyNoMoreInteractions(cleanService);
    }

    @Test
    public void testNotPaidExternalOrdersNotify() {
        Order<?> order = createOrder(OrderType.EXTERNAL, OrderState.NEW);
        doNothing().when(cleanService).notifyOrderNotPaid(any(Order.class), any(DateTime.class), anyInt());

        DateTime today = new DateTime();
        DateTime[] notifyPeriods = new DateTime[]{
                today.minusDays(NOT_PAID_ORDER_IN_7_DAYS),
                today.minusDays(NOT_PAID_ORDER_IN_30_DAYS),
                today.minusDays(NOT_PAID_ORDER_IN_60_DAYS)
        };

        for (DateTime period : notifyPeriods) {
            doReturn(period.toDate()).when(cleanService).getOrderLastActivityDate(order);
            cleanService.cleanNotPaidOrders(order, today);
        }

        verify(cleanService, times(notifyPeriods.length)).cleanNotPaidOrders(order, today);
        verify(cleanService, times(notifyPeriods.length)).getOrderLastActivityDate(order);
        verify(cleanService, times(notifyPeriods.length))
                .notifyOrderNotPaid(any(Order.class), any(DateTime.class), anyInt());
        verifyNoMoreInteractions(cleanService);
    }

    @Test
    public void testNotPaidExternalOrdersClean() {
        Order<?> order = createOrder(OrderType.EXTERNAL, OrderState.NEW);
        DateTime today = new DateTime();
        doReturn(today.minusDays(NOT_PAID_ORDER_IN_90_DAYS).toDate())
                .when(cleanService).getOrderLastActivityDate(order);
        doNothing().when(cleanService).cleanOrder(order);

        cleanService.cleanNotPaidOrders(order, today);

        verify(cleanService).cleanNotPaidOrders(order, today);
        verify(cleanService).getOrderLastActivityDate(order);
        verify(cleanService).cleanOrder(order);
        verifyNoMoreInteractions(cleanService);

        Assert.assertEquals(OrderState.DELETED, order.getState().intValue());
    }

    @Test
    public void testNotPaidNonExternalOrdersNotify() {
        Order<?> order = createOrder(OrderType.EDITOR, OrderState.NEW);
        doNothing().when(cleanService).notifyOrderNotPaid(any(Order.class), any(DateTime.class), anyInt());

        DateTime today = new DateTime();
        DateTime[] notifyPeriods = new DateTime[]{
                today.minusDays(NOT_PAID_ORDER_IN_7_DAYS),
                today.minusDays(NOT_PAID_ORDER_IN_14_DAYS),
                today.minusDays(NOT_PAID_ORDER_IN_20_DAYS)
        };

        for (DateTime period : notifyPeriods) {
            when(order.getDate()).thenReturn(period.toDate());
            cleanService.cleanNotPaidOrders(order, today);
        }

        verify(cleanService, times(notifyPeriods.length)).cleanNotPaidOrders(order, today);
        verify(cleanService, times(notifyPeriods.length))
                .notifyOrderNotPaid(any(Order.class), any(DateTime.class), anyInt());
        verifyNoMoreInteractions(cleanService);
    }

    @Test
    public void testNotPaidNonExternalOrdersClean() {
        Order<?> order = createOrder(OrderType.MPHOTO, OrderState.NEW);
        DateTime today = new DateTime();
        when(order.getDate()).thenReturn(today.minusMonths(NOT_PAID_ORDER_STORAGE_PERIOD_DAYS).toDate());
        doNothing().when(cleanService).cleanOrder(order);

        cleanService.cleanNotPaidOrders(order, today);

        verify(cleanService).cleanNotPaidOrders(order, today);
        verify(cleanService).cleanOrder(order);
        verifyNoMoreInteractions(cleanService);

        Assert.assertEquals(OrderState.DELETED, order.getState().intValue());
    }

    @Test
    public void testCleanExternalOrder() {
        Order<?> order = createOrder(OrderType.EXTERNAL, 0);
        doNothing().when(cleanService).cleanFlash(order);
        doNothing().when(cleanService).cleanJpeg(order);

        cleanService.cleanOrder(order);

        verify(cleanService).cleanFlash(order);
        verify(cleanService).cleanJpeg(order);
        verify(cleanService, never()).cleanEditorLayout(order);
        verify(pickbookClient).cleanAlbum(TEST_ORDER_IMPORT_ID);

        Assert.assertEquals(StorageState.DELETED, order.getStorageState());
    }

    @Test
    public void testCleanNonExternalOrders() {
        Set<Integer> orderTypes = OrderType.values.keySet();
        orderTypes.remove(OrderType.EXTERNAL);

        for (int orderType : orderTypes) {
            Order<?> order = createOrder(orderType, 0);
            doNothing().when(cleanService).cleanFlash(order);
            doNothing().when(cleanService).cleanJpeg(order);
            doNothing().when(cleanService).cleanEditorLayout(order);

            cleanService.cleanOrder(order);

            verify(cleanService).cleanFlash(order);
            verify(cleanService).cleanJpeg(order);
            verify(cleanService).cleanEditorLayout(order);
            verify(pickbookClient, never()).cleanAlbum(TEST_ORDER_IMPORT_ID);

            Assert.assertEquals(StorageState.DELETED, order.getStorageState());
        }
    }

    private Order<?> createOrder(int type, int state) {
        Order<?> order = spy(OrderImpl.class);
        order.setType(type);
        order.setState(state);
        if (type == OrderType.EXTERNAL) {
            order.setImportId(TEST_ORDER_IMPORT_ID);
        }
        return order;
    }
}
