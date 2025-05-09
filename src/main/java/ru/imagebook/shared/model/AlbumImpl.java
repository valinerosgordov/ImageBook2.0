package ru.imagebook.shared.model;

import static ru.imagebook.shared.model.ProductType.EVERFLAT_FULL_PRINT;
import static ru.imagebook.shared.model.ProductType.EVERFLAT_WHITE_MARGINS;
import static ru.imagebook.shared.model.ProductType.HARD_COVER_FULL_PRINT;
import static ru.imagebook.shared.model.ProductType.HARD_COVER_WHITE_MARGINS;
import static ru.imagebook.shared.model.ProductType.TABLET;

public class AlbumImpl extends ProductImpl implements Album {
	private static final long serialVersionUID = -6504762908153670291L;

	public AlbumImpl() {
		setBasePrice(0);
		setPagePrice(0);
		
		setCoverLaminationPrice(0);
		
		setPhBasePrice(0);
		setPhPagePrice(0);
		setPhCoverLaminationPrice(0);
		setPageLaminationPrice(0);
		setPhPageLaminationPrice(0);
	}

	@Override
	public String getSize() {
		return get(SIZE);
	}

	@Override
	public void setSize(String size) {
		set(SIZE, size);
	}

	@Override
	public Boolean isSeparateCover() {
		Integer type = getType();
		return type == EVERFLAT_WHITE_MARGINS || type == EVERFLAT_FULL_PRINT
				|| type == HARD_COVER_WHITE_MARGINS
				|| type == HARD_COVER_FULL_PRINT
				|| type == TABLET;
	}

	@Override
	public Boolean isWhiteCover() {
		Integer type = getType();
		return type != null && (type == EVERFLAT_WHITE_MARGINS || type == HARD_COVER_WHITE_MARGINS);
	}
	
	@Override
	public Boolean isPlotterCover() {
		return getCover() == Cover.PLOTTER_COVER;
	}

	@Override
	public Boolean isRicohCover() {
		return getCover() == Cover.RICOH_COVER;
	}

	@Override
	public Boolean isLeatheretteCover() {
		return getCover() == Cover.LEATHERETTE_COVER;
	}

	@Override
	public Boolean isClipType() {
	    return getType() == ProductType.CLIP;
	}

	@Override
	public Boolean isHardCoverFullPrint() {
		return (getType() == HARD_COVER_FULL_PRINT || getType() == TABLET);
	}

	@Override
	public Boolean isEverflat() {
		Integer type = getType();
		return type != null && (type == EVERFLAT_FULL_PRINT || type == EVERFLAT_WHITE_MARGINS);
	}

	@Override
	public Boolean isHardCover() {
		Integer type = getType();
		return type != null && (type == HARD_COVER_FULL_PRINT || type == HARD_COVER_WHITE_MARGINS || type == TABLET);
	}

	@Override
	public Boolean isHardOrEverflat() {
		return isHardCover() || isEverflat();
	}

	@Override
	public String getCoverSize() {
		return get(COVER_SIZE);
	}

	@Override
	public void setCoverSize(String coverSize) {
		set(COVER_SIZE, coverSize);
	}

	@Override
	public Integer getPdfCoverWidth() {
		return get(PDF_COVER_WIDTH);
	}

	@Override
	public void setPdfCoverWidth(Integer pdfCoverWidth) {
		set(PDF_COVER_WIDTH, pdfCoverWidth);
	}

	@Override
	public Integer getPdfCoverHeight() {
		return get(PDF_COVER_HEIGHT);
	}

	@Override
	public void setPdfCoverHeight(Integer pdfCoverHeight) {
		set(PDF_COVER_HEIGHT, pdfCoverHeight);
	}

	@Override
	public Boolean isHardcover() {
		return get(HARDCOVER);
	}

	@Override
	public void setHardcover(Boolean hardcover) {
		set(HARDCOVER, hardcover);
	}
	
    @Override
    public Integer getCoverWidth() {
        return get(COVER_WIDTH);
    }

    @Override
    public void setCoverWidth(Integer coverWidth) {
        set(COVER_WIDTH, coverWidth);
    }

    @Override
    public Integer getCoverHeight() {
        return get(COVER_HEIGHT);
    }

