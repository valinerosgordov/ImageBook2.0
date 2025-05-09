package ru.imagebook.client.common.service;

import static ru.imagebook.client.common.service.CostCalculatorImpl.computeCostByDiscount;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;

/**
 * Created by rifat on 01.02.17.
 */
public class BillCalculator {
    /**
     * Computes Bill base cost (only orders costs, without delivery and other discounts)
     *
     * @return Bill base cost
     */
    public static int computeBaseCost(Bill bill) {
        int total = 0;

        for (Order<?> order : bill.getOrders()) {
            total += order.getCost();
        }

        return total;
    }

    /**
     * Calculates total Bill cost
     *
     * @return Total Bill cost
     */
    public static int computeTotal(Bill bill) {
        return computeBaseCost(bill) + computeDeliveryCost(bill);
    }

    public static int computePhTotal(Bill bill) {
        int total = 0;

        for (Order<?> order : bill.getOrders()) {
            total += order.getPhCost();
        }

        return total;
    }

    /**
     * Calculates Bill's delivery cost
     *
     * @return Bill's delivery cost
     */
    public static int computeDeliveryCost(Bill bill) {
        int deliveryCost = bill.getDeliveryCost();

        if (bill.isHasDeliveryDiscount()) {
            deliveryCost = computeCostByDiscount(bill.getDeliveryCost(), bill.getDeliveryDiscountPc());
        }

        return deliveryCost;
    }
}
