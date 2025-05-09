package ru.minogin.data.server.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.event.internal.DefaultReplicateEventListener;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.ReplicateEventListener;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;

public class IdPreservingReplicateEventListener extends
		DefaultReplicateEventListener {
	private static final long serialVersionUID = -3446844724245252763L;

	private static final Logger logger = Logger
			.getLogger(IdPreservingReplicateEventListener.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	protected Serializable performSaveOrReplicate(Object entity, EntityKey key,
			EntityPersister persister, boolean useIdentityColumn, Object anything,
			EventSource source, boolean requiresImmediateIdAccess) {
		if (key == null) {
			Serializable id = persister.getIdentifier(entity, source);
			key = new EntityKey(id, persister);

			logger.debug("Replacing null key with entity key (" + key
					+ ") overriding default behavior of DefaultReplicateEventListener");

			useIdentityColumn = false;
		}

		return super.performSaveOrReplicate(entity, key, persister,
				useIdentityColumn, anything, source, requiresImmediateIdAccess);
	}

	@PostConstruct
	public void registerListeners() {
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory)
				.getServiceRegistry().getService(EventListenerRegistry.class);
		EventListenerGroup<ReplicateEventListener> group = registry
				.getEventListenerGroup(EventType.REPLICATE);
		group.clear();
		group.appendListener(this);
		logger
				.debug("Replaced defaut replicate listener with IdPreservingReplicateEventListener");
	}
}