    @Override
    public void setCoverHeight(Integer coverHeight) {
        set(COVER_HEIGHT, coverHeight);
    }

	@Override
	public Integer getBarcodeX() {
		return get(BAR_CODE_X);
	}

	@Override
	public void setBarcodeX(Integer barcodeX) {
		set(BAR_CODE_X, barcodeX);
	}

	@Override
	public Integer getBarcodeY() {
		return get(BAR_CODE_Y);
	}

	@Override
	public void setBarcodeY(Integer barcodeY) {
		set(BAR_CODE_Y, barcodeY);
	}

	@Override
	public Integer getMphotoBarcodeX() {
		return get(MPHOTO_BAR_CODE_X);
	}

	@Override
	public void setMphotoBarcodeX(Integer mphotoBarcodeX) {
		set(MPHOTO_BAR_CODE_X, mphotoBarcodeX);
	}

	@Override
	public Integer getMphotoBarcodeY() {
		return get(MPHOTO_BAR_CODE_Y);
	}

	@Override
	public void setMphotoBarcodeY(Integer mPhotoBarcodeY) {
		set(MPHOTO_BAR_CODE_Y, mPhotoBarcodeY);
	}

	@Override
	public Integer getInnerCrop() {
		return get(INNER_CROP);
	}

	@Override
	public void setInnerCrop(Integer innerCrop) {
		set(INNER_CROP, innerCrop);
	}

	@Override
	public String getJpegCoverFolder() {
		return get(JPEG_COVER_FOLDER);
	}

	@Override
	public void setJpegCoverFolder(String jpegCoverFolder) {
		set(JPEG_COVER_FOLDER, jpegCoverFolder);
	}

	@Override
	public Integer getFrontUpperCrop() {
		return get(FRONT_UPPER_CROP);
	}

	@Override
	public void setFrontUpperCrop(Integer frontUpperCrop) {
		set(FRONT_UPPER_CROP, frontUpperCrop);
	}

	@Override
	public Integer getFrontBottomCrop() {
		return get(FRONT_BOTTOM_CROP);
	}

	@Override
	public void setFrontBottomCrop(Integer frontBottomCrop) {
		set(FRONT_BOTTOM_CROP, frontBottomCrop);
	}

	@Override
	public Integer getFrontLeftCrop() {
		return get(FRONT_LEFT_CROP);
	}

	@Override
	public void setFrontLeftCrop(Integer frontLeftCrop) {
		set(FRONT_LEFT_CROP, frontLeftCrop);
	}

	@Override
	public Integer getFrontRightCrop() {
		return get(FRONT_RIGHT_CROP);
	}

	@Override
	public void setFrontRightCrop(Integer frontRightCrop) {
		set(FRONT_RIGHT_CROP, frontRightCrop);
	}

	@Override
	public Integer getBackUpperCrop() {
		return get(BACK_UPPER_CROP);
	}

	@Override
	public void setBackUpperCrop(Integer backUpperCrop) {
		set(BACK_UPPER_CROP, backUpperCrop);
	}

	@Override
	public Integer getBackBottomCrop() {
		return get(BACK_BOTTOM_CROP);
	}

	@Override
	public void setBackBottomCrop(Integer backBottomCrop) {
		set(BACK_BOTTOM_CROP, backBottomCrop);
	}

	@Override
	public Integer getBackLeftCrop() {
		return get(BACK_LEFT_CROP);
	}

	@Override
	public void setBackLeftCrop(Integer backLeftCrop) {
		set(BACK_LEFT_CROP, backLeftCrop);
	}

	@Override
	public Integer getBackRightCrop() {
		return get(BACK_RIGHT_CROP);
	}

	@Override
	public void setBackRightCrop(Integer backRightCrop) {
		set(BACK_RIGHT_CROP, backRightCrop);
	}

	@Override
	public boolean isSuperAlbum() {
		return getType() == 4 && getNumber() == 9;
	}

	@Override
	public Float getUpperCoverSafeArea() {
		return get(UPPER_COVER_SAFE_AREA);
	}

	@Override
	public void setUpperCoverSafeArea(Float upperCoverSafeArea) {
		set(UPPER_COVER_SAFE_AREA, upperCoverSafeArea);
	}

