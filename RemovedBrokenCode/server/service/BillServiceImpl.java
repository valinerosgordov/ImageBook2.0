package ru.imagebook.server.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.common.service.BillCalculator;
import ru.imagebook.server.repository.BillRepository;
import ru.imagebook.server.service.notify.NotifyService;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.imagebook.shared.util.delivery.DeliveryMethodHelper;
import ru.minogin.core.server.freemarker.FreeMarker;

public class BillServiceImpl implements BillService {
	private final BillRepository repository;
	private final OrderService orderService;
	private final NotifyService notifyService;
	@Autowired
	private MessageSource messages;
    @Autowired
    private DeliveryMethodHelper deliveryMethodHelper;
	@Autowired
	private DeliveryAddressHelper deliveryAddressHelper;

	public BillServiceImpl(BillRepository repository, OrderService orderService,
			NotifyService notifyService) {
		this.repository = repository;
		this.orderService = orderService;
		this.notifyService = notifyService;
	}

	@Override
	public Bill getBill(int billId) {
		return repository.getBill(billId);
	}

	@Override
	public List<Bill> loadBills(int offset, int limit, final BillFilter billFilter) {
		List<Bill> bills = repository.loadBills(offset, limit, billFilter);
		for (final Iterator<Bill> iterator = bills.iterator(); iterator.hasNext();) {
			Bill bill = iterator.next();
			orderService.setComputedValues(bill);
			if (billFilter != null) {
				if (billFilter.getTotalLimitFrom() != null && billFilter.getTotalLimitTo() != null) {
					if (billFilter.getTotalLimitFrom().compareTo(bill.getTotal()) > 0 || billFilter.getTotalLimitTo().compareTo(bill.getTotal()) < 0) {
						iterator.remove();
					}
				} else if (billFilter.getTotalLimitFrom() != null) {
					if (billFilter.getTotalLimitFrom().compareTo(bill.getTotal()) > 0) {
						iterator.remove();
					}
				} else if (billFilter.getTotalLimitTo() != null) {
					if (billFilter.getTotalLimitTo().compareTo(bill.getTotal()) < 0) {
						iterator.remove();
					}
				}
			}
		}
		return bills;
	}

	@Override
	public long countBills(final BillFilter billFilter) {
		if (billFilter != null && (billFilter.getTotalLimitTo() != null || billFilter.getTotalLimitFrom() != null)) {
			return loadBills(-1, -1, billFilter).size();
		} else {
			return repository.countBills(billFilter);
		}

	}

	@Override
	public void markPaid(List<Integer> ids) {
		List<Bill> bills = repository.loadBills(ids);
		for (Bill bill : bills) {
			if (bill.getState() == BillState.NEW) {
				markPaid(bill.getId());
			}
		}
	}

	@Transactional
	@Override
	public void markPaid(int billId) {
		markPaid(billId, true);
	}

	@Transactional
	@Override
	public void markPaid(int billId, boolean notify) {
		Date payDate = new Date();
		
		Bill bill = orderService.getBill(billId);
		bill.setState(BillState.PAID);
		bill.setPayDate(payDate);
		
		for (Order<?> order : bill.getOrders()) {
			order.setState(OrderState.PAID);
			order.setPayDate(payDate);
			orderService.computeOrderPrintDate(order);
		}

		User user = bill.getUser();
		List<Bill> bills = repository.loadPaidBills(user);
		int sum = 0;
		for (Bill b : bills) {
			orderService.setComputedValues(b);
			sum += b.getTotal();
		}
		int level = sum / 5000;
		if (user.getLevel() < level && level <= 8) {
			user.setLevel(level);
			user.setPhotographerByLevel(level);
		}

		if (notify) {
			notifyBillPaid(bill, user);
		}
	}

    private void notifyBillPaid(Bill bill, User user) {

		Locale locale = new Locale(user.getLocale());

		// Notify admin
		Vendor vendor = user.getVendor();
		String subject = "Счет № " + bill.getId() + " оплачен";
		String text = "Счет № " + bill.getId() + " оплачен";
		FreeMarker billSummaryTemplate = new FreeMarker(getClass());
		billSummaryTemplate.set("bill", bill);
		billSummaryTemplate.set("header", text);
		billSummaryTemplate.set("deliveryMethod", deliveryMethodHelper.getDeliveryMethod(bill));
		billSummaryTemplate.set("deliveryAddress", deliveryAddressHelper.getAddressString(bill));
		billSummaryTemplate.set("baseCost", BillCalculator.computeBaseCost(bill));
		billSummaryTemplate.set("deliveryCost", BillCalculator.computeDeliveryCost(bill));
		billSummaryTemplate.set("defaultFlyleafId", Flyleaf.DEFAULT_ID);
		String bodyMessage = billSummaryTemplate.process("billPaidAdmin.ftl", user.getLocale());
		notifyService.notifyVendorAdminHtml(vendor, subject, bodyMessage);

		// Notify user
		subject = messages.getMessage("billPaidSubject", new Object[]{vendor.getName()}, locale);
		FreeMarker freeMarker = new FreeMarker(getClass());
		freeMarker.set("bill", bill);
		freeMarker.set("vendor", vendor);
        freeMarker.set("deliveryMethod", deliveryMethodHelper.getDeliveryMethod(bill));
        freeMarker.set("deliveryAddress", deliveryAddressHelper.getAddressString(bill));
        freeMarker.set("baseCost", BillCalculator.computeBaseCost(bill));
		freeMarker.set("deliveryCost", BillCalculator.computeDeliveryCost(bill));
		freeMarker.set("defaultFlyleafId", Flyleaf.DEFAULT_ID);
		String html = freeMarker.process("billPaid.ftl", user.getLocale());
		notifyService.notifyUserHtml(user, subject, html);

        // sms
		String sms = freeMarker.process("billPaid_sms.ftl", user.getLocale());
		notifyService.notifyUserBySms(user, sms);
	}

	@Override
	public void updateBill(Bill modified) {
		Bill bill = repository.getBill(modified.getId());
		bill.setAdv(modified.isAdv());
        bill.setMultishipOrderId(modified.getMultishipOrderId());
        bill.setMshDeliveryService(modified.getMshDeliveryService());
        bill.setOrientDeliveryDate(modified.getOrientDeliveryDate());
        bill.setDeliveryTime(modified.getDeliveryTime());
	}

	@Override
	public void deleteBills(List<Integer> ids) {
		List<Bill> bills = repository.loadBills(ids);
		for (Bill bill : bills) {
			orderService.deleteBill(bill);
		}
	}

	@Deprecated
	@Override
	@Transactional
	public Integer computeMailruDiscountPc(Integer billId) {
		Bill bill = repository.getBill(billId);
		
		int mailRuDiscountPc = 0;
		
		boolean isCouponAppliedForBillOrder = false;
		for (Order<?> order : bill.getOrders()) {
			if (order.getCouponId() != null) {
				isCouponAppliedForBillOrder = true;
				break;
			}
		}
		
		if (!isCouponAppliedForBillOrder) {
			User user = bill.getUser();
			int userLevel = user.getLevel();
			if (userLevel >= 0 && userLevel <= 3) {
				mailRuDiscountPc = 7;
			} else if (userLevel >= 4 && userLevel <= 5) {
				mailRuDiscountPc = 5;
			} else if (userLevel >= 6 && userLevel <= 7) {
				mailRuDiscountPc = 3;
			}
		}
		
		return mailRuDiscountPc;
	}

    @Override
    @Transactional
    public void saveBill(Bill bill) {
        repository.saveBill(bill);
    }
}
