package ru.imagebook.server.service;


import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.imagebook.client.app.service.PostamateUnavailableException;
import ru.imagebook.shared.model.app.PickPointData;

import static org.junit.Assert.assertEquals;

public class PickPointCalcTest {

	private static final String SOME_POSTAMATE = "ptmt-12345";
	private static final String SOME_ADDRESS = "125167, Российская Федерация, Энск, Театральная алл., д. 3, стр. 1";

	@DataProvider(name = "zonesTestData")
	public static Object[][] createZonesTestData() {
		PickPointData moskow100gr = new PickPointData() {
			{
				setWeightGr(100);
				setRateZone("-1");
				setTrunkCoeff(1.0);
				setPostamateID(SOME_POSTAMATE);
				setAddress("125167, Российская Федерация, Москва, Театральная алл., д. 3, стр. 1");
			}
		};
		PickPointData piter100gr = new PickPointData() {
			{
				setWeightGr(100);
				setRateZone("0");
				setTrunkCoeff(1.0);
				setPostamateID(SOME_POSTAMATE);
				setAddress("197101, Российская Федерация, Санкт-Петербург, Каменноостровский пр-т, д. 4");
			}
		};
		PickPointData zoneOne2kg = new PickPointData() {
			{
				setWeightGr(1200);
				setRateZone("1");
				setTrunkCoeff(1.0);
				setPostamateID(SOME_POSTAMATE);
				setAddress(SOME_ADDRESS);
			}
		};
		PickPointData zoneOneProvinceTown2kg = new PickPointData() {
			{
				setWeightGr(1200);
				setRateZone("1");
				setTrunkCoeff(1.25);
				setPostamateID(SOME_POSTAMATE);
				setAddress(SOME_ADDRESS);
			}
		};
		PickPointData zoneEight2kg = new PickPointData() {
			{
				setWeightGr(1200);
				setRateZone("8");
				setTrunkCoeff(1.0);
				setPostamateID(SOME_POSTAMATE);
				setAddress(SOME_ADDRESS);
			}
		};

		return new Object[][]{
				{moskow100gr, 336},
				{piter100gr, 348},
				{zoneOne2kg, 369},
				{zoneOneProvinceTown2kg, 461},
				{zoneEight2kg, 1092}
		};
	}

	@Test(dataProvider = "zonesTestData")
	public void shouldProperlyCalculateDeliveryCost(PickPointData pickPointData, int expectedCost) throws Exception {
		PickPointCalc pickPointCalc = new PickPointCalc();
		int cost = pickPointCalc.calculateDeliveryCost(pickPointData);
		assertEquals(expectedCost, cost);
	}

	@Test(expectedExceptions = {PostamateUnavailableException.class})
	public void shouldFailIfZoneIsIUnknown() throws Exception {
		PickPointData pData = new PickPointData() {
			{
				setWeightGr(100);
				setRateZone("100");
				setTrunkCoeff(1.0);
				setPostamateID(SOME_POSTAMATE);
				setAddress(SOME_ADDRESS);
			}
		};
		new PickPointCalc().calculateDeliveryCost(pData);
	}

}