	@Override
	public Float getBottomCoverSafeArea() {
		return get(BOTTOM_COVER_SAFE_AREA);
	}

	@Override
	public void setBottomCoverSafeArea(Float bottomCoverSafeArea) {
		set(BOTTOM_COVER_SAFE_AREA, bottomCoverSafeArea);
	}

	@Override
	public Float getLeftCoverSafeArea() {
		return get(LEFT_COVER_SAFE_AREA);
	}

	@Override
	public void setLeftCoverSafeArea(Float leftCoverSafeArea) {
		set(LEFT_COVER_SAFE_AREA, leftCoverSafeArea);
	}

	@Override
	public Float getRightCoverSafeArea() {
		return get(RIGHT_COVER_SAFE_AREA);
	}

	@Override
	public void setRightCoverSafeArea(Float rightCoverSafeArea) {
		set(RIGHT_COVER_SAFE_AREA, rightCoverSafeArea);
	}

	@Override
	public String getCoverName() {
		return get(COVER_NAME);
	}

	@Override
	public void setCoverName(String coverName) {
		set(COVER_NAME, coverName);
	}

	@Override
	public String getLastPageTemplate() {
		return get(LAST_PAGE_TEMPLATE);
	}

	@Override
	public void setLastPageTemplate(String lastPageTemplate) {
		set(LAST_PAGE_TEMPLATE, lastPageTemplate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Album))
			return false;

		Album album = (Album) obj;
		if (getId() == null || album.getId() == null)
			return false;

		return getId().equals(album.getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}

	@Override
	public double getBasePrice() {
		return (Double) get(BASE_PRICE);
	}

	@Override
	public void setBasePrice(double basePrice) {
		set(BASE_PRICE, basePrice);
	}
	
	@Override
	public double getPagePrice() {
		return (Double) get(PAGE_PRICE);
	}

	@Override
	public void setPagePrice(double pagePrice) {
		set(PAGE_PRICE, pagePrice);
	}
	
	@Override
	public double getCoverLaminationPrice() {
		return (Double) get(COVER_LAMINATION_PRICE);
	}
	
	@Override
	public void setCoverLaminationPrice(double coverLaminationPrice) {
		set(COVER_LAMINATION_PRICE, coverLaminationPrice);
	}

	@Override
	public double getPhBasePrice() {
		return (Double) get(PH_BASE_PRICE);
	}

	@Override
	public void setPhBasePrice(double phBasePrice) {
		set(PH_BASE_PRICE, phBasePrice);
	}

	@Override
	public double getPhPagePrice() {
		return (Double) get(PH_PAGE_PRICE);
	}

	@Override
	public void setPhPagePrice(double phPagePrice) {
		set(PH_PAGE_PRICE, phPagePrice);
	}

	@Override
	public double getPhCoverLaminationPrice() {
		return (Double) get(PH_COVER_LAMINATION_PRICE);
	}

	@Override
	public void setPhCoverLaminationPrice(double phCoverLaminationPrice) {
		set(PH_COVER_LAMINATION_PRICE, phCoverLaminationPrice);
	}

	@Override
	public double getPageLaminationPrice() {
		return (Double) get(PAGE_LAMINATION_PRICE);
	}

	@Override
	public void setPageLaminationPrice(double pageLaminationPrice) {
		set(PAGE_LAMINATION_PRICE, pageLaminationPrice);
	}

	@Override
	public double getPhPageLaminationPrice() {
		return (Double) get(PH_PAGE_LAMINATION_PRICE);
	}

	@Override
	public void setPhPageLaminationPrice(double phPageLaminationPrice) {
		set(PH_PAGE_LAMINATION_PRICE, phPageLaminationPrice);
	}

	@Override
	public Boolean isFlyleafs() {
		return get(FLYLEAFS);
	}

	@Override
	public void setFlyleafs(Boolean flyleafs) {
		set(FLYLEAFS, flyleafs);
	}

	@Override
	public Boolean isSupportsVellum() {
		return get(SUPPORTS_VELLUM);
	}

	@Override
	public void setSupportsVellum(Boolean supportsVellum) {
		set(SUPPORTS_VELLUM, supportsVellum);
	}
}
