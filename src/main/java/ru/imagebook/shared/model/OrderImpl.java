package ru.imagebook.shared.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ru.imagebook.shared.model.editor.Layout;
import ru.minogin.core.client.bean.BaseObservableEntityBean;

public abstract class OrderImpl<P extends Product> extends BaseObservableEntityBean implements Order<P> {
    private Integer discountSum;
    private Integer vendorCost;

    protected OrderImpl() {
        setQuantity(0);
        setPrice(0);
        setCost(0);
        setPhPrice(0);
        setPhCost(0);

        setDate(new Date());
        setState(OrderState.NEW);
        setTrial(false);
        setInRequestBasket(false);
        setPublishFlash(false);
        setType(OrderType.MANUAL);
        setWebFlash(false);
        setStorageState(StorageState.STORAGE);
        setDiscountPc(0);
        setUrgent(false);
        setModifiedDate(new Date());
        setLayouts(new ArrayList<Layout>());

        Flyleaf defaultFlyleaf = new Flyleaf();
        defaultFlyleaf.setId(Flyleaf.DEFAULT_ID);
        setFlyleaf(defaultFlyleaf);
        setFlyleafPrice(0);

        setVellumPrice(0);
    }

    @Override
    public void addLayout(Layout layout) {
        getLayouts().add(layout);
    }

    public OrderImpl(P product) {
        this();

        setProduct(product);
    }

    @Override
    public String getNumber() {
        return get(NUMBER);
    }

    @Override
    public void setNumber(String number) {
        set(NUMBER, number);
    }

    @Override
    public User getUser() {
        return get(USER);
    }

    @Override
    public void setUser(User user) {
        set(USER, user);
    }

    @Override
    public Date getDate() {
        return get(DATE);
    }

    @Override
    public void setDate(Date date) {
        set(DATE, date);
    }

    @SuppressWarnings("unchecked")
    @Override
    public P getProduct() {
        return (P) get(PRODUCT);
    }

    @Override
    public void setProduct(P product) {
        set(PRODUCT, product);
    }

    @Override
    public Integer getState() {
        return get(STATE);
    }

    @Override
    public void setState(Integer state) {
        set(STATE, state);
    }

    @Override
    public int getQuantity() {
        return get(QUANTITY);
    }

    @Override
    public void setQuantity(int quantity) {
        set(QUANTITY, quantity);
    }

    @Override
    public String getFlash() {
        return get(FLASH);
    }

    @Override
    public void setFlash(String flash) {
        set(FLASH, flash);
    }

    @Override
    public int getPrice() {
        return get(PRICE);
    }

    @Override
    public void setPrice(int price) {
        set(PRICE, price);
    }

    @Override
    public int getCost() {
        return get(COST);
    }

    @Override
    public void setCost(int cost) {
        set(COST, cost);
    }

    @Override
    public int getDiscount() {
        return getPrice() * getQuantity() - getCost();
    }

    @Override
    public void setRejectComment(String rejectComment) {
        set(REJECT_COMMENT, rejectComment);
    }

    @Override
    public String getRejectComment() {
        return get(REJECT_COMMENT);
    }

    @Override
    public Integer getPageCount() {
        return get(PAGE_COUNT);
    }

    @Override
    public void setPageCount(Integer pageCount) {
        set(PAGE_COUNT, pageCount);
    }

    @Override
    public Color getColor() {
        return get(COLOR);
    }

    @Override
    public void setColor(Color color) {
        set(COLOR, color);
    }

    @Override
    public Integer getCoverLamination() {
        return get(COVER_LAMINATION);
    }

    @Override
    public void setCoverLamination(Integer coverLamination) {
        set(COVER_LAMINATION, coverLamination);
    }

    @Override
    public Integer getPageLamination() {
        return get(PAGE_LAMINATION);
    }

    @Override
    public void setPageLamination(Integer pageLamination) {
        set(PAGE_LAMINATION, pageLamination);
    }

    @Override
    public Address getAddress() {
        return get(ADDRESS);
    }

    @Override
    public void setAddress(Address address) {
        set(ADDRESS, address);
    }

    @Override
    public boolean isTrial() {
        return (Boolean) get(TRIAL);
    }

    @Override
    public void setTrial(boolean trial) {
        set(TRIAL, trial);
    }

    @Override
    public int getPhPrice() {
        return (Integer) get(PH_PRICE);
    }

    @Override
    public void setPhPrice(int phPrice) {
        set(PH_PRICE, phPrice);
    }

    @Override
    public int getPhCost() {
        return (Integer) get(PH_COST);
    }

    @Override
    public void setPhCost(int phCost) {
        set(PH_COST, phCost);
    }

    @Override
    public Integer getItemWeight() {
        return get(ITEM_WEIGHT);
    }

    @Override
    public void setItemWeight(Integer itemWeight) {
        set(ITEM_WEIGHT, itemWeight);
    }

