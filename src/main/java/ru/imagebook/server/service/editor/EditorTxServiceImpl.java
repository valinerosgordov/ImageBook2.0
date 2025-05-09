package ru.imagebook.server.service.editor;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository.EditorRepository;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;

public class EditorTxServiceImpl implements EditorTxService {
	private final EditorRepository repository;

	public EditorTxServiceImpl(EditorRepository repository) {
		this.repository = repository;
	}

	@Transactional
	@Override
	public List<Order<?>> loadOrders() {
		return repository.loadProcessingOrders();
	}

	@Transactional
	@Override
	public void setJpegGenerated(Integer orderId) {
		Order<?> order = repository.getOrder(orderId);
		order.setState(OrderState.FLASH_GENERATION);
	}
	
	@Transactional
	@Override
	public void setJpegGenerationError(Integer orderId) {
		Order<?> order = repository.getOrder(orderId);
		order.setState(OrderState.JPEG_GENERATION_ERROR);
	}
}
