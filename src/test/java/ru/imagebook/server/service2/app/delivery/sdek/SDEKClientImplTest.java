package ru.imagebook.server.service2.app.delivery.sdek;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryAddress;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryOrder;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryPackage;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryResponse;
import ru.imagebook.server.service2.app.delivery.sdek.model.PackageItem;
import ru.imagebook.server.service2.app.delivery.sdek.model.ResponseOrder;
import ru.minogin.core.client.crypto.Hasher;
import ru.minogin.core.server.crypto.HasherImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SDEKClientImplTest.Config.class})
public class SDEKClientImplTest {
    private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Autowired
    private SDEKClient sdekClient;

    @Ignore
    @Test
    public void sendOrderTest() {
        Hasher hasher = new HasherImpl();
        String dateString = dateFormat.get().format(new Date());
        String secure = hasher.md5(dateString + "&" + "AqyBeJVsAA2gIqsaqCeancUYEOnkgcMm");

        // TODO builders/dsl
        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setNumber("1");
        deliveryRequest.setAccount("zGdkqju9H7fqXj9M1c6CWfQQvxJYaozL");
        deliveryRequest.setSecure(secure);
        deliveryRequest.setDate(dateString);
        deliveryRequest.setOrderCount(1);

        DeliveryOrder deliveryOrder1 = new DeliveryOrder();
        deliveryOrder1.setNumber("test-order"); // order.number
        deliveryOrder1.setSendCityCode(44); // TODO property
        //deliveryOrder1.setSellerName("Имиджбук-seller"); // TODO property
        deliveryOrder1.setRecCityCode(44); // bill.sdekCityId
        deliveryOrder1.setRecipientName("Получатель"); // TODO address.getFullName
        deliveryOrder1.setPhone("+79913770310"); // TODO property +7 495 765-76-74
        deliveryOrder1.setTariffTypeCode(137); // TODO from bill

        DeliveryAddress deliveryAddress1 = new DeliveryAddress();
        deliveryAddress1.setStreet("street-G4Akh0"); // TODO address.street
        deliveryAddress1.setHouse("house-G4Akh0"); // TODO address.home
        deliveryAddress1.setFlat("flat-soOEl0"); // TODO address.office
        deliveryOrder1.setAddress(deliveryAddress1);

        DeliveryPackage deliveryPackage1 = new DeliveryPackage("1");
        //deliveryPackage1.setComment("test_comment");
        deliveryPackage1.setSizeA(10); // TODO Габариты упаковки. Длина (в сантиметрах)
        deliveryPackage1.setSizeB(20); // TODO Габариты упаковки. Ширина (в сантиметрах)
        deliveryPackage1.setSizeC(30); // TODO Габариты упаковки. Высота (в сантиметрах)
        deliveryPackage1.setWeight(1000); // TODO Общий вес (в граммах)
        deliveryOrder1.addDeliveryPackage(deliveryPackage1);

        PackageItem packageItem1 = new PackageItem();
        packageItem1.setWareKey("test-article-1"); // TODO Идентификатор/артикул товара/вложения (Уникален в пределах упаковки Package).
        packageItem1.setAmount(1); // TODO Количество единиц одноименного товара (в штуках). Максимальное количество - 999.
        packageItem1.setComment("test_comment"); // TODO product.getName / article ?
        packageItem1.setCost(8f); // TODO Объявленная стоимость товара (за единицу товара в указанной валюте, значение >=0). С данного значения рассчитывается страховка.
        packageItem1.setWeight(1000); // TODO Вес (за единицу товара, в граммах)
        deliveryPackage1.addItem(packageItem1);

        DeliveryPackage deliveryPackage11 = new DeliveryPackage("2");
        //deliveryPackage1.setComment("test_comment");
        deliveryPackage11.setSizeA(10); // TODO Габариты упаковки. Длина (в сантиметрах)
        deliveryPackage11.setSizeB(20); // TODO Габариты упаковки. Ширина (в сантиметрах)
        deliveryPackage11.setSizeC(30); // TODO Габариты упаковки. Высота (в сантиметрах)
        deliveryPackage11.setWeight(1000); // TODO Общий вес (в граммах)
        deliveryOrder1.addDeliveryPackage(deliveryPackage11);

        PackageItem packageItem11 = new PackageItem();
        packageItem11.setWareKey("test-article-1"); // TODO Идентификатор/артикул товара/вложения (Уникален в пределах упаковки Package).
        packageItem11.setAmount(1); // TODO Количество единиц одноименного товара (в штуках). Максимальное количество - 999.
        packageItem11.setComment("test_comment"); // TODO product.getName / article ?
        packageItem11.setCost(8f); // TODO Объявленная стоимость товара (за единицу товара в указанной валюте, значение >=0). С данного значения рассчитывается страховка.
        packageItem11.setWeight(1000); // TODO Вес (за единицу товара, в граммах)
        deliveryPackage11.addItem(packageItem11);

        deliveryRequest.addOrder(deliveryOrder1);

        DeliveryOrder deliveryOrder2 = new DeliveryOrder();
        deliveryOrder2.setNumber(RandomStringUtils.randomAlphabetic(10));
        deliveryOrder2.setSendCityCode(44);
        //deliveryOrder2.setSellerName("Имиджбук");
        deliveryOrder2.setRecCityCode(44);
        deliveryOrder2.setRecipientName("Получатель");
        deliveryOrder2.setPhone("+79913770310");
        deliveryOrder2.setTariffTypeCode(136);

        DeliveryAddress deliveryAddress2 = new DeliveryAddress();
//        deliveryAddress2.setStreet("street-G4Akh0");
//        deliveryAddress2.setHouse("house-G4Akh0");
//        deliveryAddress2.setFlat("flat-soOEl0");
        deliveryAddress2.setPvzCode("MSK150");
        deliveryOrder2.setAddress(deliveryAddress2);

        DeliveryPackage deliveryPackage2 = new DeliveryPackage("1");
        deliveryPackage2.setComment("test_comment");
        deliveryPackage2.setSizeA(10);
        deliveryPackage2.setSizeB(20);
        deliveryPackage2.setSizeC(30);
        deliveryPackage2.setWeight(1000);
        deliveryOrder2.addDeliveryPackage(deliveryPackage2);

        PackageItem packageItem2 = new PackageItem();
        packageItem2.setWareKey("test-article-2");
        packageItem2.setAmount(1);
        packageItem2.setComment("test_comment");
        packageItem2.setCost(8f);
        packageItem2.setWeight(1000);
        deliveryPackage2.addItem(packageItem2);

        DeliveryPackage deliveryPackage22 = new DeliveryPackage("2");
        deliveryPackage22.setComment("test_comment");
        deliveryPackage22.setSizeA(10);
        deliveryPackage22.setSizeB(20);
        deliveryPackage22.setSizeC(30);
        deliveryPackage22.setWeight(1000);
        deliveryOrder2.addDeliveryPackage(deliveryPackage22);

        PackageItem packageItem22 = new PackageItem();
        packageItem22.setWareKey("test-article-2");
        packageItem22.setAmount(1);
        packageItem22.setComment("test_comment");
        packageItem22.setCost(8f);
        packageItem22.setWeight(1000);
        deliveryPackage22.addItem(packageItem22);


        //deliveryRequest.addOrder(deliveryOrder2);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DeliveryRequest.class);
            Marshaller m = jaxbContext.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter sw = new StringWriter();
            m.marshal(deliveryRequest, sw);
            System.out.println(sw.toString());
        } catch (JAXBException ex) {
            System.out.println(ex.getMessage());
        }

        DeliveryResponse response = sdekClient.sendRequestIM(deliveryRequest);

        for (ResponseOrder respOrder : response.getOrders()) {
            if (respOrder.getErrCode() == null && respOrder.getDispatchNumber() != null) {
                System.out.println("Order with number = " + respOrder.getNumber() + " successfully sent to SDEK, "
                    + "dispatchNumber = " + respOrder.getDispatchNumber());
                // TODO update bill
            } else if (respOrder.getErrCode() != null) {

                System.out.println("Order with number = " + respOrder.getNumber() + " was not delivered to SDEK ["
                    + respOrder.getErrCode() + ", " + respOrder.getMsg()  +"]");
                // TODO update bill with err code
            } else {
                System.out.println(respOrder.getMsg());
            }
        }
    }

    @Configuration
    @ImportResource("file:src/main/webapp/WEB-INF/spring/config.xml")
    protected static class Config {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public SDEKClient sdekClient() {
            return new SDEKClientImpl();
        }
    }
}