    @Override
    public Integer getTotalWeight() {
        return get(TOTAL_WEIGHT);
    }

    @Override
    public void setTotalWeight(Integer totalWeight) {
        set(TOTAL_WEIGHT, totalWeight);
    }

    @Override
    public boolean isInRequestBasket() {
        return (Boolean) get(IN_REQUEST_BASKET);
    }

    @Override
    public void setInRequestBasket(boolean inRequestBasket) {
        set(IN_REQUEST_BASKET, inRequestBasket);
    }

    @Override
    public Request getRequest() {
        return get(REQUEST);
    }

    @Override
    public void setRequest(Request request) {
        set(REQUEST, request);
    }

    @Override
    public BonusCode getBonusCode() {
        return get(BONUS_CODE);
    }

    @Override
    public void setBonusCode(BonusCode bonusCode) {
        set(BONUS_CODE, bonusCode);
    }

    @Override
    public String getBonusCodeText() {
        return get(BONUS_CODE_TEXT);
    }

    @Override
    public void setBonusCodeText(String bonusCodeText) {
        set(BONUS_CODE_TEXT, bonusCodeText);
    }

    @Override
    public String getDeactivationCode() {
        return get(DEACTIVATION_CODE);
    }

    @Override
    public void setDeactivationCode(String deactivationCode) {
        set(DEACTIVATION_CODE, deactivationCode);
    }

    @Override
    public String getCouponId() {
        return get(COUPON_ID);
    }

    @Override
    public void setCouponId(String couponId) {
        set(COUPON_ID, couponId);
    }

    @Override
    public Bill getBill() {
        return get(BILL);
    }

    @Override
    public void setBill(Bill bill) {
        set(BILL, bill);
    }

    @Override
    public Integer getLevel() {
        return get(LEVEL);
    }

    @Override
    public void setLevel(Integer level) {
        set(LEVEL, level);
    }

    @Override
    public Integer getDeliveryType() {
        return get(DELIVERY_TYPE);
    }

    @Override
    public void setDeliveryType(Integer deliveryType) {
        set(DELIVERY_TYPE, deliveryType);
    }

    @Override
    public String getDeliveryCode() {
        return get(DELIVERY_CODE);
    }

