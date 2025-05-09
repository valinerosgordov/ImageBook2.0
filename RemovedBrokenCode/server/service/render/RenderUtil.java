package ru.imagebook.server.service.render;

public class RenderUtil {
	private static final float DPI = 300;
	private static final float MM_TO_INCH = 0.0393700787f;
	private static final float MM_TO_PX = MM_TO_INCH * DPI;

	public static int mmToPx(float mm) {
		return (int) (mm * MM_TO_PX);
	}

	public static float pxToMm(int px) {
		return ((float) px) / MM_TO_PX;
	}
}
