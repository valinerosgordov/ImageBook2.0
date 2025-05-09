package ru.imagebook.shared.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.client.i18n.MultiString;

public abstract class ProductImpl extends BaseEntityBean implements Product {
    private static final long serialVersionUID = -358614994305292851L;

    protected ProductImpl() {
        setType(0);
        setMinQuantity(1);
        setColorRange(new ArrayList<Integer>());
        setCoverLamRange(new ArrayList<Integer>());
        setPageLamRange(new ArrayList<Integer>());
        setAddressPrinted(false);
        setNonEditor(false);
        setTrialAlbum(false);
        setHasSpecialOffer(false);
        setMinAlbumsCountForDiscount(0);
        setTrialDelivery(false);
        setAccessedUsers(new HashSet<User>());
    }

    @Override
    public Integer getType() {
        return get(TYPE);
    }

    @Override
    public void setType(Integer type) {
        set(TYPE, type);
    }

    @Override
    public Integer getNumber() {
        return get(NUMBER);
    }

    @Override
    public void setNumber(Integer number) {
        set(NUMBER, number);
    }

    @Override
    public MultiString getName() {
        return get(NAME);
    }

    @Override
    public void setName(MultiString name) {
        set(NAME, name);
    }

    @Override
    public Integer getAvailability() {
        return get(AVAILABILITY);
    }

    @Override
    public void setAvailability(Integer availability) {
        set(AVAILABILITY, availability);
    }

    @Override
    public String getBlockFormat() {
        return get(BLOCK_FORMAT);
    }

    @Override
    public void setBlockFormat(String blockFormat) {
        set(BLOCK_FORMAT, blockFormat);
    }

    @Override
    public Integer getBinding() {
        return get(BINDING);
    }

    @Override
    public void setBinding(Integer binding) {
        set(BINDING, binding);
    }

    @Override
    public Integer getCover() {
        return get(COVER);
    }

    @Override
    public void setCover(Integer cover) {
        set(COVER, cover);
    }

    @Override
    public Integer getPaper() {
        return get(PAPER);
    }

    @Override
    public void setPaper(Integer paper) {
        set(PAPER, paper);
    }

    @Override
    public Integer getMultiplicity() {
        return get(MULTIPLICITY);
    }

    @Override
    public void setMultiplicity(Integer multiplicity) {
        set(MULTIPLICITY, multiplicity);
    }

    @Override
    public Integer getMinPageCount() {
        return get(MIN_PAGE_COUNT);
    }

    @Override
    public void setMinPageCount(Integer minPageCount) {
        set(MIN_PAGE_COUNT, minPageCount);
    }

    @Override
    public Integer getMaxPageCount() {
        return get(MAX_PAGE_COUNT);
    }

    @Override
    public void setMaxPageCount(Integer maxPageCount) {
        set(MAX_PAGE_COUNT, maxPageCount);
    }

    @Override
    public Integer getMinQuantity() {
        return get(MIN_QUANTITY);
    }

    @Override
    public void setMinQuantity(Integer minQuantity) {
        set(MIN_QUANTITY, minQuantity);
    }

    @Override
    public List<Integer> getColorRange() {
        return get(COLOR_RANGE);
    }

    @Override
    public void setColorRange(List<Integer> colorRange) {
        set(COLOR_RANGE, colorRange);
    }

    @Override
    public List<Integer> getCoverLamRange() {
        return get(COVER_LAM_RANGE);
    }

    @Override
    public void setCoverLamRange(List<Integer> coverLamRange) {
        set(COVER_LAM_RANGE, coverLamRange);
    }

    @Override
    public List<Integer> getPageLamRange() {
        return get(PAGE_LAM_RANGE);
    }

    @Override
    public void setPageLamRange(List<Integer> pageLamRange) {
        set(PAGE_LAM_RANGE, pageLamRange);
    }

    @Override
    public boolean isTrialAlbum() {
        return (Boolean) get(TRIAL_ALBUM);
    }

    @Override
    public void setTrialAlbum(boolean trialAlbum) {
        set(TRIAL_ALBUM, trialAlbum);
    }

    @Override
    public boolean isAddressPrinted() {
        return (Boolean) get(ADDRESS_PRINTED);
    }

    @Override
    public void setAddressPrinted(boolean addressPrinted) {
        set(ADDRESS_PRINTED, addressPrinted);
    }

    @Override
    public Integer getWidth() {
        return get(WIDTH);
    }

    @Override
    public void setWidth(Integer width) {
        set(WIDTH, width);
    }

    @Override
    public Integer getHeight() {
        return get(HEIGHT);
    }

    @Override
    public void setHeight(Integer height) {
        set(HEIGHT, height);
    }

    @Override
    public String getJpegFolder() {
        return get(JPEG_FOLDER);
    }

    @Override
    public void setJpegFolder(String jpegFolder) {
        set(JPEG_FOLDER, jpegFolder);
    }

    @Override
    public Integer getBlockWidth() {
        return get(BLOCK_WIDTH);
    }

    @Override
    public void setBlockWidth(Integer blockWidth) {
        set(BLOCK_WIDTH, blockWidth);
    }

    @Override
    public Integer getBlockHeight() {
        return get(BLOCK_HEIGHT);
    }

    @Override
    public void setBlockHeight(Integer blockHeight) {
        set(BLOCK_HEIGHT, blockHeight);
    }

