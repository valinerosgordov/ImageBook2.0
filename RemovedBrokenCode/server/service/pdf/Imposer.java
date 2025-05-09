package ru.imagebook.server.service.pdf;

import static ru.imagebook.server.service.pdf.PdfConst.MM_TO_PX;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.exception.Exceptions;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Imposer {
	private final static String FONT_LOCATION = "/ru/imagebook/server/service/pdf/arial.ttf";

	private final Order<?> order;
	private final Order<?> order2;
	private final boolean isGroupImpose;
	private final Logger logger;
	private final PdfUtil util;

	public Imposer(Order<?> order1, Order<?> order2, Logger logger, PdfUtil util) {
		this.order = order1;
		this.order2 = order2;
		this.logger = logger;
		this.isGroupImpose = order2 != null;
		this.util = util;
	}

	/** Start imposing */
	public void impose() {
		String imposedPath = order2 == null ? util.getImposedPdfPath(order) : util.getImposedPdfPath(order, order2);
		String nonImposedPath = order2 == null ? util.getNonimposedPdfPath(order) : util.getGroupPdfPath(order, order2);

		String imposedFilename = FilenameUtils.getBaseName(imposedPath);
		String nonImposedFilename = FilenameUtils.getBaseName(nonImposedPath);

		logger.debug("Imposing: " + order.getNumber()
			+ (order2 != null ? (", " + order2.getNumber()) : "")
			+ " [imposed = " + imposedFilename + ", nonImposed = " + nonImposedFilename + "]");

		Product product = order.getProduct();
		int number = product.getNumber();

		switch (product.getType()) {
			case ProductType.EVERFLAT_WHITE_MARGINS:
				if (number == 6) {
					impose010106(nonImposedPath, imposedPath);
				} else if (number == 8) {
					impose010108(nonImposedPath, imposedPath);
				} else {
				    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.EVERFLAT_FULL_PRINT:
				if (number == 6) {
					impose010206(nonImposedPath, imposedPath);
				} else if (number == 8) {
					impose010208(nonImposedPath, imposedPath);
				} else if (number == 10) {
					impose010210(nonImposedPath, imposedPath);
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.HARD_COVER_FULL_PRINT:
				if (number == 1) {
					impose010501(nonImposedPath, imposedPath);
				} else if (number == 2) {
					impose010502(nonImposedPath, imposedPath);
				} else if (number == 3) {
					impose010503(nonImposedPath, imposedPath);
				} else if (number == 4) {
					impose010504(nonImposedPath, imposedPath);
				} else if (number == 5) {
					impose010505(nonImposedPath, imposedPath);
				} else if (number == 6) {
					impose010506(nonImposedPath, imposedPath);
				} else if (number == 7) {
					impose010507(nonImposedPath, imposedPath);
				} else if (number == 8) {
					impose010508(nonImposedPath, imposedPath);
				} else if (number == 9) {
					impose010509(nonImposedPath, imposedPath);
				} else if (number == 10) {
                    impose010510(nonImposedPath, imposedPath);
                } else if (number == 16) {
                    impose010501(nonImposedPath, imposedPath);
                } else if (number == 17) {
                    impose010502(nonImposedPath, imposedPath);
                } else if (number == 18) {
                    impose010503(nonImposedPath, imposedPath);
                } else if (number == 19) {
                    impose010506(nonImposedPath, imposedPath);
                } else if (number == 20) {
                    impose010507(nonImposedPath, imposedPath);
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.HARD_COVER_WHITE_MARGINS:
				if (number == 1) {
				    impose010401(nonImposedPath, imposedPath);
                } else if (number == 2) {
                    impose010402(nonImposedPath, imposedPath);
                } else if (number == 3) {
                    impose010403(nonImposedPath, imposedPath);
                } else if (number == 4) {
                    impose010404(nonImposedPath, imposedPath);
                } else if (number == 5) {
                    impose010405(nonImposedPath, imposedPath);
                } else if (number == 6) {
                    impose010406(nonImposedPath, imposedPath);
                } else if (number == 7) {
                    impose010407(nonImposedPath, imposedPath);
                } else if (number == 8) {
                    impose010408(nonImposedPath, imposedPath);
                } else if (number == 9) {
                    impose010409(nonImposedPath, imposedPath);
                } else if (number == 11) {
                    impose010403(nonImposedPath, imposedPath);
                } else if (number == 12) {
                    impose010406(nonImposedPath, imposedPath);
                } else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.TRIAL:
				if (number == 98) {
					impose010999(nonImposedPath, imposedPath);
				} else if (number == 99) {
					impose010999(nonImposedPath, imposedPath);
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.CONGRATULATORY:
				if (number == 99) {
					impose010999(nonImposedPath, imposedPath);
				} else if (number == 1) {
					impose010999(nonImposedPath, imposedPath); // 01-10-01 (011001)
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.CLIP:
				if (number == 1) {
					impose010999(nonImposedPath, imposedPath); // 01-08-01 (010801)
				} else if (number == 2) {
					impose010802(nonImposedPath, imposedPath);
				} else if (number == 3) {
					impose010803(nonImposedPath, imposedPath);
				} else if (number == 5) {
					impose010805(nonImposedPath, imposedPath);
				} else if (number == 7) {
					impose010807(nonImposedPath, imposedPath);
				} else if (number == 36) {
					impose010807(nonImposedPath, imposedPath);
				} else if (number == 37) {
					impose010803(nonImposedPath, imposedPath);
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.SPRING:
				if (number == 1) {
					impose010701(nonImposedPath, imposedPath);
				} else if (number == 2) {
					impose010702(nonImposedPath, imposedPath);
				} else if (number == 3) {
					impose010703(nonImposedPath, imposedPath);
				} else if (number == 4) {
					impose010704(nonImposedPath, imposedPath);
				} else if (number == 5) {
					impose010705(nonImposedPath, imposedPath);
				} else if (number == 6) {
					impose010706(nonImposedPath, imposedPath);
				} else if (number == 7) {
					impose010707(nonImposedPath, imposedPath);
				} else if (number == 8) {
					impose010708(nonImposedPath, imposedPath);
				} else if (number == 9) {
					impose010709(nonImposedPath, imposedPath);
				} else if (number == 10) {
                    impose010710(nonImposedPath, imposedPath);
                } else if (number == 11) {
                    impose010703(nonImposedPath, imposedPath);
                } else if (number == 12) {
                    impose010706(nonImposedPath, imposedPath);
                } else if (number == 13) {
                    impose010707(nonImposedPath, imposedPath);
                } else if (number == 21) {
					impose010710(nonImposedPath, imposedPath); // 01-07-21 (010721)
				} else if (number == 24) {
					impose010709(nonImposedPath, imposedPath); // 01-07-24 (010724)
//				} else if (number == 32) {
//					impose010709(nonImposedPath, imposedPath); // 01-07-32 (010732)
				} else {
                    imposeDefault(nonImposedPath, imposedPath);
                }
				break;
			case ProductType.TABLET:
				if (number == 3) {
					impose011103(nonImposedPath, imposedPath);
				} else if (number == 4) {
					impose011104(nonImposedPath, imposedPath);
				} else if (number == 6) {
					impose011106(nonImposedPath, imposedPath);
				} else if (number == 7) {
					impose011107(nonImposedPath, imposedPath);
				} else if (number == 8) {
					impose011107(nonImposedPath, imposedPath);
				} else if (number == 9) {
					impose011106(nonImposedPath, imposedPath);
				} else if (number == 10) {
					impose011103(nonImposedPath, imposedPath);
				} else if (number == 11) {
					impose011107(nonImposedPath, imposedPath);
				} else if (number == 12) {
					impose011106(nonImposedPath, imposedPath);
				} else {
					imposeDefault(nonImposedPath, imposedPath);
				}
		}

		if (order2 != null) {
			new File(util.getGroupPdfPath(order, order2)).delete();
		}
	}

	/** Impose album type 010106
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010106(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010106 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010106 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010106 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010106 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010106 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010106 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010106 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010106 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010106 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / PAGES_ON_SHEET - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010106 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / PAGES_ON_SHEET;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
			// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
			// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
			// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
			// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
			// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
			// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010108
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010108(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010108 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010108 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010108 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010108 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010108 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010108 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010108 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010108 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010108 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010108 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float MARG_LEN_NO_GAP = (MARKER_LENGTH - MARKER_GAP);
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT
					+ MARG_LEN_NO_GAP, MARGIN_X
					+ MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT - MARKER_GAP)); // left top
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y
					+ BLOCK_HEIGHT
					+ MARG_LEN_NO_GAP, MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_GAP)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - MARG_LEN_NO_GAP)); // left bottom
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y
					+ MARKER_GAP, MARGIN_X
					+ BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y - MARG_LEN_NO_GAP)); // right
// bottom
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - MARG_LEN_NO_GAP, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + BLOCK_HEIGHT - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_GAP, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_MARGIN,
					MARGIN_X + BLOCK_WIDTH + MARG_LEN_NO_GAP, MARGIN_Y + BLOCK_HEIGHT - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - MARG_LEN_NO_GAP, MARGIN_Y + MARKER_MARGIN,
					MARGIN_X + MARKER_GAP,
					MARGIN_Y + MARKER_MARGIN)); // right left
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_GAP, MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ BLOCK_WIDTH + MARG_LEN_NO_GAP, MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010206
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010206(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010206 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010206 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010206 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010206 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010206 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010206 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010206 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010206 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010206 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = order2 == null ? 2 : 1;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010206 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
			// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
			// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
			// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
			// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
			// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
			// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			int topPage = 1;
			int bottomPage = sheetCount + 1;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(topPage);
				pageNumbers.add(bottomPage);
				topPage += 1;
				bottomPage += 1;
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010208
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010208(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010208 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010208 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010208 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010208 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010208 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010208 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010208 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010208 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010208 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010208 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float MARG_LEN_NO_GAP = (MARKER_LENGTH - MARKER_GAP);
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT
					+ MARG_LEN_NO_GAP, MARGIN_X
					+ MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT - MARKER_GAP)); // left top
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y
					+ BLOCK_HEIGHT
					+ MARG_LEN_NO_GAP, MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_GAP)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - MARG_LEN_NO_GAP)); // left bottom
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y
					+ MARKER_GAP, MARGIN_X
					+ BLOCK_WIDTH - MARKER_MARGIN, MARGIN_Y - MARG_LEN_NO_GAP)); // right
// bottom
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - MARG_LEN_NO_GAP, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + BLOCK_HEIGHT - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_GAP, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_MARGIN,
					MARGIN_X + BLOCK_WIDTH + MARG_LEN_NO_GAP, MARGIN_Y + BLOCK_HEIGHT - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - MARG_LEN_NO_GAP, MARGIN_Y + MARKER_MARGIN,
					MARGIN_X + MARKER_GAP,
					MARGIN_Y + MARKER_MARGIN)); // right left
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH - MARKER_GAP, MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ BLOCK_WIDTH + MARG_LEN_NO_GAP, MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010210
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010210(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010210 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010210 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010210 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010210 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010210 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010210 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010210 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010210 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010210 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010210 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
			// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
			// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010999
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010999(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010999 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010999 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010999 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010999 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010999 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010999 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010999 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010999 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010999 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = order2 == null ? 4 : 2;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010999(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Process pages
	 *
	 * @param sheetCount
	 * @param PAGES_ON_SHEET
	 * @param gabsx
	 * @param gabsy
	 * @param blockWidth
	 * @param blockHeight
	 * @param x
	 * @param y
	 * @param pageRotations
	 * @param pageNumbers
	 * @param document
	 * @param pdfReader
	 * @param pdfWriter
	 * @param imposerConfig
	 * @throws BadElementException
	 * @throws DocumentException */
	public void processPages010999(int sheetCount, int PAGES_ON_SHEET, Float gabsx[], Float gabsy[],
								   Float blockWidth[],
								   Float blockHeight[], Float x[], Float y[], Integer pageRotations[],
								   List<Integer> pageNumbers, Document document,
								   PdfReader pdfReader, PdfWriter pdfWriter, ImposerConfig imposerConfig)
			throws BadElementException,
			DocumentException, IOException {
		ImposerPage imposerPage = null;
		ImposerSheet imposerSheet = null;
		ImposerPageConfig imposerPageConfig = null;
		int pageNumberIndex = 0;
		for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
			imposerSheet = new ImposerSheet();
			for (int page = 0; page < PAGES_ON_SHEET; page++) {
				imposerPageConfig = new ImposerPageConfig();
				imposerPageConfig.setGabx(gabsx[page]);
				imposerPageConfig.setGaby(gabsy[page]);
				imposerPageConfig.setBlockWidth(blockWidth[page]);
				imposerPageConfig.setBlockHeight(blockHeight[page]);
				imposerPageConfig.setX(x[page]);
				imposerPageConfig.setY(y[page]);
				imposerPage = new ImposerPage(pageRotations[page], pageNumbers.get(pageNumberIndex), imposerPageConfig);
				imposerSheet.addPage(imposerPage);
				pageNumberIndex++;
			}
			imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
			if (sheetIndex != sheetCount - 1) {
				document.newPage();
			}
		}
		logger.debug("Imposing finished: " + order.getNumber());
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines010999(float MARGIN_X, float MARGIN_Y, float GAB_X, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH) {
		// Vertical lines
		float p1 = MARGIN_Y / 2;
		float p2 = MARGIN_Y + 2 * MM_TO_PX;
		float shift = MARGIN_X + GAB_X;
		float c = WIDTH / 2;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		imposerConfig.addFigure(new Line(shift, HEIGHT - p2, shift, HEIGHT - p1));
		imposerConfig.addFigure(new Line(c, HEIGHT - p2, c, HEIGHT - p1));
		imposerConfig.addFigure(new Line(WIDTH - shift, HEIGHT - p2, WIDTH - shift, HEIGHT - p1));
		imposerConfig.addFigure(new Line(shift, p1, shift, p2));
		imposerConfig.addFigure(new Line(c, p1, c, p2));
		imposerConfig.addFigure(new Line(WIDTH - shift, p1, WIDTH - shift, p2));
		// Horizontal lines
		p1 = MARGIN_X / 2;
		p2 = MARGIN_X + 2 * MM_TO_PX;
		shift = MARGIN_Y + 5 * MM_TO_PX;
		float c1 = HEIGHT / 2 - 2 * MM_TO_PX;
		float c2 = HEIGHT / 2 + 2 * MM_TO_PX;
		imposerConfig.addFigure(new Line(p1, shift, p2, shift));
		imposerConfig.addFigure(new Line(p1, c1, p2, c1));
		imposerConfig.addFigure(new Line(p1, c2, p2, c2));
		imposerConfig.addFigure(new Line(p1, HEIGHT - shift, p2, HEIGHT - shift));
		imposerConfig.addFigure(new Line(WIDTH - p1, shift, WIDTH - p2, shift));
		imposerConfig.addFigure(new Line(WIDTH - p1, c1, WIDTH - p2, c1));
		imposerConfig.addFigure(new Line(WIDTH - p1, c2, WIDTH - p2, c2));
		imposerConfig.addFigure(new Line(WIDTH - p1, HEIGHT - shift, WIDTH - p2, HEIGHT - shift));
		return imposerConfig;
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines010402(float MARGIN_X, float MARGIN_Y, float GAB_X, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH, float MARKER_LENGTH, float MARKER_MARGIN) {
		float center_w = WIDTH / 2;
		float center_h = HEIGHT / 2;
		float MARKER_SPACE = 5 * MM_TO_PX;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		// Vertical lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X + MARKER_SPACE, MARGIN_Y - MARKER_MARGIN, MARGIN_X
				+ MARKER_SPACE, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE, MARGIN_Y - MARKER_MARGIN,
				WIDTH - MARGIN_X - MARKER_SPACE, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Center top left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center top right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center bottom left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				- 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Center bottom right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				+ 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Horizontal lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_SPACE,
				MARGIN_X - MARKER_MARGIN + MARKER_LENGTH, HEIGHT - MARGIN_Y - MARKER_SPACE));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_SPACE, MARGIN_X
				- MARKER_MARGIN + MARKER_LENGTH, MARGIN_Y + MARKER_SPACE));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
				- MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_SPACE,
				WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, MARGIN_Y + MARKER_SPACE));
		// Center left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, center_h - 2 * MM_TO_PX, MARGIN_X
				- MARKER_MARGIN + MARKER_LENGTH, center_h - 2 * MM_TO_PX));
		// Center left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, center_h + 2 * MM_TO_PX, MARGIN_X
				- MARKER_MARGIN + MARKER_LENGTH, center_h + 2 * MM_TO_PX));
		// Center right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, center_h - 2 * MM_TO_PX,
				WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, center_h - 2 * MM_TO_PX));
		// Center right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, center_h + 2 * MM_TO_PX,
				WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, center_h + 2 * MM_TO_PX));
		return imposerConfig;
	}

	/** Impose album type 010802
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010802(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010802 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010802 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010802 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010802 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010802 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010802 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010802 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010802 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010802 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = order2 == null ? 4 : 2;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010999(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010803
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010803(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010803 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010803 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010803 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010803 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010803 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010803 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010803 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010803 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010803 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = order2 == null ? 2 : 1;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X, WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010803 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			float p1 = MARGIN_Y - MARKER_LENGTH + MARKER_GAP;
			float p2 = MARGIN_Y + MARKER_GAP;
			float shift = MARGIN_X + MARKER_MARGIN;
			float centerSheet = WIDTH / 2;
			float h = HEIGHT / 2;
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN - GAB_X, HEIGHT - p2, shift
					+ MARKER_MARGIN - GAB_X,
					HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p2,
					centerSheet
							- (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT - p2, centerSheet, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p2,
					centerSheet
							+ (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN + GAB_X, HEIGHT - p2, WIDTH
					- shift
					- MARKER_MARGIN + GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN - GAB_X, p1, shift + MARKER_MARGIN
					- GAB_X, p2));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, p1, centerSheet
					- (BLOCK_WIDTH / 2)
					+ GAB_X, p2));
			imposerConfig.addFigure(new Line(centerSheet, p1, centerSheet, p2));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, p1, centerSheet
					+ (BLOCK_WIDTH / 2)
					- GAB_X, p2));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN + GAB_X, p1,
					WIDTH - shift - MARKER_MARGIN + GAB_X, p2));

			imposerConfig.addFigure(new Line(shift + MARKER_GAP - (MARKER_LENGTH / 2) - GAB_X, h
					- MARKER_LENGTH, shift
					+ MARKER_GAP - (MARKER_LENGTH / 2) - GAB_X, h + MARKER_LENGTH));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_GAP + (MARKER_LENGTH / 2) + GAB_X, h
					- MARKER_LENGTH,
					WIDTH - shift - MARKER_GAP + (MARKER_LENGTH / 2) + GAB_X, h + MARKER_LENGTH));
			// Horizontal lines
			p1 = MARGIN_X - MARKER_LENGTH + MARKER_GAP;
			p2 = MARGIN_X + MARKER_GAP;
			shift = MARGIN_Y + MARKER_MARGIN;
			float c1 = HEIGHT / 2;
			imposerConfig.addFigure(new Line(p1, shift, p2, shift));
			imposerConfig.addFigure(new Line(p1, c1, p2, c1));
			imposerConfig.addFigure(new Line(p1, HEIGHT - shift, p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, shift, WIDTH - p2, shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, c1, WIDTH - p2, c1));
			imposerConfig.addFigure(new Line(WIDTH - p1, HEIGHT - shift, WIDTH - p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2)
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2) + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_GAP
					+ (MARKER_LENGTH / 2), centerSheet + MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + MARKER_GAP
					- (MARKER_LENGTH / 2),
					centerSheet + MARKER_LENGTH, MARGIN_Y + MARKER_GAP - (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP, h,
					circleRadius)); // left
			imposerConfig.addFigure(new Circle(WIDTH - MARGIN_X - MARKER_GAP + (MARKER_LENGTH / 2), h,
					circleRadius)); // right
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // left top
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // left bottom
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // right top
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // right bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010805
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010805(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010805 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010805 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010805 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010805 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010805 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010805 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010805 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010805 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010805 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010805 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			float p1 = MARGIN_Y - MARKER_LENGTH + MARKER_GAP;
			float p2 = MARGIN_Y + MARKER_GAP;
			float shift = MARGIN_X + MARKER_MARGIN;
			float centerSheet = WIDTH / 2;
			float h = HEIGHT / 2;
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN, HEIGHT - p2, shift + MARKER_MARGIN,
					HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p2,
					centerSheet
							- (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT - p2, centerSheet, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p2,
					centerSheet
							+ (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN, HEIGHT - p2, WIDTH - shift
					- MARKER_MARGIN,
					HEIGHT - p1));
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN, p1, shift + MARKER_MARGIN, p2));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, p1, centerSheet
					- (BLOCK_WIDTH / 2)
					+ GAB_X, p2));
			imposerConfig.addFigure(new Line(centerSheet, p1, centerSheet, p2));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, p1, centerSheet
					+ (BLOCK_WIDTH / 2)
					- GAB_X, p2));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN, p1, WIDTH - shift
					- MARKER_MARGIN, p2));

			imposerConfig.addFigure(new Line(shift + MARKER_GAP - (MARKER_LENGTH / 2), h - MARKER_LENGTH,
					shift + MARKER_GAP
							- (MARKER_LENGTH / 2), h + MARKER_LENGTH));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_GAP + (MARKER_LENGTH / 2), h
					- MARKER_LENGTH, WIDTH
					- shift - MARKER_GAP + (MARKER_LENGTH / 2), h + MARKER_LENGTH));
			// Horizontal lines
			p1 = MARGIN_X + GAB_X - MARKER_LENGTH + MARKER_GAP;
			p2 = MARGIN_X + GAB_X + MARKER_GAP;
			shift = MARGIN_Y + MARKER_MARGIN;
			float c1 = HEIGHT / 2;
			imposerConfig.addFigure(new Line(p1, shift, p2, shift));
			imposerConfig.addFigure(new Line(p1, c1, p2, c1));
			imposerConfig.addFigure(new Line(p1, HEIGHT - shift, p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, shift, WIDTH - p2, shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, c1, WIDTH - p2, c1));
			imposerConfig.addFigure(new Line(WIDTH - p1, HEIGHT - shift, WIDTH - p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2)
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2) + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_GAP
					+ (MARKER_LENGTH / 2), centerSheet + MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + MARKER_GAP
					- (MARKER_LENGTH / 2),
					centerSheet + MARKER_LENGTH, MARGIN_Y + MARKER_GAP - (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(MARGIN_X + GAB_X - (MARKER_LENGTH / 2) + MARKER_GAP, h,
					circleRadius)); // left
			imposerConfig.addFigure(new Circle(WIDTH - MARGIN_X - GAB_X - MARKER_GAP
					+ (MARKER_LENGTH / 2), h, circleRadius)); // right
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // left top
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // left bottom
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // right top
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // right bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			int maxPage = pageCount;
			int minPage = 1;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (minPage % 2 == 1) {
					pageNumbers.add(maxPage);
					pageNumbers.add(minPage);
				}
				else {
					pageNumbers.add(minPage);
					pageNumbers.add(maxPage);
				}
				minPage += 1;
				maxPage -= 1;
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010807
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010807(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010807 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010807 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010807 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010807 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010807 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010807 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010807 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010807 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X, WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010807 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			float p1 = MARGIN_Y - MARKER_LENGTH + MARKER_GAP;
			float p2 = MARGIN_Y + MARKER_GAP;
			float shift = MARGIN_X + MARKER_MARGIN;
			float centerSheet = WIDTH / 2;
			float h = HEIGHT / 2;
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN - GAB_X, HEIGHT - p2, shift
					+ MARKER_MARGIN - GAB_X,
					HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p2,
					centerSheet
							- (BLOCK_WIDTH / 2) + GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT - p2, centerSheet, HEIGHT - p1));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p2,
					centerSheet
							+ (BLOCK_WIDTH / 2) - GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN + GAB_X, HEIGHT - p2, WIDTH
					- shift
					- MARKER_MARGIN + GAB_X, HEIGHT - p1));
			imposerConfig.addFigure(new Line(shift + MARKER_MARGIN - GAB_X, p1, shift + MARKER_MARGIN
					- GAB_X, p2));
			imposerConfig.addFigure(new Line(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, p1, centerSheet
					- (BLOCK_WIDTH / 2)
					+ GAB_X, p2));
			imposerConfig.addFigure(new Line(centerSheet, p1, centerSheet, p2));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, p1, centerSheet
					+ (BLOCK_WIDTH / 2)
					- GAB_X, p2));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_MARGIN + GAB_X, p1,
					WIDTH - shift - MARKER_MARGIN + GAB_X, p2));

			imposerConfig.addFigure(new Line(shift + MARKER_GAP - (MARKER_LENGTH / 2) - GAB_X, h
					- MARKER_LENGTH, shift
					+ MARKER_GAP - (MARKER_LENGTH / 2) - GAB_X, h + MARKER_LENGTH));
			imposerConfig.addFigure(new Line(WIDTH - shift - MARKER_GAP + (MARKER_LENGTH / 2) + GAB_X, h
					- MARKER_LENGTH,
					WIDTH - shift - MARKER_GAP + (MARKER_LENGTH / 2) + GAB_X, h + MARKER_LENGTH));
			// Horizontal lines
			p1 = MARGIN_X - MARKER_LENGTH + MARKER_GAP;
			p2 = MARGIN_X + MARKER_GAP;
			shift = MARGIN_Y + MARKER_MARGIN;
			float c1 = HEIGHT / 2;
			imposerConfig.addFigure(new Line(p1, shift, p2, shift));
			imposerConfig.addFigure(new Line(p1, c1, p2, c1));
			imposerConfig.addFigure(new Line(p1, HEIGHT - shift, p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, shift, WIDTH - p2, shift));
			imposerConfig.addFigure(new Line(WIDTH - p1, c1, WIDTH - p2, c1));
			imposerConfig.addFigure(new Line(WIDTH - p1, HEIGHT - shift, WIDTH - p2, HEIGHT - shift));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2)
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + GAB_X - (BLOCK_WIDTH / 2) - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + GAB_X - (BLOCK_WIDTH / 2) + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					- MARKER_GAP
					+ (MARKER_LENGTH / 2), centerSheet + MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + MARKER_GAP
					- (MARKER_LENGTH / 2),
					centerSheet + MARKER_LENGTH, MARGIN_Y + MARKER_GAP - (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + BLOCK_HEIGHT
							- MARKER_GAP + (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X
					+ MARKER_LENGTH, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP + (MARKER_LENGTH / 2)));
			imposerConfig.addFigure(new Line(centerSheet + (BLOCK_WIDTH / 2) - GAB_X - MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2), centerSheet + (BLOCK_WIDTH / 2) - GAB_X + MARKER_LENGTH,
					MARGIN_Y + MARKER_GAP
							- (MARKER_LENGTH / 2)));
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP, h,
					circleRadius)); // left
			imposerConfig.addFigure(new Circle(WIDTH - MARGIN_X - MARKER_GAP + (MARKER_LENGTH / 2), h,
					circleRadius)); // right
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // left top
			imposerConfig.addFigure(new Circle(centerSheet - (BLOCK_WIDTH / 2) + GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // left bottom
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ BLOCK_HEIGHT - MARKER_GAP
					+ (MARKER_LENGTH / 2), circleRadius)); // right top
			imposerConfig.addFigure(new Circle(centerSheet + (BLOCK_WIDTH / 2) - GAB_X, MARGIN_Y
					+ MARKER_GAP
					- (MARKER_LENGTH / 2), circleRadius)); // right bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010701
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010701(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010701 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010701 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010701 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010701 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010701 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010701 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010701 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010701 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010701 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010701 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010702
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010702(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010702 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010702 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010702 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010702 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010702 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010702 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010702 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010702 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010702 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010702 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010703
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010703(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010703 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010703 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010703 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010703 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010703 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010703 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010703 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010703 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010703 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010703 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010704
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010704(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010704 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010704 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010704 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010704 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010704 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010704 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010704 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010704 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010704 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010704 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010706
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010706(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010706 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010706 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010706 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010706 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010706 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010706 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010706 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010706 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010706 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010706 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010705
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010705(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010705 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010705 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010705 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010705 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010705 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010705 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010705 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010705 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010401 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X, WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010707(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		} catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010707
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010707(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010707 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010707 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010707 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010707 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010707 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010707 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010707 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010707 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010401 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X, WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010707(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines010707(float MARGIN_X, float MARGIN_Y, float GAB_X, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH, float MARKER_LENGTH, float MARKER_MARGIN) {
		float center_w = WIDTH / 2;
		float MARKER_SPACE = 5 * MM_TO_PX;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		// Vertical lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X + MARKER_SPACE, MARGIN_Y - MARKER_MARGIN, MARGIN_X
				+ MARKER_SPACE, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE, MARGIN_Y - MARKER_MARGIN,
				WIDTH - MARGIN_X - MARKER_SPACE, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Center top left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center top right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center bottom left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				- 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Center bottom right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				+ 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Horizontal lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_SPACE,
				MARGIN_X - MARKER_MARGIN + MARKER_LENGTH, HEIGHT - MARGIN_Y - MARKER_SPACE));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_SPACE, MARGIN_X
				- MARKER_MARGIN + MARKER_LENGTH, MARGIN_Y + MARKER_SPACE));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
				- MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_SPACE,
				WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH, MARGIN_Y + MARKER_SPACE));
		return imposerConfig;
	}

	/** Impose album type 010708
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010708(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010708 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010708 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010708 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010708 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010708 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010708 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010708 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010708 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010708 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010708 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			int pageNum = 1;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(pageNum);
				pageNum += 1;
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010709
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010709(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010709 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010709 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010709 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010709 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010709 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010709 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010709 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010709 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010709 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			float centerSheet = WIDTH / 2;
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010709 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			imposerConfig.addFigure(new Line(MARGIN_X - MARKER_GAP, (HEIGHT / 2) - MARKER_LENGTH,
					MARGIN_X - MARKER_GAP,
					(HEIGHT / 2) + MARKER_LENGTH)); // left center
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2)
					- MARKER_LENGTH, MARGIN_X
					+ BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2) + MARKER_LENGTH)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT + MARKER_GAP - MARGIN_Y
					- (MARKER_LENGTH / 2), centerSheet,
					HEIGHT + MARKER_GAP - MARGIN_Y + (MARKER_LENGTH / 2))); // top center
			imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_GAP + (MARKER_LENGTH / 2),
					centerSheet, MARGIN_Y
					- MARKER_GAP - (MARKER_LENGTH / 2))); // bottom center
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2,
					MARGIN_X
							+ (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2)); // left center
			imposerConfig.addFigure(new Line(BLOCK_WIDTH + MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP,
					HEIGHT / 2,
					BLOCK_WIDTH + MARGIN_X + (MARKER_LENGTH / 2) + MARKER_GAP, HEIGHT / 2)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y - MARKER_GAP,
					centerSheet + MARKER_LENGTH,
					MARGIN_Y - MARKER_GAP)); // center bottom
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					+ MARKER_GAP, centerSheet
					+ MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT + MARKER_GAP)); // center top
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y - MARKER_GAP, circleRadius)); // bottom
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y + MARKER_GAP + BLOCK_HEIGHT,
					circleRadius)); // top
			imposerConfig.addFigure(new Circle(MARGIN_X - MARKER_GAP, HEIGHT / 2, circleRadius)); // left
			imposerConfig.addFigure(new Circle(MARGIN_X + MARKER_GAP + BLOCK_WIDTH, HEIGHT / 2,
					circleRadius)); // right
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010710
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010710(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010710 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010710 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010710 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010710 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010710 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010710 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010710 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010710 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010710 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			float centerSheet = WIDTH / 2;
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010710 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			imposerConfig.addFigure(new Line(MARGIN_X - MARKER_GAP, (HEIGHT / 2) - MARKER_LENGTH,
					MARGIN_X - MARKER_GAP,
					(HEIGHT / 2) + MARKER_LENGTH)); // left center
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2)
					- MARKER_LENGTH, MARGIN_X
					+ BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2) + MARKER_LENGTH)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT + MARKER_GAP - MARGIN_Y
					- (MARKER_LENGTH / 2), centerSheet,
					HEIGHT + MARKER_GAP - MARGIN_Y + (MARKER_LENGTH / 2))); // top center
			imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_GAP + (MARKER_LENGTH / 2),
					centerSheet, MARGIN_Y
					- MARKER_GAP - (MARKER_LENGTH / 2))); // bottom center
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2,
					MARGIN_X
							+ (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2)); // left center
			imposerConfig.addFigure(new Line(BLOCK_WIDTH + MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP,
					HEIGHT / 2,
					BLOCK_WIDTH + MARGIN_X + (MARKER_LENGTH / 2) + MARKER_GAP, HEIGHT / 2)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y - MARKER_GAP,
					centerSheet + MARKER_LENGTH,
					MARGIN_Y - MARKER_GAP)); // center bottom
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					+ MARKER_GAP, centerSheet
					+ MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT + MARKER_GAP)); // center top
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y - MARKER_GAP, circleRadius)); // bottom
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y + MARKER_GAP + BLOCK_HEIGHT,
					circleRadius)); // top
			imposerConfig.addFigure(new Circle(MARGIN_X - MARKER_GAP, HEIGHT / 2, circleRadius)); // left
			imposerConfig.addFigure(new Circle(MARGIN_X + MARKER_GAP + BLOCK_WIDTH, HEIGHT / 2,
					circleRadius)); // right
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010401
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010401(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010401 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010401 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010401 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010401 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010401 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010401 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010401 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010401 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010401 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010401 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010402
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010402(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010402 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010402 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010402 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010402 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010402 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010402 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010402 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010402 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010402 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010402 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010403
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010403(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010403 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010403 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010403 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010403 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010403 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010403 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010403 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010403 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010403 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010403 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010404
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010404(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010404 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010404 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010404 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010404 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010404 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010404 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010404 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010404 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010404 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010404 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010405
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010405(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010405 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010405 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010405 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010405 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010405 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010405 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010405 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010405 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010405 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010701 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010405(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines010405(float MARGIN_X, float MARGIN_Y, float GAB_X, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH, float MARKER_LENGTH, float MARKER_MARGIN) {
		float center_w = WIDTH / 2;
		float MARKER_SPACE = 5 * MM_TO_PX;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		// Vertical lines
		// Left top
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, GAB_X + MARGIN_X + MARKER_SPACE, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Left bottom
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE, MARGIN_Y - MARKER_MARGIN,
				GAB_X + MARGIN_X + MARKER_SPACE, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X, MARGIN_Y
				- MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X, MARGIN_Y - MARKER_MARGIN
				+ MARKER_LENGTH));
		// Center top left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w - 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center top right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN,
				center_w + 2 * MM_TO_PX, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH));
		// Center bottom left
		imposerConfig.addFigure(new Line(center_w - 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				- 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Center bottom right
		imposerConfig.addFigure(new Line(center_w + 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN, center_w
				+ 2 * MM_TO_PX, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Horizontal lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X, HEIGHT - MARGIN_Y
				- MARKER_SPACE, MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X, MARGIN_Y + MARKER_SPACE,
				MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X, MARGIN_Y + MARKER_SPACE));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X, HEIGHT - MARGIN_Y
				- MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X, MARGIN_Y
				+ MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X, MARGIN_Y
				+ MARKER_SPACE));
		return imposerConfig;
	}

	/** Impose album type 010406
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010406(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010406 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010406 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010406 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010406 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010406 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010406 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010406 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010406 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010406 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010406 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			int topPage = 1;
			int bottomPage = isGroupImpose ? 2 : (sheetCount + 1);
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(topPage);
				pageNumbers.add(bottomPage);
				bottomPage += isGroupImpose ? 2 : 1;
				topPage += isGroupImpose ? 2 : 1;
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}
	
	private List<Integer> getPageNumbersPanoramic(final int sheetCount) {
		int pageCount = order.getPageCount();
		if (isGroupImpose) {
			return getPageNumbersAlbums(sheetCount);
		}
		int maxPage = pageCount * (isGroupImpose ? 2 : 1);
		int minPage = 1;
		List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
		for (int index = 0; index < sheetCount * (isGroupImpose ? 2 : 1); index++) {
			if (minPage % 2 == 1) {
				pageNumbers.add(maxPage);
				pageNumbers.add(minPage);
			}
			else {
				pageNumbers.add(minPage);
				pageNumbers.add(maxPage);
			}
			minPage += isGroupImpose ? 2 : 1;
			maxPage -= isGroupImpose ? 2 : 1;
		}
		return pageNumbers;
	}
	
	private List<Integer> getPageNumbersAlbums(final int sheetCount) {
		int pageCount = order.getPageCount();
		int topPage = 1;
		int bottomPage = isGroupImpose ? 2 : (sheetCount + 1);
		List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
		for (int index = 0; index < sheetCount; index++) {
			pageNumbers.add(topPage);
			pageNumbers.add(bottomPage);
			bottomPage += isGroupImpose ? 2 : 1;
			topPage += isGroupImpose ? 2 : 1;
		}
		return pageNumbers;
	}

	private List<Integer> getPageNumbersTablet(final int sheetCount) {
		int pageCount = order.getPageCount();
//		if (isGroupImpose) {
//			return getPageNumbersAlbums(sheetCount);
//		}
		//int maxPage = pageCount * (isGroupImpose ? 2 : 1);
		int maxPage = pageCount;
		int minPage = 1;
		List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
//		for (int index = 0; index < sheetCount * (isGroupImpose ? 2 : 1); index++) {
		for (int index = 0; index < sheetCount; index++) {
			pageNumbers.add(minPage++);
			pageNumbers.add(minPage++);
		}
		return pageNumbers;
	}

	/** Impose album type 010407
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010407(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010407 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010407 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010407 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010407 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010407 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010407 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010407 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010407 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010407 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010407 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010405(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010408
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010408(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010408 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010408 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010408 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010408 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010408 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010408 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010408 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010408 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010408 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010408 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			int pageNum = 1;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(pageNum);
				pageNum += 1;
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010409
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010409(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010409 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010409 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010409 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010409 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010409 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010409 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010409 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010409 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010409 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			float centerSheet = WIDTH / 2;
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010409 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			imposerConfig.addFigure(new Line(MARGIN_X - MARKER_GAP, (HEIGHT / 2) - MARKER_LENGTH,
					MARGIN_X - MARKER_GAP,
					(HEIGHT / 2) + MARKER_LENGTH)); // left center
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2)
					- MARKER_LENGTH, MARGIN_X
					+ BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2) + MARKER_LENGTH)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT + MARKER_GAP - MARGIN_Y
					- (MARKER_LENGTH / 2), centerSheet,
					HEIGHT + MARKER_GAP - MARGIN_Y + (MARKER_LENGTH / 2))); // top center
			imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_GAP + (MARKER_LENGTH / 2),
					centerSheet, MARGIN_Y
					- MARKER_GAP - (MARKER_LENGTH / 2))); // bottom center
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2,
					MARGIN_X
							+ (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2)); // left center
			imposerConfig.addFigure(new Line(BLOCK_WIDTH + MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP,
					HEIGHT / 2,
					BLOCK_WIDTH + MARGIN_X + (MARKER_LENGTH / 2) + MARKER_GAP, HEIGHT / 2)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y - MARKER_GAP,
					centerSheet + MARKER_LENGTH,
					MARGIN_Y - MARKER_GAP)); // center bottom
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					+ MARKER_GAP, centerSheet
					+ MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT + MARKER_GAP)); // center top
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y - MARKER_GAP, circleRadius)); // bottom
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y + MARKER_GAP + BLOCK_HEIGHT,
					circleRadius)); // top
			imposerConfig.addFigure(new Circle(MARGIN_X - MARKER_GAP, HEIGHT / 2, circleRadius)); // left
			imposerConfig.addFigure(new Circle(MARGIN_X + MARKER_GAP + BLOCK_WIDTH, HEIGHT / 2,
					circleRadius)); // right
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010501
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010501(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010501 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010501 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010501 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010501 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010501 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010501 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010501 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010501 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010501 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010501 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010502
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010502(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010502 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010502 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010502 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010502 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010502 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010502 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010502 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010502 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010502 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010502 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 4;
		final Float[] gabsx = new Float[] { GAB_X, 0f, 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f, 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH - GAB_X, BLOCK_WIDTH - GAB_X,
				BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT - GAB_Y, BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y,
				BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X + GAB_X,
				MARGIN_X,
				WIDTH - BLOCK_WIDTH - MARGIN_X };
		final Float[] y = new Float[] { HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				HEIGHT - BLOCK_HEIGHT - MARGIN_Y + GAB_Y,
				MARGIN_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 180, 180, 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010402(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / PAGES_ON_SHEET;
			final int FIRST_PAGE = 1;
			final Integer[] temp = new Integer[] { FIRST_PAGE, pageCount, PAGES_ON_SHEET,
					pageCount - (PAGES_ON_SHEET - FIRST_PAGE) };
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				if (index % 2 == 0) {
					pageNumbers.addAll(Arrays.asList(temp));
				}
				else {
					pageNumbers.addAll(Arrays.asList(temp[1] - FIRST_PAGE, temp[0] + FIRST_PAGE, temp[3]
							+ FIRST_PAGE, temp[2]
							- FIRST_PAGE));
					temp[0] += PAGES_ON_SHEET;
					temp[1] -= PAGES_ON_SHEET;
					temp[2] += PAGES_ON_SHEET;
					temp[3] -= PAGES_ON_SHEET;
				}
			}
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010503
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010503(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010503 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010503 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010503 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010503 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010503 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010503 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010503 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010503 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010503 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010503 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010504
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010504(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010504 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010504 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010504 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010504 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010504 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010504 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010504 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010504 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010504 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010504 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010505
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010505(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010505 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010505 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010505 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010505 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010505 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010505 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010505 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010505 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010505 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010505 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010405(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010506
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010506(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010506 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010506 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010506 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010506 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010506 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010506 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010506 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010506 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010506 * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, 0f };
		final Float[] gabsy = new Float[] { GAB_Y, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT - GAB_Y };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, MARGIN_X + GAB_X };
		final Float[] y = new Float[] { HEIGHT / 2 - GAB_Y, MARGIN_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010506 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			final float CENTER_MARKER_MARGIN = 2f * PdfConst.MM_TO_PX;
			final float h = HEIGHT / 2;
			final float shiftX = MARGIN_X + GAB_X;
			final float shiftY = MARGIN_Y;
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX
					+ MARKER_MARGIN, HEIGHT - shiftY - MARKER_GAP)); // top left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					+ (MARKER_LENGTH - MARKER_GAP), shiftX + BLOCK_WIDTH - MARKER_MARGIN, HEIGHT - shiftY
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(shiftX + MARKER_MARGIN, shiftY + MARKER_GAP, shiftX
					+ MARKER_MARGIN, shiftY
					- (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(shiftX + BLOCK_WIDTH - MARKER_MARGIN, shiftY + MARKER_GAP,
					shiftX + BLOCK_WIDTH
							- MARKER_MARGIN, shiftY - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN, shiftX
					+ MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					+ CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h + CENTER_MARKER_MARGIN)); // left center top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), h
					- CENTER_MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, h - CENTER_MARKER_MARGIN)); // left center bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h + CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h + CENTER_MARKER_MARGIN)); // right
// center top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, h - CENTER_MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), h - CENTER_MARKER_MARGIN)); // right
// center bottom
			imposerConfig.addFigure(new Line(shiftX - (MARKER_LENGTH - MARKER_GAP), shiftY
					+ MARKER_MARGIN, shiftX
					+ MARKER_GAP, shiftY + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, shiftY + MARKER_MARGIN, WIDTH
					- MARGIN_X
					+ (MARKER_LENGTH - MARKER_GAP), shiftY + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersAlbums(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010507
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010507(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010507 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010507 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010507 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010507 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010507 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010507 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010507 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y,
				HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerConfig imposerConfig = drawLines010405(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
					LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			//
			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersPanoramic(sheetCount);
			processPages010999(sheetCount, PAGES_ON_SHEET, gabsx, gabsy, blockWidth, blockHeight, x, y,
					pageRotations,
					pageNumbers, document, pdfReader, pdfWriter, imposerConfig);
		} catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 011106
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	// TODO refactor
	public void impose011103(String nonImposedPath, String path) {
		final float WIDTH = 488 * PdfConst.MM_TO_PX;
		final float HEIGHT = 330 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_011103 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_011103 * PdfConst.MM_TO_PX;
		final float MARGIN_X = WIDTH / 2 - BLOCK_WIDTH; // 20 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = (HEIGHT - BLOCK_HEIGHT) / 2; //Imposition.MARGIN_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_X = 5 * PdfConst.MM_TO_PX; //Imposition.GAB_X_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_011103 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_011103 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_011103 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { MARGIN_Y + GAB_Y, HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			BaseFont font = BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			document.open();

			ImposerConfig imposerConfig = drawLines011106(MARGIN_X, MARGIN_Y, GAB_X, GAB_Y, WIDTH, HEIGHT,
				LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			imposerConfig.setShowFormatColorScheme(false);

			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersTablet(sheetCount); // getPageNumbersPanoramic(sheetCount);

			ImposerPage imposerPage;
			ImposerSheet imposerSheet;
			ImposerPageConfig imposerPageConfig;
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {
					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(gabsx[page]);
					imposerPageConfig.setGaby(gabsy[page]);
					imposerPageConfig.setBlockWidth(blockWidth[page]);
					imposerPageConfig.setBlockHeight(blockHeight[page]);
					imposerPageConfig.setX(x[page]);
					imposerPageConfig.setY(y[page]);
					imposerPage = new ImposerPage(pageRotations[page], pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);

				// TODO refactor start
				PdfContentByte cb = pdfWriter.getDirectContent();

				BaseColor color = BaseColor.GRAY;
				String lamText = null;
				int pageLam = order.getPageLamination();
				if (pageLam == PageLamination.GLOSSY) {
					lamText = "ГЛЯНЦ";
					color = BaseColor.BLUE;
				} else if (pageLam == PageLamination.MATT) {
					lamText = "МАТ";
					color = BaseColor.RED;
				} else if (pageLam == PageLamination.SAND) {
					lamText = "ПЕСОК";
					color = BaseColor.GREEN;
				}

				final float MM_10 = 10 * PdfConst.MM_TO_PX;
				float MARKER_SPACE = 5 * MM_TO_PX;
				//float rectWidth = 35 * PdfConst.MM_TO_PX;
				float rectHeight = ( BLOCK_HEIGHT - MM_10 ) / sheetCount;

				float llx = WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10 + GAB_X;
				float lly = (HEIGHT - MARGIN_Y - MARKER_SPACE + GAB_Y) - sheetIndex * rectHeight;
				float urx = WIDTH;
				float ury = lly - rectHeight;

				Rectangle rectangle = new Rectangle(llx, lly,  urx, ury);
				rectangle.setBackgroundColor(color);
				cb.rectangle(rectangle);
				cb.stroke();

				float textMargin = 1.5f * MM_TO_PX;
				String text = String.format("%s разв %d/%d", order.getNumber(), sheetIndex + 1, sheetCount);
				float max_font_size = getMaxFontSize(font, text, (int) rectHeight, (int) rectHeight);
				float textHeight = font.getAscentPoint(text, max_font_size) - font.getDescentPoint(text, max_font_size);

				cb.beginText();
				cb.setFontAndSize(font, max_font_size);
				cb.setColorFill(BaseColor.WHITE);
				if (lamText != null) {
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, lamText, llx + textMargin + textHeight, lly - rectHeight / 2, -90);
				}
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, llx + textMargin, lly - rectHeight / 2, -90);
				cb.endText();

				// TODO refactor end

				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		} catch (Exception e) {
			Exceptions.rethrow(e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}

			if (document != null) {
				document.close();
			}
		}
	}

	private static float getMaxFontSize(BaseFont bf, String text, int width, int height) {
		// avoid infinite loop when text is empty
		if(StringUtils.isBlank(text)){
			return 0.0f;
		}

		float fontSize = 0.1f;
		while(bf.getWidthPoint(text, fontSize) < width){
			fontSize += 0.1f;
		}

		float maxHeight = measureHeight(bf, text, fontSize);
		while(maxHeight > height){
			fontSize -= 0.1f;
			maxHeight = measureHeight(bf, text, fontSize);
		};

		return fontSize;
	}

	public static  float measureHeight(BaseFont baseFont, String text, float fontSize) {
		float ascend = baseFont.getAscentPoint(text, fontSize);
		float descend = baseFont.getDescentPoint(text, fontSize);
		return ascend - descend;
	}

	/** Impose album type 011106
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	// TODO refactor
	public void impose011104(String nonImposedPath, String path) {
		final float WIDTH = 488 * PdfConst.MM_TO_PX;
		final float HEIGHT = 330 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_011104 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_011104 * PdfConst.MM_TO_PX;
		final float MARGIN_X = WIDTH / 2 - BLOCK_WIDTH; // 20 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = (HEIGHT - BLOCK_HEIGHT) / 2; //Imposition.MARGIN_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_X = 5 * PdfConst.MM_TO_PX; //Imposition.GAB_X_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_011104 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_011104 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_011104 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { MARGIN_Y + GAB_Y, HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			BaseFont font = BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			document.open();

			ImposerConfig imposerConfig = drawLines011106(MARGIN_X, MARGIN_Y, GAB_X, GAB_Y, WIDTH, HEIGHT,
				LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			imposerConfig.setShowFormatColorScheme(false);

			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersTablet(sheetCount); // getPageNumbersPanoramic(sheetCount);

			ImposerPage imposerPage;
			ImposerSheet imposerSheet;
			ImposerPageConfig imposerPageConfig;
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {
					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(gabsx[page]);
					imposerPageConfig.setGaby(gabsy[page]);
					imposerPageConfig.setBlockWidth(blockWidth[page]);
					imposerPageConfig.setBlockHeight(blockHeight[page]);
					imposerPageConfig.setX(x[page]);
					imposerPageConfig.setY(y[page]);
					imposerPage = new ImposerPage(pageRotations[page], pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);

				// TODO refactor start
				PdfContentByte cb = pdfWriter.getDirectContent();

				BaseColor color = BaseColor.GRAY;
				String lamText = null;
				int pageLam = order.getPageLamination();
				if (pageLam == PageLamination.GLOSSY) {
					lamText = "ГЛЯНЦ";
					color = BaseColor.BLUE;
				} else if (pageLam == PageLamination.MATT) {
					lamText = "МАТ";
					color = BaseColor.RED;
				} else if (pageLam == PageLamination.SAND) {
					lamText = "ПЕСОК";
					color = BaseColor.GREEN;
				}

				final float MM_10 = 10 * PdfConst.MM_TO_PX;
				float MARKER_SPACE = 5 * MM_TO_PX;
				//float rectWidth = 35 * PdfConst.MM_TO_PX;
				float rectHeight = ( BLOCK_HEIGHT - MM_10 ) / sheetCount;

				float llx = WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10 + GAB_X;
				float lly = (HEIGHT - MARGIN_Y - MARKER_SPACE + GAB_Y) - sheetIndex * rectHeight;
				float urx = WIDTH;
				float ury = lly - rectHeight;

				Rectangle rectangle = new Rectangle(llx, lly,  urx, ury);
				rectangle.setBackgroundColor(color);
				cb.rectangle(rectangle);
				cb.stroke();

				float textMargin = 1.5f * MM_TO_PX;
				String text = String.format("%s разв %d/%d", order.getNumber(), sheetIndex + 1, sheetCount);
				float max_font_size = getMaxFontSize(font, text, (int) rectHeight, (int) rectHeight);
				float textHeight = font.getAscentPoint(text, max_font_size) - font.getDescentPoint(text, max_font_size);

				cb.beginText();
				cb.setFontAndSize(font, max_font_size);
				cb.setColorFill(BaseColor.WHITE);
				if (lamText != null) {
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, lamText, llx + textMargin + textHeight, lly - rectHeight / 2, -90);
				}
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, llx + textMargin, lly - rectHeight / 2, -90);
				cb.endText();

				// TODO refactor end

				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		} catch (Exception e) {
			Exceptions.rethrow(e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}

			if (document != null) {
				document.close();
			}
		}
	}

	/** Impose album type 011106
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	// TODO refactor
	public void impose011106(String nonImposedPath, String path) {
		final float WIDTH = 700 * PdfConst.MM_TO_PX;
		final float HEIGHT = 330 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010506 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010506 * PdfConst.MM_TO_PX;
		final float MARGIN_X = WIDTH / 2 - BLOCK_WIDTH; // 20 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = (HEIGHT - BLOCK_HEIGHT) / 2; //Imposition.MARGIN_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_X = 5 * PdfConst.MM_TO_PX; //Imposition.GAB_X_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010506 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010506 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010506 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { MARGIN_Y + GAB_Y, HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			BaseFont font = BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			document.open();

			ImposerConfig imposerConfig = drawLines011106(MARGIN_X, MARGIN_Y, GAB_X, GAB_Y, WIDTH, HEIGHT,
                LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			imposerConfig.setShowFormatColorScheme(false);

			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersTablet(sheetCount); // getPageNumbersPanoramic(sheetCount);

			ImposerPage imposerPage;
			ImposerSheet imposerSheet;
			ImposerPageConfig imposerPageConfig;
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {
					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(gabsx[page]);
					imposerPageConfig.setGaby(gabsy[page]);
					imposerPageConfig.setBlockWidth(blockWidth[page]);
					imposerPageConfig.setBlockHeight(blockHeight[page]);
					imposerPageConfig.setX(x[page]);
					imposerPageConfig.setY(y[page]);
					imposerPage = new ImposerPage(pageRotations[page], pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);

				// TODO refactor start
				PdfContentByte cb = pdfWriter.getDirectContent();

				BaseColor color = BaseColor.GRAY;
				String lamText = null;
				int pageLam = order.getPageLamination();
				if (pageLam == PageLamination.GLOSSY) {
					lamText = "ГЛЯНЦ";
					color = BaseColor.BLUE;
				} else if (pageLam == PageLamination.MATT) {
					lamText = "МАТ";
					color = BaseColor.RED;
				} else if (pageLam == PageLamination.SAND) {
					lamText = "ПЕСОК";
					color = BaseColor.GREEN;
				}

				final float MM_10 = 10 * PdfConst.MM_TO_PX;
				float MARKER_SPACE = 5 * MM_TO_PX;
				//float rectWidth = 35 * PdfConst.MM_TO_PX;
				float rectHeight = ( BLOCK_HEIGHT - MM_10 ) / sheetCount;

				float llx = WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10 + GAB_X;
				float lly = (HEIGHT - MARGIN_Y - MARKER_SPACE + GAB_Y) - sheetIndex * rectHeight;
				float urx = WIDTH;
				float ury = lly - rectHeight;

				Rectangle rectangle = new Rectangle(llx, lly,  urx, ury);
				rectangle.setBackgroundColor(color);
				cb.rectangle(rectangle);
				cb.stroke();

				float textMargin = 1.5f * MM_TO_PX;
				String text = String.format("%s разв %d/%d", order.getNumber(), sheetIndex + 1, sheetCount);
				float max_font_size = getMaxFontSize(font, text, (int) rectHeight, (int) rectHeight);
				float textHeight = font.getAscentPoint(text, max_font_size) - font.getDescentPoint(text, max_font_size);

				cb.beginText();
				cb.setFontAndSize(font, max_font_size);
				cb.setColorFill(BaseColor.WHITE);
				if (lamText != null) {
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, lamText, llx + textMargin + textHeight, lly - rectHeight / 2, -90);
				}
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, llx + textMargin, lly - rectHeight / 2, -90);
				cb.endText();

				// TODO refactor end

				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		} catch (Exception e) {
			Exceptions.rethrow(e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}

			if (document != null) {
				document.close();
			}
		}
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines011106(float MARGIN_X, float MARGIN_Y, float GAB_X, float GAB_Y, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH, float MARKER_LENGTH, float MARKER_MARGIN) {
		float MM_10 = 10 * PdfConst.MM_TO_PX;
		float MARKER_SPACE = 5 * MM_TO_PX;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		// Vertical lines
		// Left top
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE + MM_10, HEIGHT - MARGIN_Y + GAB_Y
			+ MARKER_MARGIN, GAB_X + MARGIN_X + MARKER_SPACE + MM_10, HEIGHT - MARGIN_Y + MARKER_MARGIN + GAB_Y
			- MARKER_LENGTH));
		// Left bottom
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE + MM_10, MARGIN_Y - MARKER_MARGIN + GAB_Y,
			GAB_X + MARGIN_X + MARKER_SPACE + MM_10, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH + GAB_Y));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, HEIGHT - MARGIN_Y + GAB_Y
			+ MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, HEIGHT - MARGIN_Y + MARKER_MARGIN + GAB_Y
			- MARKER_LENGTH));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, MARGIN_Y
			- MARKER_MARGIN + GAB_Y, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, MARGIN_Y - MARKER_MARGIN
			+ MARKER_LENGTH + GAB_Y));

		// Center lines
		float centerSheet = WIDTH / 2;
		// Top
		imposerConfig.addFigure(new Line(centerSheet, HEIGHT - MARGIN_Y + GAB_Y + MARKER_MARGIN,
			centerSheet, HEIGHT - MARGIN_Y + MARKER_MARGIN + GAB_Y - MARKER_LENGTH, BaseColor.RED));
		// Bottom
		imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_MARGIN + GAB_Y,
			centerSheet, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH + GAB_Y, BaseColor.RED));

		// Horizontal lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X + MM_10, HEIGHT - MARGIN_Y + GAB_Y
			- MARKER_SPACE, MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X + MM_10, HEIGHT - MARGIN_Y + GAB_Y
			- MARKER_SPACE));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X + MM_10, MARGIN_Y + MARKER_SPACE + GAB_Y,
			MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X + MM_10, MARGIN_Y + MARKER_SPACE + GAB_Y));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X - MM_10, HEIGHT - MARGIN_Y + GAB_Y
			- MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X - MM_10, HEIGHT - MARGIN_Y
			- MARKER_SPACE + GAB_Y));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X - MM_10, MARGIN_Y + GAB_Y
			+ MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X - MM_10, MARGIN_Y
			+ MARKER_SPACE + GAB_Y));
		return imposerConfig;
	}

	/** Impose album type 011107
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	// TODO refactor
	public void impose011107(String nonImposedPath, String path) {
		final float WIDTH = 488 * PdfConst.MM_TO_PX;
		final float HEIGHT = 330 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010507 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010507 * PdfConst.MM_TO_PX;
		final float MARGIN_X = WIDTH / 2 - BLOCK_WIDTH; // 20 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = 15 * PdfConst.MM_TO_PX; //Imposition.MARGIN_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_X = 5 * PdfConst.MM_TO_PX; //Imposition.GAB_X_MM_010507 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010507 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010507 * PdfConst.MM_TO_PX;
		final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010507 * PdfConst.MM_TO_PX;
		final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;

		final int PAGES_ON_SHEET = 2;
		final Float[] gabsx = new Float[] { 0f, GAB_X };
		final Float[] gabsy = new Float[] { 0f, 0f };
		final Float[] blockWidth = new Float[] { BLOCK_WIDTH - GAB_X, BLOCK_WIDTH };
		final Float[] blockHeight = new Float[] { BLOCK_HEIGHT, BLOCK_HEIGHT };
		final Float[] x = new Float[] { MARGIN_X + GAB_X, WIDTH - BLOCK_WIDTH - MARGIN_X - GAB_X };
		final Float[] y = new Float[] { HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y, HEIGHT - MARGIN_Y - BLOCK_HEIGHT + GAB_Y };
		final Integer[] pageRotations = new Integer[] { 0, 0 };
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			BaseFont font = BaseFont.createFont(FONT_LOCATION, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			document.open();

			ImposerConfig imposerConfig = drawLines011107(MARGIN_X, MARGIN_Y, GAB_X, WIDTH, HEIGHT,
				LINE_WIDTH, MARKER_LENGTH, MARKER_MARGIN);
			imposerConfig.setShowFormatColorScheme(false);

			int pageCount = order.getPageCount();
			final int sheetCount = pageCount / (isGroupImpose ? 1 : PAGES_ON_SHEET);
			List<Integer> pageNumbers = getPageNumbersTablet(sheetCount); // getPageNumbersPanoramic(sheetCount);

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {
					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(gabsx[page]);
					imposerPageConfig.setGaby(gabsy[page]);
					imposerPageConfig.setBlockWidth(blockWidth[page]);
					imposerPageConfig.setBlockHeight(blockHeight[page]);
					imposerPageConfig.setX(x[page]);
					imposerPageConfig.setY(y[page]);
					imposerPage = new ImposerPage(pageRotations[page], pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);

				// TODO refactor start
				PdfContentByte cb = pdfWriter.getDirectContent();

				BaseColor color = BaseColor.GRAY;
				String lamText = null;
				int pageLam = order.getPageLamination();
				if (pageLam == PageLamination.GLOSSY) {
					lamText = "ГЛЯНЦ";
					color = BaseColor.BLUE;
				} else if (pageLam == PageLamination.MATT) {
					lamText = "МАТ";
					color = BaseColor.RED;
				} else if (pageLam == PageLamination.SAND) {
					lamText = "ПЕСОК";
					color = BaseColor.GREEN;
				}

				final float MM_10 = 10 * PdfConst.MM_TO_PX;
				float MARKER_SPACE = 5 * MM_TO_PX;
				//float rectWidth = 35 * PdfConst.MM_TO_PX;
				float rectHeight = ( BLOCK_HEIGHT - MM_10 ) / sheetCount;

				float llx = WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10 + GAB_X;
				float lly = (HEIGHT - MARGIN_Y - MARKER_SPACE) - sheetIndex * rectHeight;
				float urx = WIDTH;
				float ury = lly - rectHeight;

				Rectangle rectangle = new Rectangle(llx, lly,  urx, ury);
				rectangle.setBackgroundColor(color);
				cb.rectangle(rectangle);
				cb.stroke();

				float textMargin = 1.5f * MM_TO_PX;
				String text = String.format("%s разв %d/%d", order.getNumber(), sheetIndex + 1, sheetCount);
				float max_font_size = getMaxFontSize(font, text, (int) rectHeight, (int) rectHeight);
				float textHeight = font.getAscentPoint(text, max_font_size) - font.getDescentPoint(text, max_font_size);

				cb.beginText();
				cb.setFontAndSize(font, max_font_size);
				cb.setColorFill(BaseColor.WHITE);
				if (lamText != null) {
					cb.showTextAligned(PdfContentByte.ALIGN_CENTER, lamText, llx + textMargin + textHeight, lly - rectHeight / 2, -90);
				}
				cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, llx + textMargin, lly - rectHeight / 2, -90);
				cb.endText();

				// TODO refactor end

				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		} catch (Exception e) {
			Exceptions.rethrow(e);
		} finally {
			if (pdfReader != null) {
				pdfReader.close();
			}

			if (document != null) {
				document.close();
			}
		}
	}

	/** @param MARGIN_X
	 * @param MARGIN_Y
	 * @param GAB_X
	 * @param WIDTH
	 * @param HEIGHT
	 * @param LINE_WIDTH
	 * @return */
	public ImposerConfig drawLines011107(float MARGIN_X, float MARGIN_Y, float GAB_X, float WIDTH,
										 float HEIGHT,
										 float LINE_WIDTH, float MARKER_LENGTH, float MARKER_MARGIN) {
		float MM_10 = 10 * PdfConst.MM_TO_PX;
		float MARKER_SPACE = 5 * MM_TO_PX;
		ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
		// Vertical lines
		// Left top
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE + MM_10, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, GAB_X + MARGIN_X + MARKER_SPACE + MM_10, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Left bottom
		imposerConfig.addFigure(new Line(GAB_X + MARGIN_X + MARKER_SPACE + MM_10, MARGIN_Y - MARKER_MARGIN,
				GAB_X + MARGIN_X + MARKER_SPACE + MM_10, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, HEIGHT - MARGIN_Y
				+ MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, HEIGHT - MARGIN_Y + MARKER_MARGIN
				- MARKER_LENGTH));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, MARGIN_Y
				- MARKER_MARGIN, WIDTH - MARGIN_X - MARKER_SPACE - GAB_X - MM_10, MARGIN_Y - MARKER_MARGIN
				+ MARKER_LENGTH));

		// Center lines
		float centerSheet = WIDTH / 2;
		// Top
		imposerConfig.addFigure(new Line(centerSheet, HEIGHT - MARGIN_Y + MARKER_MARGIN,
			centerSheet, HEIGHT - MARGIN_Y + MARKER_MARGIN - MARKER_LENGTH, BaseColor.RED));
		// Bottom
		imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_MARGIN,
			centerSheet, MARGIN_Y - MARKER_MARGIN + MARKER_LENGTH, BaseColor.RED));

		// Horizontal lines
		// Left top
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X + MM_10, HEIGHT - MARGIN_Y
				- MARKER_SPACE, MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X + MM_10, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Left bottom
		imposerConfig.addFigure(new Line(MARGIN_X - MARKER_MARGIN + GAB_X + MM_10, MARGIN_Y + MARKER_SPACE,
				MARGIN_X - MARKER_MARGIN + MARKER_LENGTH + GAB_X + MM_10, MARGIN_Y + MARKER_SPACE));
		// Right top
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X - MM_10, HEIGHT - MARGIN_Y
				- MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X - MM_10, HEIGHT - MARGIN_Y
				- MARKER_SPACE));
		// Right bottom
		imposerConfig.addFigure(new Line(WIDTH - MARGIN_X + MARKER_MARGIN - GAB_X - MM_10, MARGIN_Y
				+ MARKER_SPACE, WIDTH - MARGIN_X + MARKER_MARGIN - MARKER_LENGTH - GAB_X - MM_10, MARGIN_Y
				+ MARKER_SPACE));
		return imposerConfig;
	}

	/** Impose album type 010508
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010508(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010508 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010508 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010508 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010508 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010508 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010508 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010508 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010508 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010508 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010508 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			int pageNum = 1;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(pageNum);
				pageNum += 1;
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010509
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010509(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010509 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010509 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010509 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010509 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010509 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010509 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010509 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010509 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010509 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			float centerSheet = WIDTH / 2;
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010509 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			imposerConfig.addFigure(new Line(MARGIN_X - MARKER_GAP, (HEIGHT / 2) - MARKER_LENGTH,
					MARGIN_X - MARKER_GAP,
					(HEIGHT / 2) + MARKER_LENGTH)); // left center
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2)
					- MARKER_LENGTH, MARGIN_X
					+ BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2) + MARKER_LENGTH)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT + MARKER_GAP - MARGIN_Y
					- (MARKER_LENGTH / 2), centerSheet,
					HEIGHT + MARKER_GAP - MARGIN_Y + (MARKER_LENGTH / 2))); // top center
			imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_GAP + (MARKER_LENGTH / 2),
					centerSheet, MARGIN_Y
					- MARKER_GAP - (MARKER_LENGTH / 2))); // bottom center
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2,
					MARGIN_X
							+ (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2)); // left center
			imposerConfig.addFigure(new Line(BLOCK_WIDTH + MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP,
					HEIGHT / 2,
					BLOCK_WIDTH + MARGIN_X + (MARKER_LENGTH / 2) + MARKER_GAP, HEIGHT / 2)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y - MARKER_GAP,
					centerSheet + MARKER_LENGTH,
					MARGIN_Y - MARKER_GAP)); // center bottom
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					+ MARKER_GAP, centerSheet
					+ MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT + MARKER_GAP)); // center top
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y - MARKER_GAP, circleRadius)); // bottom
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y + MARKER_GAP + BLOCK_HEIGHT,
					circleRadius)); // top
			imposerConfig.addFigure(new Circle(MARGIN_X - MARKER_GAP, HEIGHT / 2, circleRadius)); // left
			imposerConfig.addFigure(new Circle(MARGIN_X + MARKER_GAP + BLOCK_WIDTH, HEIGHT / 2,
					circleRadius)); // right
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

	/** Impose album type 010510
	 *
	 * @param nonImposedPath
	 * {@link String}
	 * @param path
	 * {@link String} */
	public void impose010510(String nonImposedPath, String path) {
		final float WIDTH = Imposition.WIDTH_MM_010510 * PdfConst.MM_TO_PX;
		final float HEIGHT = Imposition.HEIGHT_MM_010510 * PdfConst.MM_TO_PX;
		final float BLOCK_WIDTH = Imposition.BLOCK_WIDTH_MM_010510 * PdfConst.MM_TO_PX;
		final float BLOCK_HEIGHT = Imposition.BLOCK_HEIGHT_MM_010510 * PdfConst.MM_TO_PX;
		final float MARGIN_X = Imposition.MARGIN_X_MM_010510 * PdfConst.MM_TO_PX;
		final float MARGIN_Y = Imposition.MARGIN_Y_MM_010510 * PdfConst.MM_TO_PX;
		final float GAB_X = Imposition.GAB_X_MM_010510 * PdfConst.MM_TO_PX;
		final float GAB_Y = Imposition.GAB_Y_MM_010510 * PdfConst.MM_TO_PX;
		final float LINE_WIDTH = Imposition.LINE_WIDTH_MM_010510 * PdfConst.MM_TO_PX;
		final int PAGES_ON_SHEET = 1;
		PdfReader pdfReader = null;
		Document document = null;
		try {
			logger.debug("Imposing: " + order.getNumber());
			pdfReader = new PdfReader(nonImposedPath);
			document = new Document(new Rectangle(WIDTH, HEIGHT));
			document.setMargins(0, 0, 0, 0);
			FileOutputStream os = new FileOutputStream(path);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new BufferedOutputStream(os));
			document.open();

			ImposerPage imposerPage = null;
			ImposerSheet imposerSheet = null;
			ImposerPageConfig imposerPageConfig = null;
			ImposerConfig imposerConfig = new ImposerConfig(LINE_WIDTH, order.getProduct().getType(), order.getProduct().getNumber());
			float centerSheet = WIDTH / 2;
			// Vertical lines
			final float MARKER_GAP = 1.5f * PdfConst.MM_TO_PX;
			final float MARKER_LENGTH = Imposition.MARKER_LENGTH_MM_010510 * PdfConst.MM_TO_PX;
			final float MARKER_MARGIN = 5f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP),
					MARGIN_X + MARKER_MARGIN, HEIGHT - MARGIN_Y - MARKER_GAP)); // top
