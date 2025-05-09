package ru.imagebook.server.service2.flash;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.flash.service.FlashData;
import ru.imagebook.client.flash.service.FlashService;
import ru.imagebook.server.repository2.FlashRepository;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.flash.Flash;
import ru.minogin.core.server.hibernate.Dehibernate;

public class FlashServiceImpl implements FlashService {
	@Autowired
	private ru.imagebook.server.service.flash.FlashService service;
	@Autowired
	private AuthService authService;
	@Autowired
	protected FlashRepository repository;
	@Autowired
	private XFlashService xService;

	@Transactional
	@Dehibernate
	@Override
	public FlashData loadData(int code) {
		User user = authService.getCurrentUser();
		Order<?> order = service.loadOrder(code, user.getId());
		return new FlashData(order, user);
	}

	@Deprecated
	@Transactional
	@Override
	public void unpublish(int orderId) {
		int userId = authService.getCurrentUserId();
		service.unpublishFlash(orderId, userId);
	}

	@Deprecated
	@Transactional
	@Override
	public void publish(int orderId) {
		int userId = authService.getCurrentUserId();
		service.publishFlash(orderId, userId);
	}

	@Transactional
	@Override
	public void generateFlash(int orderId, int flashWidth) {
		Order<?> order = repository.getOrder(orderId);
		xService.generateFlash(order, flashWidth);
	}

	@Transactional
	@Override
	public List<Flash> loadFlashes(int orderId) {
		Order<?> order = repository.getOrder(orderId);
		return xService.loadFlashes(order);
	}

	@Transactional
	@Override
	public void deleteFlash(int orderId, int width) {
		Order<?> order = repository.getOrder(orderId);
		xService.deleteFlash(order, width);
	}
}
