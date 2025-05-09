package ru.imagebook.shared.util;

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Function;

import ru.minogin.core.client.bean.EntityBean;

public class Functions {
    public static final Function<EntityBean, Integer> ENTITY_TO_ID_FUNCTION = new Function<EntityBean, Integer>() {
        @Override
        public Integer apply(EntityBean input) {
            checkNotNull(input);
            return input.getId();
        }
    };

    private Functions() {
    }
}
