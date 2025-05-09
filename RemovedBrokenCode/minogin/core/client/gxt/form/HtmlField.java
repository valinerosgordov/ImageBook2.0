package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

/**
 * @deprecated
 *
 */
public class HtmlField extends Field<String> {
	private HTML html;
	private HtmlFieldListener listener;

	@Override
	protected void onRender(final Element parent, final int index) {
		html = new HTML();
		html.setSize("100%", "100%");
		Element element = html.getElement();
		setElement(element);
		DOM.insertChild(parent, element, index);

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				doRender(parent, index);
			}
		});
	}

	private void doRender(Element parent, int index) {
		HtmlFieldServiceAsync service = GWT.create(HtmlFieldService.class);
		service.getEditor(value, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String editorHtml) {
				html.setHTML(editorHtml);

				if (listener != null)
					listener.onRendered();
			}

			@Override
			public void onFailure(Throwable caught) {
				html.setHTML("Ошибка загрузки текстового редактора. Пожалуйста, обновите страницу.");
			}
		});
	}

	private native void updateInputField()
	/*-{
		for ( var name in $wnd.FCKeditorAPI.Instances) {
			var oEditor = $wnd.FCKeditorAPI.Instances[name];
			oEditor.UpdateLinkedField();
		}
	}-*/;

	@Override
	public String getValue() {
		updateInputField();

		Node div = html.getElement().getChild(0);
		InputElement input = (InputElement) div.getChild(0);
		return input.getValue();
	}

	@Override
	public void setValue(final String value) {
		this.value = value;

		Timer timer = new Timer() {
			@Override
			public void run() {
				if (doSetValue())
					cancel();
			}
		};
		timer.scheduleRepeating(200);
	}

	private boolean doSetValue() {
		try {
			Node div = html.getElement().getChild(0);

			IFrameElement iFrame = (IFrameElement) div.getChild(1);
			Document document = iFrame.getContentDocument();
			TableCellElement cell = (TableCellElement) document
					.getElementById("xEditingArea");
			Node child = cell.getChild(0);
			try {
				iFrame = (IFrameElement) child;
				document = iFrame.getContentDocument();
				BodyElement body = document.getBody();
				body.setInnerHTML(value);
				return true;
			}
			catch (Throwable t) {
				TextAreaElement textArea = (TextAreaElement) child;
				textArea.setValue(value);
				return true;
			}
		}
		catch (Throwable t) {
			return false;
		}
	}

	public void setListener(HtmlFieldListener listener) {
		this.listener = listener;
	}
}
