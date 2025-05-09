package ru.imagebook.shared.model;

import ru.imagebook.shared.model.editor.Layout;
import ru.minogin.core.client.bean.ObservableEntityBean;

import java.util.Date;
import java.util.List;

public interface Order<P extends Product> extends ObservableEntityBean, Comparable<Order<?>> {
    String NUMBER = "number";
    String USER = "user";
    String PRODUCT = "product";
    String STATE = "state";
    String DATE = "date";
    String QUANTITY = "quantity";
    String FLASH = "flash";
    String PRICE = "price";
    String COST = "cost";
    String PH_PRICE = "phPrice";
    String PH_COST = "phCost";
    String REJECT_COMMENT = "rejectComment";
    String PAGE_COUNT = "pageCount";
    String COLOR = "color";
    String COVER_LAMINATION = "coverLamination";
    String PAGE_LAMINATION = "pageLamination";
    String ADDRESS = "address";
    String TRIAL = "trial";
    String ITEM_WEIGHT = "itemWeight";
    String TOTAL_WEIGHT = "totalWeight";
    String IN_REQUEST_BASKET = "inRequestBasket";
    String REQUEST = "request";
    String BONUS_CODE = "bonusCode";
    String BONUS_CODE_TEXT = "bonusCodeText";
    String DEACTIVATION_CODE = "deactivationCode";
    String COUPON_ID = "couponId";
    String BILL = "bill";
    String DELIVERY_TYPE = "deliveryType";
    String DELIVERY_CODE = "deliveryCode";
    String LEVEL = "level";
    String COMMENT = "comment";
    String PUBLISH_FLASH = "publishFlash";
    String LAYOUT = "layout";
    String TYPE = "type";
    String PRINT_DATE = "printDate";
    String WEB_FLASH = "web_flash";
    String STORAGE_STATE = "storageState";
    String SENT_DATE = "sentDate";
    String BONUS_DISCOUNT = "bonusDiscount";
    String DELIVERY_COMMENT = "deliveryComment";
    String CODE = "code";
    String URGENT = "urgent";
    String SPECIAL_PRICE = "specialPrice";
    String IMPORT_ID = "importId";
    String PAY_DATE = "payDate";
    String DISCOUNT_PCENTER = "discountPCenter";
    String PACKAGE_NUMBER = "packageNumber";
    String MODIFIED_DATE = "modifiedDate";
    String LAYOUTS = "layouts";
    String PUBLISH_CODE = "publishCode";
    String FLYLEAF = "flyleaf";
    String FLYLEAF_PRICE = "flyleafPrice";
    String ALBUM_ID = "albumId";
    String VELLUM = "vellum";
    String VELLUM_PRICE = "vellumPrice";

    String getNumber();

    void setNumber(String number);

    User getUser();

    void setUser(User user);

    Date getDate();

    void setDate(Date date);

    P getProduct();

    void setProduct(P product);

    Integer getState();

    void setState(Integer state);

    int getQuantity();

    void setQuantity(int quantity);

    String getFlash();

    void setFlash(String flash);

    int getPrice();

    void setPrice(int price);

    int getCost();

    void setCost(int cost);

    int getPhPrice();

    void setPhPrice(int phPrice);

    int getPhCost();

    void setPhCost(int phCost);

    int getDiscount();

    void setRejectComment(String rejectComment);

    String getRejectComment();

    Integer getPageCount();

    void setPageCount(Integer pageCount);

    Color getColor();

    void setColor(Color color);

    Integer getCoverLamination();

    void setCoverLamination(Integer coverLamination);

    Integer getPageLamination();

    void setPageLamination(Integer pageLamination);

    Address getAddress();

    void setAddress(Address address);

    boolean isTrial();

    void setTrial(boolean trial);

    Integer getItemWeight();

    void setItemWeight(Integer itemWeight);

    Integer getTotalWeight();

    void setTotalWeight(Integer totalWeight);

    boolean isInRequestBasket();

    void setInRequestBasket(boolean inRequestBasket);

    Request getRequest();

    void setRequest(Request request);

    BonusCode getBonusCode();

    void setBonusCode(BonusCode bonusCode);

    String getBonusCodeText();

    void setBonusCodeText(String bonusCodeText);

    String getDeactivationCode();

    void setDeactivationCode(String deactivationCode);

    @Deprecated
    String getCouponId();

    @Deprecated
    void setCouponId(String couponId);

    Bill getBill();

    void setBill(Bill bill);

    Integer getLevel();

    void setLevel(Integer level);

    Integer getDeliveryType();

    void setDeliveryType(Integer deliveryType);

    void attachAddress(Address address);

    String getDeliveryCode();

    void setDeliveryCode(String deliveryCode);

    String getComment();

    void setComment(String comment);

    boolean isPublishFlash();

    void setPublishFlash(boolean publishFlash);

    Layout getLayout();

    void setLayout(Layout layout);

    int getType();

    void setType(int type);

    Date getPrintDate();

    void setPrintDate(Date printDate);

    boolean isWebFlash();

    void setWebFlash(boolean webFlash);

    int getStorageState();

    void setStorageState(int storageState);

    Date getSentDate();

    void setSentDate(Date sentDate);

    public void setSent();

    boolean isPaid();

    int getDiscountPc();

    void setDiscountPc(int bonusDiscount);

    String getDeliveryComment();

    void setDeliveryComment(String deliveryComment);

    Integer getDiscountSum();

    void setDiscountSum(Integer discountSum);

    String getCode();

    void setCode(String code);

    boolean isUrgent();

    void setUrgent(boolean urgent);

    Integer getSpecialPrice();

    void setSpecialPrice(Integer specialPrice);

    Integer getImportId();

    void setImportId(Integer importId);

    void setVendorCost(Integer vendorCost);

    Integer getVendorCost();

    Date getPayDate();

    void setPayDate(Date payDate);

    boolean isEditorOrder();

    boolean isExternalOrder();

    boolean isOnlineEditorOrder();

    Integer getDiscountPCenter();

    void setDiscountPCenter(Integer discountPcenter);

    String getPackageNumber();

    void setPackageNumber(String packageNumber);

    boolean isPackaged();

    Date getModifiedDate();

    void setModifiedDate(Date modifiedDate);

    List<Layout> getLayouts();

    void setLayouts(List<Layout> layouts);

    void addLayout(Layout layout);

    void sortLayouts();

    Integer getPublishCode();

    void setPublishCode(Integer code);

    Flyleaf getFlyleaf();

    void setFlyleaf(Flyleaf flyleaf);

    String getAlbumId();

    void setAlbumId(String albumId);

    int getFlyleafPrice();

    void setFlyleafPrice(int flyleafPrice);

    Vellum getVellum();

    void setVellum(Vellum vellum);

    int getVellumPrice();

    void setVellumPrice(int vellumPrice);
}