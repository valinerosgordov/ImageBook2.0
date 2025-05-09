package ru.minogin.core.server.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.*;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.server.ServiceLogger;
import ru.minogin.core.server.reflection.ReflectionUtils;

import java.util.*;

/**
 * Thread-safety: this class in not thread-safe.
 *
 * @author Andrey Minogin
 * @version 1.0, 02/28/11
 *
 */
public class Dehibernator {
    private static final Logger logger = Logger.getLogger(Dehibernator.class);

    private IdentityHashMap<Object, Object> processed;

    @SuppressWarnings("unchecked")
    public <T> T clean(T object) {
        processed = new IdentityHashMap<Object, Object>();

        logger.debug("Cleaning: " + (object != null ? object.getClass() : null));
        return (T) doClean(object);
    }

    @SuppressWarnings("unchecked")
    private Object doClean(final Object dirty) {
        logger.debug("Do clean: " + (dirty != null ? dirty.getClass() : null));

        if (dirty == null)
            return null;

        if (processed.containsKey(dirty)) {
            logger.debug("Object already cleaned, skipping.");

            return processed.get(dirty);
        }

        if (isPrimitive(dirty)) {
            logger.debug("Object is primitive, skipping.");

            return dirty;
        }

        if (dirty instanceof PersistentList) {
            logger.debug("Object is a PersistentList");

            PersistentList dirtyList = (PersistentList) dirty;
            List<Object> cleanList = new ArrayList<Object>();
            processed.put(dirtyList, cleanList);
            if (dirtyList.wasInitialized()) {
                for (Object value : dirtyList) {
                    cleanList.add(doClean(value));
                }
            }
            return cleanList;
        }

        if (dirty instanceof PersistentBag) {
            logger.debug("Object is a PersistentBag");

            PersistentBag dirtyList = (PersistentBag) dirty;
            List<Object> cleanList = new ArrayList<Object>();
            processed.put(dirtyList, cleanList);
            if (dirtyList.wasInitialized()) {
                for (Object value : dirtyList) {
                    cleanList.add(doClean(value));
                }
            }
            return cleanList;
        }

        if (dirty instanceof PersistentSortedSet) {
            logger.debug("Object is a PersistentSortedSet");

            PersistentSortedSet dirtySet = (PersistentSortedSet) dirty;
            Set<Object> cleanSet = new TreeSet<Object>();
            processed.put(dirtySet, cleanSet);
            if (dirtySet.wasInitialized()) {
                for (Object value : dirtySet) {
                    cleanSet.add(doClean(value));
                }
            }
            return cleanSet;
        }

        if (dirty instanceof PersistentSet) {
            logger.debug("Object is a PersistentSet");

            PersistentSet dirtySet = (PersistentSet) dirty;
            Set<Object> cleanSet = new HashSet<Object>();
            processed.put(dirtySet, cleanSet);
            if (dirtySet.wasInitialized()) {
                for (Object value : dirtySet) {
                    cleanSet.add(doClean(value));
                }
            }
            return cleanSet;
        }

        if (dirty instanceof PersistentMap) {
            logger.debug("Object is a PersistentMap");

            PersistentMap dirtyMap = (PersistentMap) dirty;
            Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
            processed.put(dirtyMap, cleanMap);
            if (dirtyMap.wasInitialized()) {
                for (Object key : dirtyMap.keySet()) {
                    Object value = dirtyMap.get(key);
                    cleanMap.put(doClean(key), doClean(value));
                }
            }
            return cleanMap;
        }

        if (dirty instanceof List) {
            logger.debug("Object is a List");

            List<Object> dirtyList = (List<Object>) dirty;
            List<Object> cleanList = new ArrayList<Object>();
            processed.put(dirtyList, cleanList);
            try {
                for (Object value : dirtyList) {
                    cleanList.add(doClean(value));
                }
            } catch (ConcurrentModificationException e) {
                ServiceLogger.warn(">>>>>>> ConcurrentModificationException on: "
                        + dirty.getClass());
                ServiceLogger.warn("2: " + dirty);
                Object item = dirtyList.get(0);
                ServiceLogger.warn("3: " + item.getClass());
                ServiceLogger.warn("4: " + item);
                throw e;
            }
            return cleanList;
        }

        if (dirty instanceof LinkedHashMap) {
            logger.debug("Object is a LinkedHashMap");

            Map<Object, Object> dirtyMap = (Map<Object, Object>) dirty;
            Map<Object, Object> cleanMap = new LinkedHashMap<Object, Object>();
            processed.put(dirtyMap, cleanMap);
            for (Object key : dirtyMap.keySet()) {
                Object value = dirtyMap.get(key);
                cleanMap.put(doClean(key), doClean(value));
            }
            return cleanMap;
        }

        if (dirty instanceof HashMap) {
            logger.debug("Object is a HashMap");

            Map<Object, Object> dirtyMap = (Map<Object, Object>) dirty;
            Map<Object, Object> cleanMap = new HashMap<Object, Object>();
            processed.put(dirtyMap, cleanMap);
            for (Object key : dirtyMap.keySet()) {
                Object value = dirtyMap.get(key);
                cleanMap.put(doClean(key), doClean(value));
            }
            return cleanMap;
        }

        if (dirty instanceof LinkedHashSet<?>) {
            logger.debug("Object is a LinkedHashSet");

            Set<Object> dirtySet = (LinkedHashSet<Object>) dirty;
            Set<Object> cleanSet = new LinkedHashSet<Object>();
            processed.put(dirtySet, cleanSet);
            for (Object value : dirtySet) {
                cleanSet.add(doClean(value));
            }
            return cleanSet;
        }

        if (dirty instanceof HashSet<?>) {
            logger.debug("Object is a HashSet");

            Set<Object> dirtySet = (HashSet<Object>) dirty;
            Set<Object> cleanSet = new HashSet<Object>();
            processed.put(dirtySet, cleanSet);
            for (Object value : dirtySet) {
                cleanSet.add(doClean(value));
            }
            return cleanSet;
        }

        if (dirty instanceof TreeSet<?>) {
            logger.debug("Object is a TreeSet");

            Set<Object> dirtySet = (TreeSet<Object>) dirty;
            Set<Object> cleanSet = new TreeSet<Object>();
            processed.put(dirtySet, cleanSet);
            for (Object value : dirtySet) {
                cleanSet.add(doClean(value));
            }
            return cleanSet;
        }

        Object implementation = dirty;
        if (dirty instanceof HibernateProxy) {
            logger.debug("Object is a HibernateProxy");

//            implementation = Hibernate.unproxy(dirty);

            HibernateProxy proxy = (HibernateProxy) dirty;
            LazyInitializer lazyInitializer = proxy.getHibernateLazyInitializer();
            if (lazyInitializer.isUninitialized()) {
                logger.debug("It is uninitialized, skipping");

                processed.put(dirty, null);
                return null;
            } else {
                logger.debug("It is initialized, getting implementation");

                implementation = lazyInitializer.getImplementation();
            }
        }

        if (implementation instanceof Bean) {
            logger.debug("Object is a Bean");

            Bean bean = (Bean) implementation;
            processed.put(dirty, bean);
            for (String property : bean.getPropertyNames()) {
                logger.debug("Processing property " + property);

                bean.set(property, doClean(bean.get(property)));
            }
            return bean;
        }

        logger.debug("Object is a POJO");
        Object object = implementation;
        processed.put(dirty, object);
        for (String property : ReflectionUtils.getProperties(object)) {
            logger.debug("Processing property " + property);

            Object value = ReflectionUtils.get(object, property);
            ReflectionUtils.setIfPossible(object, property, doClean(value));
        }
        return object;
    }

    private boolean isPrimitive(Object object) {
        if (object instanceof String)
            return true;

        if (object instanceof Boolean)
            return true;

        if (object instanceof Integer)
            return true;

        if (object instanceof Long)
            return true;

        if (object instanceof Float)
            return true;

        if (object instanceof Double)
            return true;

        if (object instanceof Date)
            return true;

        if (object instanceof Enum)
            return true;

        Class<? extends Object> xClass = object.getClass();
        if (xClass.isPrimitive())
            return true;

        return false;
    }
}
