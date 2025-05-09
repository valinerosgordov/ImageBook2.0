package ru.imagebook.server.service2.app.delivery.sdek;

import static java.lang.String.format;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.server.service2.app.delivery.DeliveryConfig;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryAddress;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryOrder;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryPackage;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRecipientCostAdv;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.PackageItem;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.CalculatePriceRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.GoodDto;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.TariffDto;
import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.minogin.core.client.CoreFactory;

@Component
public class SDEKHelperImpl implements SDEKHelper {
    private static final int DELIVERY_PACKAGE_MAX_AMOUNT = 23;
    private static final ThreadLocal<DateFormat> REQUEST_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Autowired
    private CoreFactory coreFactory;

    @Autowired
    private DeliveryConfig deliveryConfig;

    @Autowired
    private DeliveryAddressHelper deliveryAddressHelper;

    @Override
    public DeliveryRequest createDeliveryRequest(int billsCount) {
        String dateString = REQUEST_DATE_FORMAT.get().format(new Date());
        String secure = coreFactory.getHasher()
                .md5(dateString + "&" + deliveryConfig.getSdekSecurePassword());

        DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setNumber("1");
        deliveryRequest.setAccount(deliveryConfig.getSdekAccount());
        deliveryRequest.setSecure(secure);
        deliveryRequest.setDate(dateString);
        deliveryRequest.setOrderCount(billsCount);
        return deliveryRequest;
    }

    @Override
    public DeliveryOrder createDeliveryOrder(Bill bill, SDEKDeliveryType deliveryType) {
        Address address = deliveryAddressHelper.getAddress(bill);
        if (address == null) {
            throw new RuntimeException(format("Couldn't find address for bill.id=%s", bill.getId()));
        }

        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setNumber(String.valueOf(bill.getId()));
        deliveryOrder.setSendCityCode(deliveryConfig.getSdekSendCityCode());
        //TODO move seller to separate class
        //TODO add tests for SDEK Integration
        deliveryOrder.setSellerName(deliveryConfig.getSdekSellerName());
        deliveryOrder.setRecCityCode(bill.getSdekCityId());
        deliveryOrder.setRecipientName(address.getFullName());
        deliveryOrder.setPhone(address.getPhone());
        deliveryOrder.setAddress(createDeliveryAddress(bill, deliveryType, address));
        deliveryOrder.setDeliveryPackages(computePackages(bill));
        deliveryOrder.setDeliveryRecipientCostAdv(new DeliveryRecipientCostAdv());

        return deliveryOrder;
    }

    private DeliveryAddress createDeliveryAddress(Bill bill, SDEKDeliveryType deliveryType, Address address) {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        if (deliveryType == SDEKDeliveryType.COURIER) {
            deliveryAddress.setStreet(address.getStreet());
            deliveryAddress.setHouse(address.getHome());
            deliveryAddress.setFlat(address.getOffice());
        } else {
            deliveryAddress.setPvzCode(bill.getSdekPickupPointId());
        }
        return deliveryAddress;
    }

    @Override
    public CalculatePriceRequest createCalculateTariffRequest(Bill bill, SDEKDeliveryType deliveryType, DeliveryOrder deliveryOrder) {
        CalculatePriceRequest calculatePriceRequest = new CalculatePriceRequest();
        calculatePriceRequest.setSenderCityId(deliveryConfig.getSdekSendCityCode());
        calculatePriceRequest.setReceiverCityId(bill.getSdekCityId());

        int[] tariffIdList;
        switch (deliveryType) {
            case COURIER:
                tariffIdList = new int[]{137, 122};
                break;
            case PICKUP:
                tariffIdList = new int[]{136, 62};
                break;
            default:
                tariffIdList = new int[0];
        }
        calculatePriceRequest.setTariffList(createTariffDtoList(tariffIdList));

        calculatePriceRequest.setAuthLogin(deliveryConfig.getSdekAccount());
        calculatePriceRequest.setSecure(deliveryConfig.getSdekSecurePassword());

        calculatePriceRequest.setGoods(
                deliveryOrder.getDeliveryPackages().stream()
                        .map(deliveryPackage -> new GoodDto(
                                deliveryPackage.getWeight(),
                                deliveryPackage.getSizeA(),
                                deliveryPackage.getSizeB(),
                                deliveryPackage.getSizeC()
                        )).collect(Collectors.toList())
        );
        return calculatePriceRequest;
    }

    private List<TariffDto> createTariffDtoList(int[] tariffIdList) {
        List<TariffDto> tariffDtoList = new ArrayList<>();

        for (int i = 0; i < tariffIdList.length; i++) {
            TariffDto tariffDto = new TariffDto(
                    tariffIdList[i],
                    i + 1
            );
            tariffDtoList.add(tariffDto);
        }

        return tariffDtoList;
    }

    @Override
    public List<DeliveryPackage> computePackages(Bill bill) {
        List<DeliveryPackage> packages = new ArrayList<>();

        int packageNumber = 1;
        int totalCost = 0;
        int totalQuantity = 0;
        int height = 6;
        int width;
        int length;

        DeliveryPackage deliveryPackage = createPackage(packageNumber);
        packages.add(deliveryPackage);

        int packageAmount = 0;
        for (Order<?> order : bill.getOrders()) {
            totalCost += order.getPrice();
            totalQuantity += order.getQuantity();

            for (int i = 0; i < order.getQuantity(); i++) {
                if (packageAmount >= DELIVERY_PACKAGE_MAX_AMOUNT) {
                    deliveryPackage = createPackage(++packageNumber);
                    packages.add(deliveryPackage);
                    packageAmount = 0;
                }

                deliveryPackage.setWeight(deliveryPackage.getWeight() + order.getItemWeight());

                width = Math.min(order.getProduct().getWidth(), order.getProduct().getHeight());
                length = Math.max(order.getProduct().getWidth(), order.getProduct().getHeight());
                deliveryPackage.setSizeA(Math.max(deliveryPackage.getSizeA(), length / 10));
                deliveryPackage.setSizeB(Math.max(deliveryPackage.getSizeB(), width / 10));
                deliveryPackage.setSizeC(deliveryPackage.getSizeC() + height);
                packageAmount++;
            }
        }

        int averageCost = totalCost / totalQuantity;
        int averageWeight = bill.getWeight() / totalQuantity;

        for (DeliveryPackage dPackage : packages) {
            PackageItem packageItem = createPackageItem(averageCost, averageWeight);
            dPackage.addItem(packageItem);
        }

        return packages;
    }

    private DeliveryPackage createPackage(int number) {
        DeliveryPackage deliveryPackage = new DeliveryPackage(String.valueOf(number));
        deliveryPackage.setSizeA(0); // TODO move to property
        deliveryPackage.setSizeB(0);
        deliveryPackage.setSizeC(0);
        return deliveryPackage;
    }

    private PackageItem createPackageItem(float cost, int weight) {
        PackageItem packageItem = new PackageItem();
        packageItem.setWareKey("1");
        packageItem.setAmount(1);
        packageItem.setComment("книги");
        packageItem.setCost(cost);
        packageItem.setWeight(weight);
        return packageItem;
    }
}