package ru.minogin.core.server.format;

import java.io.StringReader;
import java.text.DecimalFormat;

import org.xml.sax.InputSource;

public class MoneyFormat {
	@SuppressWarnings("unused")
	private static String RubOneUnit, RubTwoUnit, RubFiveUnit, RubSex, KopOneUnit, KopTwoUnit,
			KopFiveUnit, KopSex;
	private static String xml = "<?xml version=\"1.0\" encoding=\"windows-1251\" standalone=\"yes\"?>"
			+ "<CurrencyList>"
			+ "<RUR CurrID=\"810\" CurrName=\"Российские рубли\" RubOneUnit=\"рубль\" "
			+ "RubTwoUnit=\"рубля\" RubFiveUnit=\"рублей\" RubSex=\"M\" KopOneUnit=\"копейка\" "
			+ "KopTwoUnit=\"копейки\" KopFiveUnit=\"копеек\" KopSex=\"F\" />"
			+ "<DEM CurrID=\"276\" CurrName=\"Немецкие марки\" RubOneUnit=\"марка\" "
			+ "RubTwoUnit=\"марки\" RubFiveUnit=\"марок\" RubSex=\"F\" KopOneUnit=\"пфенниг\" "
			+ "KopTwoUnit=\"пфеннига\" KopFiveUnit=\"пфеннигов\" KopSex=\"M\" />"
			+ "<USD CurrID=\"840\" CurrName=\"Доллары США\" RubOneUnit=\"доллар\" "
			+ "RubTwoUnit=\"доллара\" RubFiveUnit=\"долларов\" RubSex=\"M\" KopOneUnit=\"цент\" "
			+ "KopTwoUnit=\"цента\" KopFiveUnit=\"центов\" KopSex=\"M\" /></CurrencyList>";
	private StringBuffer money2str = new StringBuffer();

	public MoneyFormat() {
		FillSuffix("RUR");
	}

	public String format(double sum) {
		moneytostr(sum);
		return money2str.toString();
	}

	private void FillSuffix(String theISOstr) {
		org.w3c.dom.Document xmlDoc = null;

		javax.xml.parsers.DocumentBuilderFactory DocFactory = javax.xml.parsers.DocumentBuilderFactory
				.newInstance();

		try {
			javax.xml.parsers.DocumentBuilder xmlDocBuilder = DocFactory.newDocumentBuilder();
			StringReader reader = new StringReader(xml);
			InputSource inputSource = new InputSource(reader);
			xmlDoc = xmlDocBuilder.parse(inputSource);
		}
		catch (org.xml.sax.SAXException sxe) {
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();
		}
		catch (javax.xml.parsers.ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		}
		org.w3c.dom.Element theISOElement = (org.w3c.dom.Element) (xmlDoc
				.getElementsByTagName(theISOstr)).item(0);

		RubOneUnit = theISOElement.getAttribute("RubOneUnit");
		RubTwoUnit = theISOElement.getAttribute("RubTwoUnit");
		RubFiveUnit = theISOElement.getAttribute("RubFiveUnit");
		KopOneUnit = theISOElement.getAttribute("KopOneUnit");
		KopTwoUnit = theISOElement.getAttribute("KopTwoUnit");
		KopFiveUnit = theISOElement.getAttribute("KopFiveUnit");
		RubSex = theISOElement.getAttribute("RubSex");
		KopSex = theISOElement.getAttribute("KopSex");
	}

	private void moneytostr(Double theMoney) {
		int triadNum = 0;
		int theTriad;

		int intPart = theMoney.intValue();
		int fractPart = (int) Math.round((theMoney.doubleValue() - intPart) * 100);
		if (intPart == 0)
			money2str.append("Ноль ");
		do {
			theTriad = intPart % 1000;
			money2str.insert(0, triad2Word(theTriad, triadNum, RubSex));
			if (triadNum == 0) {
				int range10 = (theTriad % 100) / 10;
				int range = theTriad % 10;
				if (range10 == 1)
					money2str.append(RubFiveUnit);
				else {
					switch (range) {
						case 1:
							money2str.append(RubOneUnit);
							break;
						case 2:
						case 3:
						case 4:
							money2str.append(RubTwoUnit);
							break;
						default:
							money2str.append(RubFiveUnit);
							break;
					}
				}
			}
			intPart = intPart / 1000;
			triadNum++;
		}
		while (intPart > 0);

		money2str.append(" ");
		money2str.append(new DecimalFormat("00").format(fractPart));
		money2str.append(" ");
		// if (fractPart == 0)
		// money2str.append("ноль ");
		// money2str.append(triad2Word(fractPart, 0, KopSex));
		if ((fractPart % 10) == 1) {
			money2str.append(KopOneUnit);
		}
		else {
			switch (fractPart % 10) {
				case 1:
					money2str.append(KopOneUnit);
					break;
				case 2:
				case 3:
				case 4:
					money2str.append(KopTwoUnit);
					break;
				default:
					money2str.append(KopFiveUnit);
					break;
			}
		}
		money2str.setCharAt(0, Character.toUpperCase(money2str.charAt(0)));
	}