// left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					+ (MARKER_LENGTH - MARKER_GAP), WIDTH - MARGIN_X - MARKER_MARGIN, HEIGHT - MARGIN_Y
					- MARKER_GAP)); // top
// right
			imposerConfig.addFigure(new Line(MARGIN_X + MARKER_MARGIN, MARGIN_Y + MARKER_GAP, MARGIN_X
					+ MARKER_MARGIN,
					MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom left
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_MARGIN, MARGIN_Y + MARKER_GAP,
					WIDTH - MARGIN_X
							- MARKER_MARGIN, MARGIN_Y - (MARKER_LENGTH - MARKER_GAP))); // bottom
// right
			imposerConfig.addFigure(new Line(MARGIN_X - MARKER_GAP, (HEIGHT / 2) - MARKER_LENGTH,
					MARGIN_X - MARKER_GAP,
					(HEIGHT / 2) + MARKER_LENGTH)); // left center
			imposerConfig.addFigure(new Line(MARGIN_X + BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2)
					- MARKER_LENGTH, MARGIN_X
					+ BLOCK_WIDTH + MARKER_GAP, (HEIGHT / 2) + MARKER_LENGTH)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet, HEIGHT + MARKER_GAP - MARGIN_Y
					- (MARKER_LENGTH / 2), centerSheet,
					HEIGHT + MARKER_GAP - MARGIN_Y + (MARKER_LENGTH / 2))); // top center
			imposerConfig.addFigure(new Line(centerSheet, MARGIN_Y - MARKER_GAP + (MARKER_LENGTH / 2),
					centerSheet, MARGIN_Y
					- MARKER_GAP - (MARKER_LENGTH / 2))); // bottom center
			// Horizontal lines
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y
					- MARKER_MARGIN,
					MARGIN_X + MARKER_GAP, HEIGHT - MARGIN_Y - MARKER_MARGIN)); // left
