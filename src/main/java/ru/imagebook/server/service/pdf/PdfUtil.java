package ru.imagebook.server.service.pdf;

import org.springframework.util.Assert;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Order;

public class PdfUtil {
    private static final String NONIMPOSED = "_nonimposed_";
    private static final String COVER = "_cover_";

	private static final String IMPOSED_PDF = "_%sekz.pdf";
	private static final String NONIMPOSED_PDF = NONIMPOSED + "%sekz.pdf";
	private static final String GROUP_PDF = "_group_%sekz.pdf";
	private static final String COVER_PDF = COVER + "%sekz.pdf";
	private static final String COVER_RICOH_PDF = COVER + "Ricoh_%sekz.pdf";
	
	private final PdfConfig config;

	public PdfUtil(PdfConfig config) {
		this.config = config;
	}

	public String getImposedPdfFileName(Order<?> order) {
		return order.getNumber() + String.format(IMPOSED_PDF, order.getQuantity());
	}
	
    public String getImposedPdfFileName(Order<?> order1, Order<?> order2) {
		return order1.getNumber() + "_" + order2.getNumber() + String.format(IMPOSED_PDF, getGroupQuantity(order1));
	}

	private int getGroupQuantity(Order<?> order1) {
		if (order1.getQuantity() == 1) {
			return order1.getQuantity();
		} else {
			return Math.round(order1.getQuantity() / 2 + (order1.getQuantity() % 2 == 0 ? 0 : 1));
		}
	}

	public String getImposedPdfPath(Order<?> order) {
		return config.getPdfPath() + "/" + getImposedPdfFileName(order);
	}
	
	public String getImposedPdfPath(Order<?> order1, Order<?> order2) {
		return config.getPdfPath() + "/" + getImposedPdfFileName(order1, order2);
	}

	public String getNonimposedPdfFileName(Order<?> order) {
		return order.getNumber() + String.format(NONIMPOSED_PDF, order.getQuantity());
	}

	public String getNonimposedPdfPath(Order<?> order) {
		return config.getPdfPath() + "/" + getNonimposedPdfFileName(order);
	}
	
	public static String getFirstOrderNumber(final Order<?> order) {
		String orderNumber = order.getNumber();
		if (order.getNumber().contains("-")) {
			orderNumber = order.getNumber().substring(0, order.getNumber().indexOf("-"));
		}
		return orderNumber;
	}

    /**
     * Generate cover pdf file name for Order
     * - For default order (XXXX) generate cover file name based on pattern [XXXX_cover_Qekz.pdf]
     * - For package order (XXXX-N) generate cover file name based on pattern [XXXX(without `-N`)-1-Q_cover_Qekz.pdf]
     *   First part of cover name for package order [XXXX(without `-N`)-1-Q] - simplifies pdf transfer logic
     *
     * `Q` is a order.quantityForPackages (or summary of package orders quantityForPackages)
     *
     * @param order order
     * @param quantityForPackages order.quantityForPackages or summary of package orders quantityForPackages
     * @return cover pdf file name for Order
     */
	public String getCoverPdfFileName(Order<?> order, Integer quantityForPackages) {
		boolean isPackageOrderWithCommonCover = order.isPackaged() && quantityForPackages != null;
		String baseCoverFileName = isPackageOrderWithCommonCover
			? (order.getPackageNumber() + "-1-" + quantityForPackages)
			: order.getNumber();
		Integer quantity = isPackageOrderWithCommonCover ? quantityForPackages : order.getQuantity();
		return baseCoverFileName
			+ (((Album) order.getProduct()).isRicohCover()
				? String.format(COVER_RICOH_PDF, quantity)
				: String.format(COVER_PDF, quantity));
	}

	public String getCoverPdfPath(Order<?> order) {
		return config.getPdfPath() + "/" + getCoverPdfFileName(order, null);
	}
	
	public String getCoverPdfPath(Order<?> order, Integer quantity) {
		return config.getPdfPath() + "/" + getCoverPdfFileName(order, quantity);
	}

	public String getGroupPdfFileName(Order<?> order1, Order<?> order2) {
		return order1.getNumber() + "_" + order2.getNumber() + String.format(GROUP_PDF, getGroupQuantity(order1));
	}
	
	public String getGroupPdfPath(Order<?> order1, Order<?> order2) {
		return config.getPdfPath() + "/" + getGroupPdfFileName(order1, order2);
	}

	public static boolean isNonImposedPdf(String pdfFileName) {
        Assert.hasText(pdfFileName);
        return pdfFileName.contains(NONIMPOSED);
    }

    public static boolean isCoverPdf(String pdfFileName) {
        Assert.hasText(pdfFileName);
	    return pdfFileName.contains(COVER);
    }
}
