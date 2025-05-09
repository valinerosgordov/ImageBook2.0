package ru.imagebook.client.faq.view;

import java.util.Date;

import ru.imagebook.client.faq.ctl.QuestionPresenter;
import ru.imagebook.shared.model.Question;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gwt.ui.MessageBox;
import ru.minogin.core.client.gxt.GxtConstants;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;

import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class QuestionsViewImpl implements QuestionsView {
	@Inject
	private QuestionsConstants constants;
	@Inject
	private GxtConstants gxtConstants;		
	@Inject
	private CommonConstants commonConstants;
	
	private QuestionPresenter presenter;
	
	private TextArea questionField;
	private XTextField nameField;
	private EmailField emailField;
	private Label questionResultLabel;
	private FormPanel formPanel;
	private FlexTable panel;
	private Timer timer;
	
	@Override
	public void setPresenter(QuestionPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public Widget asWidget() {
	    	formPanel = new XFormPanel();
	    	panel = new FlexTable();
	    	
		questionResultLabel = new Label(constants.questionAsked());
		questionResultLabel.setVisible(false);
		RootPanel.get("questionResult").add(questionResultLabel);
		RootPanel.get("askQuestion").add(new HTML("<h2>" + constants.askQuestion() + "</h2>"));
		
	    	int row = 0;
		panel.setWidget(row, 0, new HTML(constants.nameField() + ": "));
		nameField = new XTextField();
		nameField.setWidth("25%");
		panel.setWidget(row, 1, nameField);
		row++;
		
		panel.setWidget(row, 0, new HTML(constants.emailField() + ": "));
		emailField = new EmailField();
		emailField.setWidth("33%");
		panel.setWidget(row, 1, emailField);
		row++;
		
		panel.setText(row, 1, constants.emailComment());
		row++;
		
		HTML questionTitle = new HTML(constants.questionField() + ": * ");
		questionTitle.setWordWrap(false);
		panel.setWidget(row, 0, questionTitle);
		panel.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		questionField = new TextArea();
		questionField.setAllowBlank(false);
		questionField.setWidth("80%");
		questionField.setHeight(120);
		panel.setWidget(row, 1, questionField);
		row++;
		formPanel.add(panel);
		
		formPanel.addStyleName("question-panel");
		
		final Button button = new Button(constants.addButton(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.addQuestionButtonClicked();
			}
		});
		panel.setWidget(row, 1, button);
		
		timer = new Timer() {
			@Override
			public void run() {
				boolean enable = true;
				enable = emailField.isValid(true) && enable;
				enable = nameField.isValid(true) && enable;
				enable = questionField.isValid(true) && enable;
				if (enable != button.isEnabled()) {
					button.setEnabled(enable);
				}
			}
		};
		timer.run();
	    timer.scheduleRepeating(500);
		
		return formPanel;
	}
	
	@Override
	public void informAskResult() {
	    	panel.setVisible(false);
		questionResultLabel.setVisible(true);
		RootPanel.get("askQuestion").setVisible(false);
	}
	
	@Override
	public void alertTechFailure() {
		new MessageBox(commonConstants.error(), constants.techFailure(), commonConstants).show();
	}	

	@Override
	public void fetch(Question question) {
		question.setDate(new Date());
		question.setEmail(emailField.getValue());
		question.setQuestion(questionField.getValue());
		question.setName(nameField.getValue());
	}
}