	static private String triad2Word(int triad, int triadNum, String Sex) {
		StringBuffer triadWord = new StringBuffer(50);

		if (triad == 0) {
			return triadWord.toString();
		}

		int range = triad / 100;
		switch (range) {
			default:
				break;
			case 1:
				triadWord.append("сто ");
				break;
			case 2:
				triadWord.append("двести ");
				break;
			case 3:
				triadWord.append("триста ");
				break;
			case 4:
				triadWord.append("четыреста ");
				break;
			case 5:
				triadWord.append("пятьсот ");
				break;
			case 6:
				triadWord.append("шестьсот ");
				break;
			case 7:
				triadWord.append("семьсот ");
				break;
			case 8:
				triadWord.append("восемьсот ");
				break;
			case 9:
				triadWord.append("девятьсот ");
				break;
		}

		range = (triad % 100) / 10;
		switch (range) {
			default:
				break;
			case 2:
				triadWord.append("двадцать ");
				break;
			case 3:
				triadWord.append("тридцать ");
				break;
			case 4:
				triadWord.append("сорок ");
				break;
			case 5:
				triadWord.append("пятьдесят ");
				break;
			case 6:
				triadWord.append("шестьдесят ");
				break;
			case 7:
				triadWord.append("семьдесят ");
				break;
			case 8:
				triadWord.append("восемьдесят ");
				break;
			case 9:
				triadWord.append("девяносто ");
				break;
		}

		int range10 = range;
		range = triad % 10;
		if (range10 == 1) {
			switch (range) {
				case 0:
					triadWord.append("десять ");
					break;
				case 1:
					triadWord.append("одиннадцать ");
					break;
				case 2:
					triadWord.append("двенадцать ");
					break;
				case 3:
					triadWord.append("тринадцать ");
					break;
				case 4:
					triadWord.append("четырнадцать ");
					break;
				case 5:
					triadWord.append("пятнадцать ");
					break;
				case 6:
					triadWord.append("шестнадцать ");
					break;
				case 7:
					triadWord.append("семнадцать ");
					break;
				case 8:
					triadWord.append("восемнадцать ");
					break;
				case 9:
					triadWord.append("девятнадцать ");
					break;
			}
		}
		else {
			switch (range) {
				default:
					break;
				case 1:
					if (triadNum == 1)
						triadWord.append("одна ");
					else if (Sex.equals("M"))
						triadWord.append("один ");
					if (Sex.equals("F"))
						triadWord.append("одна ");
					break;
				case 2:
					if (triadNum == 1)
						triadWord.append("две ");
					else if (Sex.equals("M"))
						triadWord.append("два ");
					if (Sex.equals("F"))
						triadWord.append("две ");
					break;
				case 3:
					triadWord.append("три ");
					break;
				case 4:
					triadWord.append("четыре ");
					break;
				case 5:
					triadWord.append("пять ");
					break;
				case 6:
					triadWord.append("шесть ");
					break;
				case 7:
					triadWord.append("семь ");
					break;
				case 8:
					triadWord.append("восемь ");
					break;
				case 9:
					triadWord.append("девять ");
					break;
			}
		}

		switch (triadNum) {
			default:
				break;
			case 1:
				if (range10 == 1)
					triadWord.append("тысяч ");
				else {
					switch (range) {
						default:
							triadWord.append("тысяч ");
							break;
						case 1:
							triadWord.append("тысяча ");
							break;
						case 2:
						case 3:
						case 4:
							triadWord.append("тысячи ");
							break;
					}
				}
				break;
			case 2:
				if (range10 == 1)
					triadWord.append("миллионов ");
				else {
					switch (range) {
						default:
							triadWord.append("миллионов ");
							break;
						case 1:
							triadWord.append("миллион ");
							break;
						case 2:
						case 3:
						case 4:
							triadWord.append("миллиона ");
							break;
					}
				}
				break;
			case 3:
				if (range10 == 1)
					triadWord.append("миллиардов ");
				else {
					switch (range) {
						default:
							triadWord.append("миллиардов ");
							break;
						case 1:
							triadWord.append("миллиард ");
							break;
						case 2:
						case 3:
						case 4:
							triadWord.append("миллиарда ");
							break;
					}
				}
				break;
			case 4:
				if (range10 == 1)
					triadWord.append("триллионов ");
				else {
					switch (range) {
						default:
							triadWord.append("триллионов ");
							break;
						case 1:
							triadWord.append("триллион ");
							break;
						case 2:
						case 3:
						case 4:
							triadWord.append("триллиона ");
							break;
					}
				}
				break;
		}
		return triadWord.toString();
	}
}
