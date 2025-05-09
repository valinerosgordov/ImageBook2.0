package ru.imagebook.client.admin.ctl.order;

import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.Vellum;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DataLoadedMessage extends BaseMessage {
    private static final long serialVersionUID = -6316500325136670641L;

    public static final String PRODUCTS = "products";
    public static final String COLORS = "colors";
    public static final String ACTIONS = "actions";
    public static final String FLYLEAFS = "flyleafs";
    public static final String VELLUMS = "vellums";

    DataLoadedMessage() {
    }

    public DataLoadedMessage(Map<Integer, List<Product>> products, List<Color> colors,
                             List<BonusAction> actions, List<Flyleaf> flyleafs, List<Vellum> vellums) {
        super(OrderMessages.DATA_LOADED);

        addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

        set(PRODUCTS, products);
        set(COLORS, colors);
        set(ACTIONS, actions);
        set(FLYLEAFS, flyleafs);
        set(VELLUMS, vellums);
    }

    public Map<Integer, List<Product>> getProducts() {
        return get(PRODUCTS);
    }

    public List<Color> getColors() {
        return get(COLORS);
    }

    public List<BonusAction> getActions() {
        return get(ACTIONS);
    }

    public List<Flyleaf> getFlyleafs() {
        return get(FLYLEAFS);
    }

    public List<Vellum> getVellums() {
        return get(VELLUMS);
    }
}