    @Override
    public void setDeliveryCode(String deliveryCode) {
        set(DELIVERY_CODE, deliveryCode);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Order<?>))
            return false;

        Order<?> order = (Order<?>) obj;
        if (getId() == null || order.getId() == null)
            return false;

        return getId().equals(order.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

    @Override
    public int compareTo(Order<?> order) {
        if (getDate().equals(order.getDate())) {
            if (getId() != null && order.getId() != null)
                return getId().compareTo(order.getId());
            else
                return 0;
        } else
            return getDate().compareTo(order.getDate());
    }

    @Override
    public void attachAddress(Address address) {
        Address clone = new Address(address);
        clone.remove(Address.ID);
        setAddress(clone);
    }

    @Override
    public String getComment() {
        return get(COMMENT);
    }

    @Override
    public void setComment(String comment) {
        set(COMMENT, comment);
    }

    @Override
    public boolean isPublishFlash() {
        return (Boolean) get(PUBLISH_FLASH);
    }

    @Override
    public void setPublishFlash(boolean publishFlash) {
        set(PUBLISH_FLASH, publishFlash);
    }

    @Override
    public Layout getLayout() {
        return get(LAYOUT);
    }

    @Override
    public void setLayout(Layout layout) {
        set(LAYOUT, layout);
    }

    @Override
    public int getType() {
        return (Integer) get(TYPE);
    }

    @Override
    public void setType(int type) {
        set(TYPE, type);
    }

    @Override
    public Date getPrintDate() {
        return get(PRINT_DATE);
    }

    @Override
    public void setPrintDate(Date printDate) {
        set(PRINT_DATE, printDate);
    }

    @Override
    public boolean isWebFlash() {
        return (Boolean) get(WEB_FLASH);
    }

    @Override
    public void setWebFlash(boolean webFlash) {
        set(WEB_FLASH, webFlash);
    }

    @Override
    public int getStorageState() {
        return (Integer) get(STORAGE_STATE);
    }

    @Override
    public void setStorageState(int storageState) {
        set(STORAGE_STATE, storageState);
    }

    @Override
    public Date getSentDate() {
        return get(SENT_DATE);
    }

    @Override
    public void setSentDate(Date sentDate) {
        set(SENT_DATE, sentDate);
    }

    @Override
    public void setSent() {
        setState(OrderState.SENT);
        setSentDate(new Date());
    }

    @Override
    public boolean isPaid() {
        int state = getState();
        return state == OrderState.PAID
                || state == OrderState.PDF_ERROR
                || state == OrderState.PDF_REGENERATION
                || state == OrderState.READY_TO_TRANSFER_PDF
                || state == OrderState.PDF_TRANSFER_IN_PROGRESS
                || state == OrderState.PRINTING
                || state == OrderState.FINISHING
                || state == OrderState.PRINTED
                || state == OrderState.DELIVERY
                || state == OrderState.SENT;
    }

    @Override
    public int getDiscountPc() {
        return (Integer) get(BONUS_DISCOUNT);
    }

    @Override
    public void setDiscountPc(int bonusDiscount) {
        set(BONUS_DISCOUNT, bonusDiscount);
    }

    @Override
    public Integer getDiscountPCenter() {
        return (Integer) get(DISCOUNT_PCENTER);
    }

    @Override
    public void setDiscountPCenter(Integer discount) {
        set(DISCOUNT_PCENTER, discount);
    }

    @Override
    public String getDeliveryComment() {
        return get(DELIVERY_COMMENT);
    }

    @Override
    public void setDeliveryComment(String deliveryComment) {
        set(DELIVERY_COMMENT, deliveryComment);
    }

    @Override
    public Integer getDiscountSum() {
        return discountSum;
    }

    @Override
    public void setDiscountSum(Integer discountSum) {
        this.discountSum = discountSum;
    }

    @Override
    public String getCode() {
        return get(CODE);
    }

    @Override
    public void setCode(String code) {
        set(CODE, code);
    }

    @Override
    public boolean isUrgent() {
        return (Boolean) get(URGENT);
    }

    @Override
    public void setUrgent(boolean urgent) {
        set(URGENT, urgent);
    }

    @Override
    public Integer getSpecialPrice() {
        return get(SPECIAL_PRICE);
    }

    @Override
    public void setSpecialPrice(Integer specialPrice) {
        set(SPECIAL_PRICE, specialPrice);
    }

    @Override
    public Integer getImportId() {
        return (Integer) get(IMPORT_ID);
    }

    @Override
    public void setImportId(Integer importId) {
        set(IMPORT_ID, importId);
    }

    @Override
    public void setVendorCost(Integer vendorCost) {
        this.vendorCost = vendorCost;
    }

    @Override
    public Integer getVendorCost() {
        return this.vendorCost;
    }

    @Override
    public Date getPayDate() {
        return get(PAY_DATE);
    }

    @Override
    public void setPayDate(Date payDate) {
        set(PAY_DATE, payDate);
    }

    @Override
    public boolean isEditorOrder() {
        return getType() == OrderType.EDITOR;
    }

    @Override
    public boolean isExternalOrder() {
        return getType() == OrderType.EXTERNAL && getImportId() != null;
    }

    @Override
    public boolean isOnlineEditorOrder() {
        return getType() == OrderType.BOOK && getAlbumId() != null;
    }

    @Override
    public String getPackageNumber() {
        return get(PACKAGE_NUMBER);
    }

    @Override
    public void setPackageNumber(String packageNumber) {
        set(PACKAGE_NUMBER, packageNumber);
    }

    @Override
    public boolean isPackaged() {
        return getPackageNumber() != null;
    }

    @Override
    public Date getModifiedDate() {
        return get(MODIFIED_DATE);
    }

    @Override
    public void setModifiedDate(Date modifiedDate) {
        set(MODIFIED_DATE, modifiedDate);
    }

    @Override
    public List<Layout> getLayouts() {
        return get(LAYOUTS);
    }

    @Override
    public void setLayouts(final List<Layout> layouts) {
        set(LAYOUTS, layouts);
    }

    @Override
    public void sortLayouts() {
        if (this.getLayouts() != null && !this.getLayouts().isEmpty()) {
            Collections.sort(getLayouts(), new Comparator<Layout>() {
                @Override
                public int compare(Layout o1, Layout o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
        }
    }

    @Override
    public Integer getPublishCode() {
        return get(PUBLISH_CODE);
    }

    @Override
    public void setPublishCode(Integer publishCode) {
        set(PUBLISH_CODE, publishCode);
    }

    @Override
    public Flyleaf getFlyleaf() {
        return get(FLYLEAF);
    }

    @Override
    public void setFlyleaf(Flyleaf flyleaf) {
        set(FLYLEAF, flyleaf);
    }

    @Override
    public String getAlbumId() {
        return get(ALBUM_ID);
    }

    @Override
    public void setAlbumId(String albumId) {
        set(ALBUM_ID, albumId);
    }

    @Override
    public int getFlyleafPrice() {
        return get(FLYLEAF_PRICE);
    }

    @Override
    public void setFlyleafPrice(int flyleafPrice) {
        set(FLYLEAF_PRICE, flyleafPrice);
    }

    @Override
    public Vellum getVellum() {
        return get(VELLUM);
    }

    @Override
    public void setVellum(Vellum vellum) {
        set(VELLUM, vellum);
    }

    @Override
    public int getVellumPrice() {
        return get(VELLUM_PRICE);
    }

    @Override
    public void setVellumPrice(int vellumPrice) {
        set(VELLUM_PRICE, vellumPrice);
    }
}
