package ru.saasengine.server.service.beanstore;

import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.bean.PersistentBean;
import ru.minogin.core.client.bean.PersistentIdBean;
import ru.minogin.core.client.serialization.Serializer;
import ru.saasengine.server.repository.beanstore.BeanStoreRepository;

public class BeanStoreServiceImpl implements BeanStoreService {
	private final BeanStoreRepository repository;
	private final CoreFactory coreFactory;

	public BeanStoreServiceImpl(BeanStoreRepository repository, CoreFactory coreFactory) {
		this.repository = repository;
		this.coreFactory = coreFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <B extends PersistentBean> B load(String id) {
		String json = repository.getJSON(id);
		if (json == null)
			return null;

		Serializer serializer = coreFactory.getSerializer();
		
		return (B) serializer.deserialize(json);
	}

	@Override
	public void save(String id, PersistentBean bean) {
		Serializer serializer = coreFactory.getSerializer();
		String json = serializer.serialize(bean);
		repository.saveJSON(id, json);
	}
	
	@Override
	public void save(PersistentIdBean bean) {
		save(bean.getId(), bean);
	}
}
