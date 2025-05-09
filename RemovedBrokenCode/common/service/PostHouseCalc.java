package ru.imagebook.client.common.service;

import static ru.minogin.util.shared.math.MathUtil.roundMoney;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import ru.imagebook.client.common.service.delivery.PostHouseType;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;

public class PostHouseCalc {
    public static final int ALBUM_HEIGHT_MM = 30;
    public static final int MAX_FIRST_CLASS_DIMENSIONS_SUM_MM = 680;
    public static final double PACKAGE_MULTIPLIER = 1.2; 
    
	public static final Map<PostHouseType, Integer> MIN_WEIGHTS = new HashMap<PostHouseType, Integer>();
	public static final Map<PostHouseType, Integer> MAX_WEIGHTS = new HashMap<PostHouseType, Integer>();
	public static final Map<PostHouseType, Integer> WEIGHT_STEPS = new HashMap<PostHouseType, Integer>();

	public static final Map<PostHouseType, Double> MIN_PRICES = new HashMap<PostHouseType, Double>();
	public static final Map<PostHouseType, Double> PRICE_STEPS = new HashMap<PostHouseType, Double>();

	public static final Map<PostHouseType, Double> PACKAGE_PRICES = new HashMap<PostHouseType, Double>();

	public static final double MAJOR_SHIPPING_TO_POSTHOUSE_PER_PACKAGE = 25;

	static {
		MIN_WEIGHTS.put(PostHouseType.NORMAL, 100);
		MAX_WEIGHTS.put(PostHouseType.NORMAL, 20000);
		WEIGHT_STEPS.put(PostHouseType.NORMAL, 20);
		MIN_PRICES.put(PostHouseType.NORMAL, 49.56);
		PRICE_STEPS.put(PostHouseType.NORMAL, 1.59);
		PACKAGE_PRICES.put(PostHouseType.NORMAL, 33.04);

		MIN_WEIGHTS.put(PostHouseType.FIRST_CLASS, 100);
		MAX_WEIGHTS.put(PostHouseType.FIRST_CLASS, 2000);
		WEIGHT_STEPS.put(PostHouseType.FIRST_CLASS, 100);
		MIN_PRICES.put(PostHouseType.FIRST_CLASS, 126.26);
		PRICE_STEPS.put(PostHouseType.FIRST_CLASS, 18.88);
		PACKAGE_PRICES.put(PostHouseType.FIRST_CLASS, 47.2);
	}

	public int getCost(PostHouseType type, int weightG) {
		if (weightG < 0) {
			throw new IllegalArgumentException();
		}	

		int maxWeight = MAX_WEIGHTS.get(type);
		int nMaxPackages = weightG / maxWeight;
		int nPackages = nMaxPackages + 1;
		int lastPackageWeight = weightG % maxWeight;

		int minWeight = MIN_WEIGHTS.get(type);
		int weightStep = WEIGHT_STEPS.get(type);

		int steps;
		if (lastPackageWeight < minWeight) {
			steps = 0;
		} else {
			steps = (lastPackageWeight - minWeight) / weightStep + 1;
			if ((lastPackageWeight - minWeight) % weightStep == 0) {
				steps--;
			}
		}

		double minPrice = MIN_PRICES.get(type);
		double priceStep = PRICE_STEPS.get(type);
		double maxPrice = minPrice + priceStep * (maxWeight - minWeight) / weightStep;
		double packagePrice = PACKAGE_PRICES.get(type);

		double postCost = nMaxPackages * maxPrice + minPrice + priceStep * steps;
		double postHouseCost = postCost + packagePrice * nPackages;
//		  double postHouseCostWithMajorTransfer = postHouseCost + MAJOR_SHIPPING_TO_POSTHOUSE_PER_PACKAGE * nPackages;
//        double finalCost = postHouseCostWithMajorTransfer * Delivery.COST_MULTIPLIER;
		return roundMoney(postHouseCost);
	}

	public boolean isFirstClassAllowed(Bill bill) {
	    Set<Order<?>> orders = bill.getOrders();   
	    
	    int totalQuantity = 0;
	    int maxLength = 0;
	    int maxWidth = 0;
	    
	    for (Order<?> order : orders) {
	        Product product = order.getProduct();
	        
	        if (product.getBlockWidth() > maxLength) {
	            maxLength = product.getBlockWidth();
	        }
	        if (product.getBlockHeight() > maxWidth) {
	            maxWidth = product.getBlockHeight();
	        }
	        totalQuantity += order.getQuantity();
	    }
	    
	    int height = ALBUM_HEIGHT_MM * totalQuantity;
	    double dim_sum = (maxLength + maxWidth + height) * PACKAGE_MULTIPLIER;
	    return dim_sum < MAX_FIRST_CLASS_DIMENSIONS_SUM_MM;
	}

    public boolean isFirstClassAllowed(Order<?> order) {
        Bill bill = new Bill();
        bill.setOrders(new TreeSet<Order<?>>());
        bill.addOrder(order);
        return isFirstClassAllowed(bill);
    }
}