package ru.imagebook.client.admin.view.site;

import java.util.List;

import ru.imagebook.shared.model.site.Page;
import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.client.gxt.GxtConstants;
import ru.minogin.core.client.gxt.form.KeyField;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.XHtmlField;
import ru.minogin.core.client.gxt.form.XTextArea;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class DocumentForm {
	private final SiteConstants constants;
	private final FormPanel formPanel;
	private final GxtConstants xgxtConstants;

	private TextField<String> keyField;
	private TextField<String> nameField;
	private TextField<String> titleField;
	private TextField<String> h1Field;
	private TextField<String> keywordsField;
	private TextField<String> descriptionField;
	private CheckBox wideField;
	private TextField<String> urlField;
	private CheckBox targetBlankField;
	private XHtmlField contentField;
	private TextArea footerField;
	private SelectField<Tag> tagField;
	private final List<Tag> tags;

	public DocumentForm(SiteConstants constants, FormPanel formPanel,
			GxtConstants xgxtConstants, List<Tag> tags) {
		this.constants = constants;
		this.formPanel = formPanel;
		this.xgxtConstants = xgxtConstants;
		this.tags = tags;
	}

	public void render() {
		keyField = new KeyField(xgxtConstants);
		keyField.setFieldLabel(constants.keyField());
		formPanel.add(keyField);

		nameField = new TextField<String>();
		nameField.setFieldLabel(constants.nameField());
		formPanel.add(nameField);

		titleField = new TextField<String>();
		titleField.setFieldLabel(constants.titleField());
		formPanel.add(titleField);

		keywordsField = new TextField<String>();
		keywordsField.setFieldLabel(constants.keywordsField());
		formPanel.add(keywordsField);

		descriptionField = new TextField<String>();
		descriptionField.setFieldLabel(constants.descriptionField());
		formPanel.add(descriptionField);

		h1Field = new TextField<String>();
		h1Field.setFieldLabel(constants.h1Field());
		formPanel.add(h1Field);

		wideField = new CheckBox();
		wideField.setFieldLabel(constants.wideField());
		wideField.setBoxLabel("");
		formPanel.add(wideField);

		urlField = new TextField<String>();
		urlField.setFieldLabel(constants.urlField());
		formPanel.add(urlField, new FormData(300, -1));

		targetBlankField = new CheckBox();
		targetBlankField.setFieldLabel(constants.targetBlankField());
		targetBlankField.setBoxLabel("");
		formPanel.add(targetBlankField);

		contentField = new XHtmlField();
		contentField.setHideLabel(true);
		formPanel.add(contentField, new FormData(800, 500));

		footerField = new XTextArea(constants.footerField(), true, formPanel);
		footerField.setHeight(200);

		tagField = new SelectField<Tag>(constants.tagField(), true, formPanel);
		for (Tag tag : tags) {
			tagField.add(tag, tag.getName());
		}
	}

	public void fetch(Page page) {
		page.setKey(keyField.getValue());
		page.setName(nameField.getValue());
		page.setTitle(titleField.getValue());
		page.setKeywords(keywordsField.getValue());
		page.setDescription(descriptionField.getValue());
		page.setH1(h1Field.getValue());
		page.setWide(wideField.getValue());
		page.setUrl(urlField.getValue());
		page.setTargetBlank(targetBlankField.getValue());
		page.setContent(contentField.getValue());
		page.setFooter(footerField.getValue());
		page.setTag(tagField.getXValue());
	}

	public void setValues(Page page) {
		keyField.setValue(page.getKey());
		nameField.setValue(page.getName());
		titleField.setValue(page.getTitle());
		keywordsField.setValue(page.getKeywords());
		descriptionField.setValue(page.getDescription());
		h1Field.setValue(page.getH1());
		wideField.setValue(page.isWide());
		urlField.setValue(page.getUrl());
		targetBlankField.setValue(page.isTargetBlank());
		String content = page.getContent();
		contentField.setValue(content);
		footerField.setValue(page.getFooter());
		tagField.setXValue(page.getTag());
	}
}
