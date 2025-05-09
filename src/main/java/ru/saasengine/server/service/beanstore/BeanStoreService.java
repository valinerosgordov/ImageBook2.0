package ru.saasengine.server.service.beanstore;

import ru.minogin.core.client.bean.PersistentBean;
import ru.minogin.core.client.bean.PersistentIdBean;

public interface BeanStoreService {
	<B extends PersistentBean> B load(String id);

	void save(String id, PersistentBean bean);
	
	void save(PersistentIdBean bean);
}
