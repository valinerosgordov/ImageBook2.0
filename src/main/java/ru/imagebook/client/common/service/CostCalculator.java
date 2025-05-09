package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.Order;

public interface CostCalculator {
	int calculateCost(Order<?> order);
	
	int calculateCost(Order<?> order, boolean oldPrice);

	/**
	 * Calculates order print house cost
	 * If specialOfferEnabled flag is set to true then cost calculates with discounts
	 * specified for order's product (@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-224'>IMAGEBOOK-224</a>)
	 * If discountPCenter field is not empty then cost calculates with discountsPCenter
	 * specified for order's product(@see <a href='http://jira.minogin.ru/browse/IMAGEBOOK-361'>IMAGEBOOK-361</a>)
	 *
	 * @param order
	 * @return order print house cost
	 */
	int calculatePhCost(Order<?> order);
}
