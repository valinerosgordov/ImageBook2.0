package ru.imagebook.shared.model.editor;

import java.io.Serializable;
import java.util.*;

public enum NotificationType implements Serializable {
    CREATE_PACKAGE_ORDER(1, "Создание пакетного заказа"),
    CHANGE_IMAGE_ON_COMMON_PAGE(2, "Изменение изображения на общем листе"),
    CHANGE_IMAGE_ON_INDIVIDUAL_PAGE(3, "Изменение изображения на индивидуальном листе"),
    CHANGE_COMMON_ON_INDIVIDUAL_PAGE(4, "Изменение листа общего типа на индивидуальный"),
    CHANGE_IMAGE(5, "Изменение изображения");

    private static final List<NotificationType> AS_LIST = Collections.unmodifiableList(Arrays.asList(values()));
    private static final Map<Integer, NotificationType> BY_MNEMO = prepareIndexedByMnemoMap(AS_LIST, true);

    public static final Map<Integer, NotificationType> prepareIndexedByMnemoMap(final List<NotificationType> c, final boolean makeUnmodifiable) {
        if (c == null) return null;
        if (c.isEmpty()) return makeUnmodifiable ? Collections.<Integer, NotificationType>emptyMap() : new LinkedHashMap<Integer, NotificationType>();
        final Map<Integer, NotificationType> result = new LinkedHashMap<Integer, NotificationType>(c.size(), 0.9f);
        for (final NotificationType t : c) result.put(t.getType(), t);
        return makeUnmodifiable ? Collections.unmodifiableMap(result) : result;
    }

    private final int type;
    private final String message;

    private NotificationType(final int type, final String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() { return type; }

    public String getMessage() { return message; }

    public static NotificationType getNotificationTypeById(final int id) {
        return BY_MNEMO.get(id);
    }
}