    @Override
    public Float getUpperSafeArea() {
        return get(UPPER_SAFE_AREA);
    }

    @Override
    public void setUpperSafeArea(Float upperSafeArea) {
        set(UPPER_SAFE_AREA, upperSafeArea);
    }

    @Override
    public Float getBottomSafeArea() {
        return get(BOTTOM_SAFE_AREA);
    }

    @Override
    public void setBottomSafeArea(Float bottomSafeArea) {
        set(BOTTOM_SAFE_AREA, bottomSafeArea);
    }

    @Override
    public Float getInnerSafeArea() {
        return get(INNER_SAFE_AREA);
    }

    @Override
    public void setInnerSafeArea(Float innerSafeArea) {
        set(INNER_SAFE_AREA, innerSafeArea);
    }

    @Override
    public Float getOuterSafeArea() {
        return get(OUTER_SAFE_AREA);
    }

    @Override
    public void setOuterSafeArea(Float outerSafeArea) {
        set(OUTER_SAFE_AREA, outerSafeArea);
    }

    @Override
    public boolean isNonEditor() {
        return (Boolean) get(NON_EDITOR);
    }

    @Override
    public void setNonEditor(boolean nonEditor) {
        set(NON_EDITOR, nonEditor);
    }

    @Override
    public Integer getMinAlbumsCountForDiscount() {
        return get(MIN_ALBUMS_COUNT_FOR_DISCOUNT);
    }

    @Override
    public void setMinAlbumsCountForDiscount(Integer minAlbumsCountForDiscount) {
        set(MIN_ALBUMS_COUNT_FOR_DISCOUNT, minAlbumsCountForDiscount);
    }

    @Override
    public Integer getImagebookDiscount() {
        return get(IMAGEBOOK_DISCOUNT);
    }

    @Override
    public void setImagebookDiscount(Integer imagebookDiscount) {
        set(IMAGEBOOK_DISCOUNT, imagebookDiscount);
    }

    @Override
    public Integer getPhDiscount() {
        return get(PH_DISCOUNT);
    }

    @Override
    public void setPhDiscount(Integer phDiscount) {
        set(PH_DISCOUNT, phDiscount);
    }

    @Override
    public int compareTo(Product product) {
        if (getType() != null && product.getType() != null && getNumber() != null
                && product.getNumber() != null) {
            if (!getType().equals(product.getType()))
                return getType().compareTo(product.getType());
            else
                return getNumber().compareTo(product.getNumber());
        } else
            return getId().compareTo(product.getId());
    }

    @Override
    public boolean isCongratulatory() {
        return getType() == ProductType.CONGRATULATORY
                || (getType() == 4 && getNumber() == 11);    // hack for "Мое лето"
    }

    @Override
    public boolean isTrialDelivery() {
        return (Boolean) get(TRIAL_DELIVERY);
    }

    @Override
    public void setTrialDelivery(boolean trialDelivery) {
        set(TRIAL_DELIVERY, trialDelivery);
    }

    @Override
    public boolean isHasSpecialOffer() {
        return (Boolean) get(HAS_SPECIAL_OFFER);
    }

    @Override
    public void setHasSpecialOffer(boolean hasSpecialOffer) {
        set(HAS_SPECIAL_OFFER, hasSpecialOffer);
    }

    @Override
    public boolean isSpecialOfferEnabled(int productQuantity) {
        return isHasSpecialOffer() && productQuantity >= getMinAlbumsCountForDiscount();
    }

    @Override
    public boolean isTablet() {
        return getType() == ProductType.TABLET;
    }

    @Override
    public Set<User> getAccessedUsers() {
        return get(ACCESSED_USERS);
    }

    @Override
    public void setAccessedUsers(Set<User> user) {
        set(ACCESSED_USERS, user);
    }

	@Override
	public String getArticle() {
		return get(ARTICLE);
	}

	@Override
	public void setArticle(String article) {
		setTransient(ARTICLE, article);
	}

    @Override
    public Set<UserAlbumDiscount> getAlbumDiscounts() {
        return get(ALBUM_DISCOUNTS);
    }

    @Override
    public void setAlbumDiscounts(Set<UserAlbumDiscount> albumDiscounts) {
        set(ALBUM_DISCOUNTS, albumDiscounts);
    }

    @Override
    public String getApproxProdTime() {
        return get(APPROX_PROD_TIME);
    }

    @Override
    public void setApproxProdTime(String approxProdTime) {
        set(APPROX_PROD_TIME, approxProdTime);
    }

    @Override
    public String getCalcComment() {
        return get(CALC_COMMENT);
    }

    @Override
    public void setCalcComment(String calcComment) {
        set(CALC_COMMENT, calcComment);
    }

    @Override
    public boolean isBarcodeOnTheLastSpread() {
        return (Boolean) get(LAST_SPREAD_BARCODE);
    }

    @Override
    public void setBarcodeOnTheLastSpread(boolean barcodeOnTheLastSpread) {
        set(LAST_SPREAD_BARCODE, barcodeOnTheLastSpread);
    }

    @Override
    public boolean isNonCalc() {
        return (Boolean) get(NON_CALC);
    }

    @Override
    public void setNonCalc(boolean nonCalc) {
        set(NON_CALC, nonCalc);
    }
}
