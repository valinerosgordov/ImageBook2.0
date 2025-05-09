package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

import java.util.List;
import java.util.Map;

public class LoadBonusCodesWithOrderInfoResultMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;
	public static final String BONUS_CODE_ORDERS = "bonusCodeOrders";

	LoadBonusCodesWithOrderInfoResultMessage() {}

	public LoadBonusCodesWithOrderInfoResultMessage(Map<BonusCode, List<Order<?>>> map) {
		super(ActionMessages.LOAD_BONUS_CODES_WITH_ORDER_INFO_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(BONUS_CODE_ORDERS, map);
	}

	public Map<BonusCode, List<Order<?>>> getBonusCodeOrders() {
		return get(BONUS_CODE_ORDERS);
	}
}
