package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;


public class CostCalculatorImpl implements CostCalculator {
	private final Calc calc;
	private final int userLevel;
	private final int userDiscountPc;
	private final int userAlbumDiscountPc;
	private final boolean specialOfferEnabled;

	CostCalculatorImpl(Calc calc, int userLevel, int userDiscountPc) {
		this.calc = calc;
		this.userLevel = userLevel;
		this.userDiscountPc = userDiscountPc;
		this.userAlbumDiscountPc = 0;
		this.specialOfferEnabled = Boolean.FALSE;
	}
	
	public CostCalculatorImpl(Calc calc, int userLevel, int userDiscountPc, int userAlbumDiscountPc,
							  boolean specialOfferEnabled) {
        this.calc = calc;
        this.userLevel = userLevel;
        this.userDiscountPc = userDiscountPc;
        this.userAlbumDiscountPc = userAlbumDiscountPc;
        this.specialOfferEnabled = specialOfferEnabled;
    }
	
	@Override
	public int calculateCost(Order<?> order) {
		return calculateCost(order, false);
	}

	/**
     * Calculates order cost
	 * User discount for the album has top priority
	 *    (@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-434'>IMAGEBOOK-434</a>)
     * Else if {@link #specialOfferEnabled} flag is set to true then cost calculates with discounts specified
	 * for order's product
	 *    (@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-224'>IMAGEBOOK-224</a>)	 *
     *  
     * @param order 
     * @param oldPrice OldPrice calculation flag
     *        (@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-217'>IMAGEBOOK-217</a>)
     * @return order cost
     */
	@Override	
	public int calculateCost(Order<?> order, boolean oldPrice) {
		int quantity = order.getQuantity();
		int price = calc.getImagebookPrice();
		int discountPc;
		int cost;
		int costByLevel;

		if (userAlbumDiscountPc > 0) {
			cost = computeCostByDiscount(price, quantity, userAlbumDiscountPc);
		} else if (specialOfferEnabled && !oldPrice) {
		    Product product = order.getProduct();
		    cost = computeCostByDiscount(price, quantity, product.getImagebookDiscount());
		} else {
			int orderLevel = order.getLevel() != null ? order.getLevel() : 0;
			int level = Math.max(orderLevel, userLevel);
			
			if (order.getProduct().isTrialAlbum()) {
				return price * quantity;
			}

			costByLevel = computeCostByLevel(quantity, level, oldPrice);
			if (order.getDiscountSum() != null) {
				costByLevel -= order.getDiscountSum();
				if (costByLevel < 0)
					costByLevel = 0;
			}
			discountPc = Math.max(order.getDiscountPc(), userDiscountPc);
			
			int costByDiscountPc = computeCostByDiscount(price, quantity, discountPc);
			cost = Math.min(costByLevel, costByDiscountPc);
		}
		
		return cost;
	}
	
	@Override
	public int calculatePhCost(final Order<?> order) {
	    final int price = calc.getPrintingHousePrice();
	    final int quantity = order.getQuantity();
		final Integer discountPCenter = order.getDiscountPCenter();
	    int cost;

	    if (specialOfferEnabled) {
	        Product product = order.getProduct();
	        cost = computeCostByDiscount(price, quantity, product.getPhDiscount());
	    } else {
	        cost = price * quantity;
	    }
	    
        return discountPCenter != null ? (int) Math.ceil(cost - ((cost * discountPCenter) / 100)) : cost;
	}
	
	private int computeCostByDiscount(int price, int quantity, int discountPc) {
	    int cost = price * quantity;
	    return computeCostByDiscount(cost, discountPc);
	}

	/**
	 * Calculates cost based on user's bonus status 
	 *  
	 * @param quantity Order quantity
	 * @param level User's bonus status
	 * @param oldPrice OldPrice calculation flag: if true then calculate cost for the 9 bonus status
	 *        (@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-217'>IMAGEBOOK-217</a>)  
	 * @return cost based on user's bonus status 
	 */
	private int computeCostByLevel(int quantity, int level, boolean oldPrice) {
	    return oldPrice
               ? calc.getIBXOldPrice(quantity, 9) 
               : calc.getIBX(quantity, level);
	}

	public static int computeCostByDiscount(int initCost, int discountPc) {
		return (int) (initCost * (100.0 - discountPc) / 100.0);
	}
}
