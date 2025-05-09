package ru.minogin.oo.server.text;

public class BorderLine {
	
	private final int argbColor;
	private final short InnerLineWidth;
	private final short OuterLineWidth;
	private final short LineDistance;

	public BorderLine(int argbColor, short innerLineWidth,
			short outerLineWidth, short lineDistance) {
		this.argbColor = argbColor;
		InnerLineWidth = innerLineWidth;
		OuterLineWidth = outerLineWidth;
		LineDistance = lineDistance;
	}

	public BorderLine(int argbColor, int innerLineWidth, int outerLineWidth, int lineDistance) {
		this(argbColor, (short)innerLineWidth, (short)outerLineWidth, (short)lineDistance);
	}

	public int getArgbColor() {
		return argbColor;
	}

	public short getInnerLineWidth() {
		return InnerLineWidth;
	}

	public short getOuterLineWidth() {
		return OuterLineWidth;
	}

	public short getLineDistance() {
		return LineDistance;
	}

}
