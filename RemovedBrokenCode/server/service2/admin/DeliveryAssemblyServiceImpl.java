package ru.imagebook.server.service2.admin;

import org.springframework.beans.factory.annotation.Autowired;

import ru.imagebook.client.admin.service.DeliveryAssemblyService;
import ru.imagebook.server.repository2.admin.DeliveryAssemblyRepository;

public class DeliveryAssemblyServiceImpl implements DeliveryAssemblyService {
	private final DeliveryAssemblyRepository repository;
	
	@Autowired
	public DeliveryAssemblyServiceImpl(DeliveryAssemblyRepository repository) {
		this.repository = repository;
	}
}
