package ru.imagebook.shared.model;

public interface Album extends Product {
	String JPEG_COVER_FOLDER = "jpegCoverFolder";

	String SIZE = "size";
	String COVER_SIZE = "coverSize";
	String PDF_COVER_WIDTH = "pdfCoverWidth";
	String PDF_COVER_HEIGHT = "pdfCoverHeight";
	String HARDCOVER = "hardcover";
	String COVER_WIDTH = "coverWidth";
	String COVER_HEIGHT = "coverHeight";
	String BAR_CODE_X = "barcodeX";
	String BAR_CODE_Y = "barcodeY";
	String MPHOTO_BAR_CODE_X = "mphotoBarcodeX";
	String MPHOTO_BAR_CODE_Y = "mphotoBarcodeY";
	String INNER_CROP = "innerCrop";
	String FRONT_UPPER_CROP = "frontUpperCrop";
	String FRONT_BOTTOM_CROP = "frontBottomCrop";
	String FRONT_LEFT_CROP = "frontLeftCrop";
	String FRONT_RIGHT_CROP = "frontRightCrop";
	String BACK_UPPER_CROP = "backUpperCrop";
	String BACK_BOTTOM_CROP = "backBottomCrop";
	String BACK_LEFT_CROP = "backLeftCrop";
	String BACK_RIGHT_CROP = "backRightCrop";
	String UPPER_COVER_SAFE_AREA = "upperCoverSafeArea";
	String BOTTOM_COVER_SAFE_AREA = "bottomCoverSafeArea";
	String LEFT_COVER_SAFE_AREA = "leftCoverSafeArea";
	String RIGHT_COVER_SAFE_AREA = "rightCoverSafeArea";
	String COVER_NAME = "coverName";
	String LAST_PAGE_TEMPLATE = "lastPageTemplate";
	String BASE_PRICE = "basePrice";
	String COVER_LAMINATION_PRICE = "coverLaminationPrice";
	String PAGE_PRICE = "pagePrice";
	String PH_BASE_PRICE = "phBasePrice";
	String PH_PAGE_PRICE = "phPagePrice";
	String PH_COVER_LAMINATION_PRICE = "phCoverLaminationPrice";
	String PAGE_LAMINATION_PRICE = "pageLaminationPrice";
	String PH_PAGE_LAMINATION_PRICE = "phPageLaminationPrice";
	String ACCESSED_USERS = "accessedUsers";
	String FLYLEAFS = "flyleafs";
	String SUPPORTS_VELLUM = "supportVellums";

	String getSize();

	void setSize(String size);

	Boolean isSeparateCover();

	Boolean isWhiteCover();
	
	Boolean isPlotterCover();

	Boolean isRicohCover();

	Boolean isLeatheretteCover();
	
	Boolean isClipType();

    Boolean isHardCoverFullPrint();

	Boolean isEverflat();

	Boolean isHardCover();

	Boolean isHardOrEverflat();

	String getCoverSize();

	void setCoverSize(String coverSize);

	Integer getPdfCoverWidth();

	void setPdfCoverWidth(Integer pdfCoverWidth);

	Integer getPdfCoverHeight();

	void setPdfCoverHeight(Integer pdfCoverHeight);

	Boolean isHardcover();

	void setHardcover(Boolean hardcover);
	
	Integer getCoverWidth();

    void setCoverWidth(Integer CoverWidth);

    Integer getCoverHeight();

    void setCoverHeight(Integer CoverHeight);

	Integer getBarcodeX();

	void setBarcodeX(Integer barcodeX);

	Integer getBarcodeY();

	void setBarcodeY(Integer barcodeY);

    Integer getMphotoBarcodeX();

    void setMphotoBarcodeX(Integer mPhotoBarcodeX);

    Integer getMphotoBarcodeY();

    void setMphotoBarcodeY(Integer mPhotoBarcodeY);

	Integer getInnerCrop();

	void setInnerCrop(Integer innerCrop);

	String getJpegCoverFolder();

	void setJpegCoverFolder(String jpegCoverFolder);

	Integer getFrontUpperCrop();

	void setFrontUpperCrop(Integer frontUpperCrop);

	Integer getFrontBottomCrop();

	void setFrontBottomCrop(Integer frontBottomCrop);

	Integer getFrontLeftCrop();

	void setFrontLeftCrop(Integer frontLeftCrop);

	Integer getFrontRightCrop();

	void setFrontRightCrop(Integer frontRightCrop);

	Integer getBackUpperCrop();

	void setBackUpperCrop(Integer backUpperCrop);

	Integer getBackBottomCrop();

	void setBackBottomCrop(Integer backBottomCrop);

	Integer getBackLeftCrop();

	void setBackLeftCrop(Integer backLeftCrop);

	Integer getBackRightCrop();

	void setBackRightCrop(Integer backRightCrop);

	boolean isSuperAlbum();

	Float getUpperCoverSafeArea();

	void setUpperCoverSafeArea(Float upperCoverSafeArea);

	Float getBottomCoverSafeArea();

	void setBottomCoverSafeArea(Float bottomCoverSafeArea);

	Float getLeftCoverSafeArea();

	void setLeftCoverSafeArea(Float leftCoverSafeArea);

	Float getRightCoverSafeArea();

	void setRightCoverSafeArea(Float rightCoverSafeArea);

	String getCoverName();

	void setCoverName(String coverName);

	String getLastPageTemplate();

	void setLastPageTemplate(String lastPageTemplate);

	double getBasePrice();

	void setBasePrice(double basePrice);
	
	double getPagePrice();

	void setPagePrice(double pagePrice);
	
	double getCoverLaminationPrice();
	
	void setCoverLaminationPrice(double coverLaminationPrice);
	
	double getPhBasePrice();

	void setPhBasePrice(double phBasePrice);

	double getPhPagePrice();

	void setPhPagePrice(double phPagePrice);
	
	double getPhCoverLaminationPrice();

	void setPhCoverLaminationPrice(double phCoverLaminationPrice);

	double getPageLaminationPrice();

	void setPageLaminationPrice(double pageLaminationPrice);

	double getPhPageLaminationPrice();

	void setPhPageLaminationPrice(double phPageLaminationPrice);

	Boolean isFlyleafs();

	void setFlyleafs(Boolean flyleafs);

	Boolean isSupportsVellum();

	void setSupportsVellum(Boolean supportsVellum);
}