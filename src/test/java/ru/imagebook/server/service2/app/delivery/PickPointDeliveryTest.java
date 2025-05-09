package ru.imagebook.server.service2.app.delivery;

import net.sf.ehcache.store.disk.ods.AATreeSet;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.imagebook.client.common.util.i18n.I18n;
import ru.imagebook.server.repository.BillRepository;
import ru.imagebook.server.service.BillService;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.model.app.PickPointData;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.server.CoreFactoryImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author Sergey Boykov
 */
@ContextConfiguration(classes = PickPointDeliveryTest.Config.class)
@Test(groups = {"integration"})
public class PickPointDeliveryTest extends AbstractTestNGSpringContextTests {

    private static final int TIMEOUT = 60000;

    @Autowired
    private PickPointDeliveryService deliveryService;
    @Autowired
    private PickPointHelper pickPointHelper;
    @Autowired
    private PickPointTransferSendingService transferService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillService billService;

    @BeforeMethod
    public void init() {
        reset(billService);
    }

    @Test(timeOut = TIMEOUT)
    public void shouldBeAbleToLogin() {
        String sessionId = pickPointHelper.login();
        assertNotNull(sessionId, "Unable to login to PickPoint, sessionId");
    }

    @Test(timeOut = TIMEOUT)
    public void shouldBeAbleToLogout() {
        String sessionId = pickPointHelper.login();
        assertTrue(pickPointHelper.logout(sessionId), "Unable to login to PickPoint, sessionId");
    }

    @Test(timeOut = TIMEOUT)
    public void shouldReturnZoneInfoByPostamateId() {
        /*
			Some real postamates
			{"DeliveryMax":2,"DeliveryMin":1,"FromCity":"Москва","Koeff":1,"ToCity":"Санкт-Петербург","ToPT":"7801-001","Zone":"0"},
			{"DeliveryMax":2,"DeliveryMin":1,"FromCity":"Москва","Koeff":1,"ToCity":"Санкт-Петербург","ToPT":"7801-003","Zone":"0"},
			{"DeliveryMax":2,"DeliveryMin":1,"FromCity":"Москва","Koeff":1,"ToCity":"Санкт-Петербург","ToPT":"7801-004","Zone":"0"}
		 */
        PickPointData pickPointData = deliveryService.getZoneInfoByPostamateId("7801-001");
        assertTrue(pickPointData.getTimeMin() > 0, "Min delivery time should be  > 0");
    }

    @Test(timeOut = TIMEOUT)
    public void shouldCreateSending() {
        List<Bill> readyBills = new ArrayList<>();
        Bill goodBill1 = new Bill();
        readyBills.add(goodBill1);
        goodBill1.setId(1);
        goodBill1.setPickpointPostamateID("7801-001");
        Album album = new AlbumImpl();
        album.setType(4);
        album.setNumber(123);
        album.setName(I18n.ms("Альбом 1", "Album 1"));
        Order<Album> order = new AlbumOrderImpl(album);
        order.setProduct(album);
        order.setPageCount(10);
        Color color = new Color(0, I18n.ms("Нет", "None"));
        order.setColor(color);
        order.setCoverLamination(1);
        order.setPageLamination(2);
        goodBill1.setOrders(new AATreeSet<Order<?>>());
        goodBill1.getOrders().add(order);
        Address address = new Address();
        address.setLastName("Иванов");
        address.setName("Иван");
        address.setPhone("+74951234567");
        address.setEmail("aaa@bbb.ccc");
        order.attachAddress(address);

        when(billRepository.loadReadyToTransferToPickPoint()).thenReturn(readyBills);

        transferService.transfer();

        ArgumentCaptor<Bill> billArg = ArgumentCaptor.forClass(Bill.class);
        verify(billService).saveBill(billArg.capture());
        assertEquals(Integer.valueOf(DsSendState.SENT), billArg.getValue().getDsSendState(),
                "Couldn't create sending from bill in PickPoint");
    }

    @Configuration
    @ImportResource("classpath*:spring/test-context.xml")
    protected static class Config {
        @Bean
        public RestTemplate pickPointRestTemplate() {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(TIMEOUT);
            requestFactory.setReadTimeout(TIMEOUT);
            return new RestTemplate(requestFactory);
        }

        @Bean
        public PickPointDeliveryService pickPointDeliveryService() {
            return new PickPointDeliveryServiceImpl();
        }

        @Bean
        public PickpointSendingIdGenerator pickpointSendingIdGenerator() {
            return new PickpointSendingIdGenerator() {
                private static final String ID_PREFIX = "imbtst-";

                @Override
                public String generateImageBookSendingId(Bill bill) {
                    return ID_PREFIX + new Date().getTime();
                }
            };
        }

        @Bean
        public PickPointHelper pickPointHelper() {
            return new PickPointHelperImpl();
        }

        @Bean
        public PickPointTransferSendingService pickPointTransferSendingService() {
            return new PickPointTransferSendingServiceImpl();
        }

        @Bean
        public BillRepository billRepository() {
            return mock(BillRepository.class);
        }

        @Bean
        public CoreFactory coreFactory() {
            return new CoreFactoryImpl();
        }

        @Bean
        public BillService billService() {
            return mock(BillService.class);
        }

        @Bean
        public NotifyService notifyService() {
            return mock(NotifyService.class);
        }
    }
}