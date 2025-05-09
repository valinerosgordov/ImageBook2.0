package ru.imagebook.shared.model;

import java.util.List;
import java.util.Set;

import ru.minogin.core.client.bean.EntityBean;
import ru.minogin.core.client.i18n.MultiString;

public interface Product extends EntityBean, Comparable<Product> {
    String TYPE = "type";
    String NUMBER = "number";
    String NAME = "name";
    String AVAILABILITY = "availability";
    String BLOCK_FORMAT = "blockFormat";
    String BINDING = "binding";
    String COVER = "cover";
    String PAPER = "paper";
    String MULTIPLICITY = "multiplicity";
    String MIN_PAGE_COUNT = "minPageCount";
    String MAX_PAGE_COUNT = "maxPageCount";
    String MIN_QUANTITY = "minQuantity";
    String COLOR_RANGE = "colorRange";
    String COVER_LAM_RANGE = "coverLamRange";
    String PAGE_LAM_RANGE = "pageLamRange";
    String WIDTH = "width";
    String HEIGHT = "height";
    String JPEG_FOLDER = "jpegFolder";
    String BLOCK_WIDTH = "blockWidth";
    String BLOCK_HEIGHT = "blockHeight";
    String UPPER_SAFE_AREA = "upperSafeArea";
    String BOTTOM_SAFE_AREA = "bottomSafeArea";
    String INNER_SAFE_AREA = "innerSafeArea";
    String OUTER_SAFE_AREA = "outerSafeArea";
    String ADDRESS_PRINTED = "addressPrinted";
    String NON_EDITOR = "nonEditor";
    String TRIAL_ALBUM = "trialAlbum";
    String HAS_SPECIAL_OFFER = "hasSpecialOffer";
    String MIN_ALBUMS_COUNT_FOR_DISCOUNT = "minAlbumsCountForDiscount";
    String IMAGEBOOK_DISCOUNT = "imagebookDiscount";
    String PH_DISCOUNT = "phDiscount";
    String TRIAL_DELIVERY = "trialDelivery";
    String ACCESSED_USERS = "accessedUsers";
    String ARTICLE = "article";
    String ALBUM_DISCOUNTS = "albumDiscounts";
    String APPROX_PROD_TIME = "approxProdTime";
    String CALC_COMMENT = "calcComment";
    String LAST_SPREAD_BARCODE = "lastSpreadBarcode";
    String NON_CALC = "nonCalc";

    String TRIAL_BB = "09";
    String TRIAL_CC = "99";

    Integer getType();

    void setType(Integer type);

    Integer getNumber();

    void setNumber(Integer number);

    MultiString getName();

    void setName(MultiString name);

    Integer getAvailability();

    void setAvailability(Integer availability);

    String getBlockFormat();

    void setBlockFormat(String blockFormat);

    Integer getBinding();

    void setBinding(Integer binding);

    Integer getCover();

    void setCover(Integer cover);

    Integer getPaper();

    void setPaper(Integer paper);

    Integer getMultiplicity();

    void setMultiplicity(Integer multiplicity);

    Integer getMinPageCount();

    void setMinPageCount(Integer minPageCount);

    Integer getMaxPageCount();

    void setMaxPageCount(Integer maxPageCount);

    Integer getMinQuantity();

    void setMinQuantity(Integer minQuantity);

    List<Integer> getColorRange();

    void setColorRange(List<Integer> colorRange);

    List<Integer> getCoverLamRange();

    void setCoverLamRange(List<Integer> coverLamRange);

    List<Integer> getPageLamRange();

    void setPageLamRange(List<Integer> pageLamRange);

    boolean isTrialAlbum();

    void setTrialAlbum(boolean trialAlbum);

    Integer getWidth();

    void setWidth(Integer width);

    Integer getHeight();

    void setHeight(Integer height);

    String getJpegFolder();

    void setJpegFolder(String jpegFolder);

    Integer getBlockWidth();

    void setBlockWidth(Integer blockWidth);

    Integer getBlockHeight();

    void setBlockHeight(Integer blockHeight);

    Float getUpperSafeArea();

    void setUpperSafeArea(Float upperSafeArea);

    Float getBottomSafeArea();

    void setBottomSafeArea(Float bottomSafeArea);

    Float getInnerSafeArea();

    void setInnerSafeArea(Float innerSafeArea);

    Float getOuterSafeArea();

    void setOuterSafeArea(Float outerSafeArea);

    boolean isAddressPrinted();

    boolean isCongratulatory();

    void setAddressPrinted(boolean addressPrinted);

    boolean isNonEditor();

    void setNonEditor(boolean nonEditor);

    Integer getMinAlbumsCountForDiscount();

    void setMinAlbumsCountForDiscount(Integer minAlbumsCountForDiscount);

    Integer getImagebookDiscount();

    void setImagebookDiscount(Integer imagebookDiscount);

    Integer getPhDiscount();

    void setPhDiscount(Integer phDiscount);

    boolean isTrialDelivery();

    void setTrialDelivery(boolean trialDelivery);

    boolean isHasSpecialOffer();

    void setHasSpecialOffer(boolean hasSpecialOffer);

    boolean isSpecialOfferEnabled(int productQuantity);

    /**
     * Вид продуктов - "Планшеты". Они создаются на основе твердого альбом с полной запечаткой.
     * Их отличие в том, что форзац у них тоже запечатывается, а не остается белым, как у обычных альбомов.
     * <p>
     * Если эта галочка установлена, то в онлайн-сборщике для таких продуктов вместо первой и последней страницы
     * отображается не страница, а разворот. Причем это полноценный разворот, можно разместить фото на весь разворот,
     * можно слева, можно справа, как и для других разворотов.
     * <p>
     * Т.е. сейчас, например, для продукта 01-05-01 в сборщике показывается:
     * обложка, стр.1, разворот 2-3, разворот 4-5, ..., разворот 14-15, стр. 16.
     * А для аналогичного планшета должно быть:
     * обложка, разворот форзац-1, разворот 2-3, разворот 4-5, ..., разворот 14-15, разворот 16-форзац.
     * <p>
     * Форзацы считаются как самостоятельные страницы. Т.е., если указано кол-во страниц - 2, то схема альбома:
     * обложка
     * "первый форзац" - "последний форзац"
     * если указано кол-во стр. 4, то схема альбома:
     * обложка
     * первый форзац - стр. 1
     * стр. 2 - последний форзац
     * если указано кол-во стр. 6, то схема альбома:
     * обложка
     * первый форзац - стр. 1
     * стр. 2 - стр. 3
     * стр. 4 - последний форзац
     * и т.д.
     *
     * @return true if product is a tablet
     * @see <a href="http://jira.minogin.ru/browse/IMAGEBOOK-392">Планшеты</a>
     */
    boolean isTablet();

    Set<User> getAccessedUsers();

    void setAccessedUsers(Set<User> user);

    String getArticle();

    void setArticle(String article);

    Set<UserAlbumDiscount> getAlbumDiscounts();

    void setAlbumDiscounts(Set<UserAlbumDiscount> albumDiscounts);

    String getApproxProdTime();

    void setApproxProdTime(String approxProdTime);

    String getCalcComment();

    void setCalcComment(String calcComment);

    boolean isBarcodeOnTheLastSpread();

    void setBarcodeOnTheLastSpread(boolean barcodeOnTheLastSpread);

    boolean isNonCalc();

    void setNonCalc(boolean nonCalc);
}