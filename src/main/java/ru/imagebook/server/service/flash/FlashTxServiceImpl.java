package ru.imagebook.server.service.flash;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.FlashRepository;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.minogin.core.client.collections.XArrayList;

public class FlashTxServiceImpl implements FlashTxService {
	private final FlashRepository repository;
	private final OrderService orderService;

	public FlashTxServiceImpl(FlashRepository repository, OrderService orderService) {
		this.repository = repository;
		this.orderService = orderService;
	}

	@Transactional
	@Override
	public List<Order<?>> loadOrders() {
		return repository.loadOrders();
	}

	@Transactional
	@Override
	public void setFlashGenerated(Integer id) {
		Order<?> order = repository.getOrder(id);
		order.setState(OrderState.FLASH_GENERATED);
	}

	@Transactional
	@Override
	public void notifyFlashGenerated(Order<?> order) {
		orderService.notifyFlashGenerated(new XArrayList<Integer>(order.getId()));
	}
}
