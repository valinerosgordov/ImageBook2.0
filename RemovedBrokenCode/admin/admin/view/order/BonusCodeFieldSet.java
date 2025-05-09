package ru.imagebook.client.admin.view.order;

import java.util.List;

import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.minogin.core.client.gxt.form.SelectField;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class BonusCodeFieldSet extends FieldSet {
	private SelectField<BonusAction> actionField;
	private TextField<String> codeField;
	private BonusCode code;

	public BonusCodeFieldSet(List<BonusAction> actions, OrderConstants constants) {
		setLayout(new FormLayout());
		setCollapsible(true);

		actionField = new SelectField<BonusAction>();
		actionField.setFieldLabel(constants.actionField());
		actionField.add(null, "-");
		for (BonusAction action : actions) {
			actionField.add(action, action.getName());
		}
		add(actionField);

		codeField = new TextField<String>();
		codeField.setFieldLabel(constants.codeField());
		add(codeField);
	}

	public void setValue(BonusCode code) {
		this.code = code;

		if (code != null) {
			actionField.setXValue(code.getAction());
			codeField.setValue(code.getCode());
		}
		else {
			actionField.setXValue(null);
			codeField.setValue(null);
		}
	}

	public BonusCode getValue() {
		BonusAction action = actionField.getXValue();
		String codeValue = codeField.getValue();
		if (action == null)
			return null;

		if (code == null)
			code = new BonusCode();

		code.setAction(action);
		code.setCode(codeValue);
		return code;
	}
}
