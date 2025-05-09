package ru.imagebook.server.service;

import com.google.common.base.Strings;
import ru.imagebook.client.app.service.PostamateUnavailableException;
import ru.imagebook.shared.model.app.PickPointData;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.minogin.util.shared.math.MathUtil.roundMoney;

/**
 * Allows to compute delivery cost via PickPoint service.
 */
public class PickPointCalc {

    /**
     * Position of city in address string.
     */
    private static final int CITY_INDEX = 2;

    /**
     * Rate for storage in a cell of type L
     */
    private static final int BOX_RATE = 240;

    /**
     * Coefficient to compensate expenses and restore VAT
     */
    private static final double K_COMPENSATION = 1.4;

    /**
     * Rate per 1 kg of sending to regional center of Russian Federation in rubles.
     * Sending weight will be rounded up to the next integer, e.g. 1200 gr will be rounded to 2 kg.
     */
    private static final Map<String, Double> ZONE_RATES = new HashMap<>();

    /**
     * Rates in rubles for every zone.
     */
    static {
        ZONE_RATES.put("-1", 0.0);
        ZONE_RATES.put("0", 8.0);
        ZONE_RATES.put("1", 11.7);
        ZONE_RATES.put("2", 19.5);
        ZONE_RATES.put("3", 34.2);
        ZONE_RATES.put("4", 46.8);
        ZONE_RATES.put("5", 87.1);
        ZONE_RATES.put("6", 162.0);
        ZONE_RATES.put("7", 185.0);
        ZONE_RATES.put("8", 270.0);
    }

    /**
     * Computes delivery cost.
     *
     * @param pickPointData info to compute delivery cost
     * @return delivery cost, rounded to the nearest int
     */
    public int calculateDeliveryCost(PickPointData pickPointData) {
        checkArgument(pickPointData.getWeightGr() > 0, "Weight should be > 0");
        checkArgument(!Strings.isNullOrEmpty(pickPointData.getAddress()), "Pickpoint address shouldn't be empty");
        checkArgument(!Strings.isNullOrEmpty(pickPointData.getPostamateID()), "Postamate ID shouldn't be empty");
        checkArgument(!Strings.isNullOrEmpty(pickPointData.getRateZone()), "Rate zone shouldn't be empty");
        checkArgument(pickPointData.getTrunkCoeff() != null, "Trunk coefficient shouldn't be empty");

        String rateZone = pickPointData.getRateZone();
        if (!ZONE_RATES.containsKey(rateZone)) {
            throw new PostamateUnavailableException(String.format("No such rate zone [%s] for postamateid=%s",
                    rateZone, pickPointData.getPostamateID()));
        }
        double rate = ZONE_RATES.get(rateZone);
        double weightKg = Math.ceil(pickPointData.getWeightGr() / 1000.0);
        double cost = (BOX_RATE + weightKg * rate) * pickPointData.getTrunkCoeff() * K_COMPENSATION;
        return roundMoney(cost);
    }
}
