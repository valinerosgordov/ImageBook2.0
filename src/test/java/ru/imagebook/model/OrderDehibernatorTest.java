package ru.imagebook.model;

import java.util.Set;

import org.junit.Test;

import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.server.hibernate.Dehibernator;

public class OrderDehibernatorTest {
	@Test
	public void test() {
		Bean bill = new BaseEntityBean();

		OrderBean order1 = new OrderBean();
		order1.setId(1);
		order1.set("bill", bill);

		OrderBean order2 = new OrderBean();
		order2.setId(2);
		order2.set("bill", bill);

		OrderBean order3 = new OrderBean();
		order3.setId(3);
		order3.set("bill", bill);

		OrderBean order4 = new OrderBean();
		order4.setId(4);
		order4.set("bill", bill);

		Set<Bean> orders = new MockPersistentSortedSet();
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		orders.add(order4);
		bill.set("orders", orders);

		System.out.println("AAAAAAAAAAAAAA");
		for (OrderBean order : (Set<OrderBean>) bill.get("orders")) {
			System.out.println(order.getId());
		}

		new Dehibernator().clean(bill);

		System.out.println("BBBBBBBBBBBBB");
		for (OrderBean order : (Set<OrderBean>) bill.get("orders")) {
			System.out.println(order.getId());
		}
	}
}