// top
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, HEIGHT - MARGIN_Y
					- MARKER_MARGIN, WIDTH
					- MARGIN_X + (MARKER_LENGTH - MARKER_GAP), HEIGHT - MARGIN_Y - MARKER_MARGIN)); // right
// top
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH - MARKER_GAP), MARGIN_Y
					+ MARKER_MARGIN, MARGIN_X
					+ MARKER_GAP, MARGIN_Y + MARKER_MARGIN)); // left bottom
			imposerConfig.addFigure(new Line(WIDTH - MARGIN_X - MARKER_GAP, MARGIN_Y + MARKER_MARGIN,
					WIDTH - MARGIN_X
							+ (MARKER_LENGTH - MARKER_GAP), MARGIN_Y + MARKER_MARGIN)); // right
// bottom
			imposerConfig.addFigure(new Line(MARGIN_X - (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2,
					MARGIN_X
							+ (MARKER_LENGTH / 2) - MARKER_GAP, HEIGHT / 2)); // left center
			imposerConfig.addFigure(new Line(BLOCK_WIDTH + MARGIN_X - (MARKER_LENGTH / 2) + MARKER_GAP,
					HEIGHT / 2,
					BLOCK_WIDTH + MARGIN_X + (MARKER_LENGTH / 2) + MARKER_GAP, HEIGHT / 2)); // right
// center
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y - MARKER_GAP,
					centerSheet + MARKER_LENGTH,
					MARGIN_Y - MARKER_GAP)); // center bottom
			imposerConfig.addFigure(new Line(centerSheet - MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT
					+ MARKER_GAP, centerSheet
					+ MARKER_LENGTH, MARGIN_Y + BLOCK_HEIGHT + MARKER_GAP)); // center top
			// Circles
			final float circleRadius = 2f * PdfConst.MM_TO_PX;
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y - MARKER_GAP, circleRadius)); // bottom
			imposerConfig.addFigure(new Circle(centerSheet, MARGIN_Y + MARKER_GAP + BLOCK_HEIGHT,
					circleRadius)); // top
			imposerConfig.addFigure(new Circle(MARGIN_X - MARKER_GAP, HEIGHT / 2, circleRadius)); // left
			imposerConfig.addFigure(new Circle(MARGIN_X + MARKER_GAP + BLOCK_WIDTH, HEIGHT / 2,
					circleRadius)); // right
			//
			int pageCount = order.getPageCount();
			int sheetCount = pageCount / PAGES_ON_SHEET;
			List<Integer> pageNumbers = new ArrayList<Integer>(pageCount);
			for (int index = 0; index < sheetCount; index++) {
				pageNumbers.add(index + 1);
			}
			//
			int pageNumberIndex = 0;
			for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
				imposerSheet = new ImposerSheet();
				for (int page = 0; page < PAGES_ON_SHEET; page++) {

					imposerPageConfig = new ImposerPageConfig();
					imposerPageConfig.setGabx(GAB_X);
					imposerPageConfig.setGaby(GAB_Y);
					imposerPageConfig.setBlockWidth(BLOCK_WIDTH);
					imposerPageConfig.setBlockHeight(BLOCK_HEIGHT);
					imposerPageConfig.setX(MARGIN_X);
					imposerPageConfig.setY(MARGIN_Y);
					imposerPage = new ImposerPage(0, pageNumbers.get(pageNumberIndex), imposerPageConfig);
					imposerSheet.addPage(imposerPage);
					pageNumberIndex++;
				}
				imposerSheet.imposeSheet(document, pdfWriter, pdfReader, imposerConfig);
				if (sheetIndex != sheetCount - 1) {
					document.newPage();
				}
			}
			logger.debug("Imposing finished: " + order.getNumber());
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		finally {
			if (pdfReader != null)
				pdfReader.close();

			if (document != null)
				document.close();
		}
	}

    /**
     * Just copy file
     *
     * @param nonImposedPath
     * @param path
     */
    public void imposeDefault(String nonImposedPath, String path) {
        try {
            Files.copy(Paths.get(nonImposedPath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            Exceptions.rethrow(ex);
        }
    }
}