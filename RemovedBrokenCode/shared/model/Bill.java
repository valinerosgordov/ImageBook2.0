package ru.imagebook.shared.model;

import ru.imagebook.client.common.service.delivery.MultishipType;
import ru.minogin.core.client.bean.BaseEntityBean;

import java.beans.Transient;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Bill extends BaseEntityBean {
    private static final long serialVersionUID = -5843255776698310467L;

    public static final String USER = "user";
    public static final String DATE = "date";
    public static final String ORDERS = "orders";
    public static final String TOTAL = "total";
    public static final String PH_TOTAL = "ph_total";
    public static final String STATE = "state";
    public static final String ADV = "adv";
    public static final String WEIGHT = "weight";
    public static final String DELIVERY_TYPE = "deliveryType";
    public static final String DELIVERY_COST = "deliveryCost";
    public static final String REG_LETTER = "regLetter";    // [regletter]
    public static final String IMPORT_ID = "importId";
    public static final String PAY_DATE = "payDate";
    public static final String DISCOUNT_PC = "discountPc";
    public static final String MULTISHIP_ORDER_ID = "multishipOrderId";
    public static final String MULTISHIP_TYPE = "multishipType";
    public static final String MULTISHIP_DELIVERY_SERVICE = "mshDeliveryService";
    public static final String ORIENT_DELIVERY_DATE = "orientDeliveryDate";
    public static final String DELIVERY_TIME = "deliveryTime";
    public static final String DS_SEND_STATE = "dsSendState";
    public static final String DS_ERROR_MESSAGE = "dsErrorMessage";
    public static final String SENDING_ID = "sendingId";
    public static final String DS_SENDING_ID = "dsSendingId";
    public static final String PICKPOINT_POSTAMATE_ID = "pickpointPostamateID";
    public static final String PICKPOINT_RATE_ZONE = "pickpointRateZone";
    public static final String PICKPOINT_TRUNK_COEFF = "pickpointTrunkCoeff";
    public static final String PICKPOINT_ADDRESS = "pickpointAddress";
    public static final String TRIAL = "trial";
    public static final String DDELIVERY_TYPE = "ddeliveryType";
    public static final String DDELIVERY_CITY_ID_ = "ddeliveryCityId";
    public static final String DDELIVERY_COMPANY_ID = "ddeliveryCompanyId";
    public static final String DDELIVERY_COMPANY_NAME = "ddeliveryCompanyName";
    public static final String DDELIVERY_PICKUP_POINT_ID = "ddeliveryPickupPointId";
    public static final String DDELIVERY_PICKUP_POINT_ADDRESS = "ddeliveryPickupPointAddress";
    public static final String PICKUP_SEND_STATE_DATE = "pickupSendStateDate";
    public static final String NOTIFY_PICKUP = "notifyPickup";
    public static final String DELIVERY_DISCOUNT_PC = "deliveryDiscountPc";
    public static final String SDEK_DELIVERY_TYPE = "sdekDeliveryType";
    public static final String SDEP_PICKUP_POINT_ID = "sdekPickupPointId";
    public static final String SDEK_CITY_ID = "sdekCityId";
    public static final String SDEK_PICKUP_POINT_ADDRESS = "sdekPickupPointAddress";
    public static final String SDEK_TARIF_CODE = "sdekTarifCode";

    private int mailRuDiscountPc;
    private String deliveryComment;

    public Bill() {
    }

    public Bill(User user, Date date) {
        setUser(user);
        setDate(date);
        setOrders(new TreeSet<Order<?>>());
        setState(BillState.NEW);
        setAdv(false);
        setRegLetter(false);
        setDiscountPc(0);
        setDeliveryCost(0);
        setDsSendState(DsSendState.NOT_SENT);
        setTotal(0);
        setNotifyPickup(false);
    }

    public User getUser() {
        return get(USER);
    }

    public void setUser(User user) {
        set(USER, user);
    }

    public Date getDate() {
        return get(DATE);
    }

    public void setDate(Date date) {
        set(DATE, date);
    }

    public Set<Order<?>> getOrders() {
        return get(ORDERS);
    }

    public void setOrders(Set<Order<?>> orders) {
        set(ORDERS, orders);
    }

    public void addOrder(Order<?> order) {
        getOrders().add(order);
        order.setBill(this);
    }

    public int getTotal() {
        return (Integer) get(TOTAL);
    }

    public void setTotal(int total) {
        set(TOTAL, total);
    }

    public int getPhTotal() {
        return (Integer) get(PH_TOTAL);
    }

    public void setPhTotal(int phTotal) {
        set(PH_TOTAL, phTotal);
    }

    public void attachAddressToOrders(Address address) {
        for (Order<?> order : getOrders()) {
            order.attachAddress(address);
            order.setDeliveryType(getDeliveryType());
            order.setDeliveryComment(getDeliveryComment());
        }
    }

    public Integer getState() {
        return get(STATE);
    }

    public void setState(Integer state) {
        set(STATE, state);
    }

    public boolean isAdv() {
        return (Boolean) get(ADV);
    }

    public void setAdv(boolean adv) {
        set(ADV, adv);
    }

    public Integer getWeight() {
        return get(WEIGHT);
    }

    public void setWeight(Integer weight) {
        set(WEIGHT, weight);
    }

    public Integer getDeliveryType() {
        return get(DELIVERY_TYPE);
    }

    public void setDeliveryType(Integer deliveryType) {
        set(DELIVERY_TYPE, deliveryType);
    }

    public int getDeliveryCost() {
        return get(DELIVERY_COST);
    }

    public void setDeliveryCost(int deliveryCost) {
        set(DELIVERY_COST, deliveryCost);
    }

    @Deprecated
    public boolean isRegLetter() {
        return (Boolean) get(REG_LETTER);
    }

    @Deprecated
    public void setRegLetter(boolean regLetter) {
        set(REG_LETTER, regLetter);
    }

    public Integer getImportId() {
        return (Integer) get(IMPORT_ID);
    }

    public void setImportId(Integer importId) {
        set(IMPORT_ID, importId);
    }

    public Date getPayDate() {
        return get(PAY_DATE);
    }

    public void setPayDate(Date payDate) {
        set(PAY_DATE, payDate);
    }

    @Deprecated
    public int getDiscountPc() {
        return (Integer) get(DISCOUNT_PC);
    }

    @Deprecated
    public void setDiscountPc(int discountPc) {
        set(DISCOUNT_PC, discountPc);
    }

    @Deprecated
    @Transient
    public int getMailRuDiscountPc() {
        return mailRuDiscountPc;
    }

    @Deprecated
    public void setMailRuDiscountPc(int mailRuDiscountPc) {
        this.mailRuDiscountPc = mailRuDiscountPc;
    }

    @Deprecated
    public Integer getMultishipOrderId() {
        return get(MULTISHIP_ORDER_ID);
    }

    @Deprecated
    public void setMultishipOrderId(Integer multishipOrderId) {
        set(MULTISHIP_ORDER_ID, multishipOrderId);
    }

    public String getMultishipType() {
        return get(MULTISHIP_TYPE);
    }

    @Deprecated
    public void setMultishipType(MultishipType multishipType) {
        set(MULTISHIP_TYPE, multishipType != null ? multishipType.name() : null);
    }

    @Deprecated
    public void setMultishipType(String multishipType) {
        set(MULTISHIP_TYPE, multishipType);
    }

    @Deprecated
    public String getMshDeliveryService() {
        return get(MULTISHIP_DELIVERY_SERVICE);
    }

    @Deprecated
    public String setMshDeliveryService(String deliveryService) {
        return set(MULTISHIP_DELIVERY_SERVICE, deliveryService);
    }

    @Deprecated
    public Date getOrientDeliveryDate() {
        return get(ORIENT_DELIVERY_DATE);
    }

    @Deprecated
    public Date setOrientDeliveryDate(Date orientDeliveryDate) {
        return set(ORIENT_DELIVERY_DATE, orientDeliveryDate);
    }

    /**
     * @return Срок доставки
     */
    public Integer getDeliveryTime() {
        return get(DELIVERY_TIME);
    }

    public void setDeliveryTime(Integer deliveryTime) {
        set(DELIVERY_TIME, deliveryTime);
    }

    @Transient
    public String getDeliveryComment() {
        return this.deliveryComment;
    }

    public void setDeliveryComment(String deliveryComment) {
        this.deliveryComment = deliveryComment;
    }

    @Transient
    public boolean isPickupDelivery() {
        return (getDeliveryType() != null && getDeliveryType() == DeliveryType.EXW) ||
                (getMultishipType() != null && MultishipType.valueOf(getMultishipType()) == MultishipType.MULTISHIP_PICKUP);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Bill))
            return false;

        Bill bill = (Bill) obj;
        if (getId() == null || bill.getId() == null)
            return false;

        return getId().equals(bill.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

    public Integer getDsSendState() {
        return get(DS_SEND_STATE);
    }

    public void setDsSendState(Integer state) {
        set(DS_SEND_STATE, state);
    }

    public String getDsErrorMessage() {
        return get(DS_ERROR_MESSAGE);
    }

    public void setDsErrorMessage(String dsErrorMessage) {
        set(DS_ERROR_MESSAGE, dsErrorMessage);
    }

    public String getSendingId() {
        return get(SENDING_ID);
    }

    public void setSendingId(String sendingId) {
        set(SENDING_ID, sendingId);
    }

    public String getDsSendingId() {
        return get(DS_SENDING_ID);
    }

    public void setDsSendingId(String dsSendingId) {
        set(DS_SENDING_ID, dsSendingId);
    }

    public void setPickpointPostamateID(String pickpointPostamateID) {
        set(PICKPOINT_POSTAMATE_ID, pickpointPostamateID);
    }

    public String getPickpointPostamateID() {
        return get(PICKPOINT_POSTAMATE_ID);
    }

    public void setPickpointRateZone(String pickpointRateZone) {
        set(PICKPOINT_RATE_ZONE, pickpointRateZone);
    }

    public String getPickpointRateZone() {
        return get(PICKPOINT_RATE_ZONE);
    }

    public void setPickpointTrunkCoeff(Double pickpointTrunkCoeff) {
        set(PICKPOINT_TRUNK_COEFF, pickpointTrunkCoeff);
    }

    public Double getPickpointTrunkCoeff() {
        return get(PICKPOINT_TRUNK_COEFF);
    }

    public void setPickpointAddress(String pickpointAddress) {
        set(PICKPOINT_ADDRESS, pickpointAddress);
    }

    public String getPickpointAddress() {
        return get(PICKPOINT_ADDRESS);
    }

    @Transient
    public boolean isTrial() {
        Boolean trial = get(TRIAL);
        return trial != null && trial;
    }

    public void setTrial(boolean trial) {
        set(TRIAL, trial);
    }

    @Deprecated
    public String getDdeliveryType() {
        return get(DDELIVERY_TYPE);
    }

    @Deprecated
    public void setDdeliveryType(String ddeliveryType) {
        set(DDELIVERY_TYPE, ddeliveryType);
    }

    @Deprecated
    public Integer getDdeliveryCityId() {
        return get(DDELIVERY_CITY_ID_);
    }

    @Deprecated
    public void setDdeliveryCityId(Integer cityId) {
        set(DDELIVERY_CITY_ID_, cityId);
    }

    @Deprecated
    public Integer getDdeliveryCompanyId() {
        return get(DDELIVERY_COMPANY_ID);
    }

    @Deprecated
    public void setDdeliveryCompanyId(Integer companyId) {
        set(DDELIVERY_COMPANY_ID, companyId);
    }

    @Deprecated
    public String getDdeliveryCompanyName() {
        return get(DDELIVERY_COMPANY_NAME);
    }

    @Deprecated
    public void setDdeliveryCompanyName(String companyName) {
        set(DDELIVERY_COMPANY_NAME, companyName);
    }

    @Deprecated
    public Integer getDdeliveryPickupPointId() {
        return get(DDELIVERY_PICKUP_POINT_ID);
    }

    @Deprecated
    public void setDdeliveryPickupPointId(Integer pickupPointId) {
        set(DDELIVERY_PICKUP_POINT_ID, pickupPointId);
    }

    @Deprecated
    public String getDdeliveryPickupPointAddress() {
        return get (DDELIVERY_PICKUP_POINT_ADDRESS);
    }

    @Deprecated
    public void setDdeliveryPickupPointAddress(String address) {
        set(DDELIVERY_PICKUP_POINT_ADDRESS, address);
    }

    public Date getPickupSendStateDate() { return get(PICKUP_SEND_STATE_DATE); }

    public void setPickupSendStateDate(final Date date) { set(PICKUP_SEND_STATE_DATE, date); }

    public boolean isNotifyPickup() {
        return (Boolean) get(NOTIFY_PICKUP);
    }

    public void setNotifyPickup(boolean isNotifyPickup) {
        set(NOTIFY_PICKUP, isNotifyPickup);
    }

    public Integer getDeliveryDiscountPc() {
        return get(DELIVERY_DISCOUNT_PC);
    }

    public void setDeliveryDiscountPc(Integer discount) {
        set(DELIVERY_DISCOUNT_PC, discount);
    }

    public boolean isHasDeliveryDiscount() {
        return getDeliveryDiscountPc() != null && getDeliveryDiscountPc() > 0;
    }

    public String getSdekDeliveryType() {
        return get(SDEK_DELIVERY_TYPE);
    }

    public void setSdekDeliveryType(String deliveryType) {
        set(SDEK_DELIVERY_TYPE, deliveryType);
    }

    public String getSdekPickupPointId() {
        return get(SDEP_PICKUP_POINT_ID);
    }

    public void setSdekPickupPointId(String pickupPointId) {
        set(SDEP_PICKUP_POINT_ID, pickupPointId);
    }

    public Integer getSdekCityId() {
        return get(SDEK_CITY_ID);
    }

    public void setSdekCityId(Integer cityId) {
        set(SDEK_CITY_ID, cityId);
    }

    public String getSdekPickupPointAddress() {
        return get(SDEK_PICKUP_POINT_ADDRESS);
    }

    public void setSdekPickupPointAddress(String pickupPointAddress) {
        set(SDEK_PICKUP_POINT_ADDRESS, pickupPointAddress);
    }

    public void setSdekTarifCode(Integer tarifCode) {
        set(SDEK_TARIF_CODE, tarifCode);
    }

    public Integer getSdekTarifCode() {
        return get(SDEK_TARIF_CODE);
    }
}
