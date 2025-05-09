package ru.imagebook.client.admin.view.product;

import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.format.Formatter;

import com.google.inject.Inject;

public class Format {
	private final CoreFactory coreFactory;

	@Inject
	public Format(CoreFactory coreFactory) {
		this.coreFactory = coreFactory;
	}

	public String formatType(int type, String locale) {
		Formatter formatter = coreFactory.createFormatter();
		return formatter.n2(type) + " - " + ProductType.values.get(type).getNonEmptyValue(locale);
	}
	
	public String formatCoverLamination(int lam, String locale) {
		Formatter formatter = coreFactory.createFormatter();
		return formatter.n2(lam) + " - " + CoverLamination.values.get(lam).getNonEmptyValue(locale);
	}

	public String formatPageLamination(int lam, String locale) {
		Formatter formatter = coreFactory.createFormatter();
		return formatter.n2(lam) + " - " + PageLamination.values.get(lam).getNonEmptyValue(locale);
	}
}
