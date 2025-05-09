package ru.minogin.ui.client.field.upload;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import ru.minogin.ui.client.files.DropFilesEvent;
import ru.minogin.ui.client.files.DropFilesHandler;
import ru.minogin.ui.client.files.FilesPanel;

/** Active field for file uploads supporting:<br/>
 * 1. file drag and drop;<br/>
 * 2. multiple files upload.
 * <br/><br/>
 * This field fires {@link DropFilesEvent}.
 * 
 * @author Andrey Minogin */
public class ActiveFileUpload extends Composite {
	private final FilesPanel panel;
	private final HTML html;

	private State state = State.NORMAL;
	private String text;
	private String emptyText;

	public ActiveFileUpload() {
		Resources.INSTANCE.css().ensureInjected();

		panel = new FilesPanel();
		panel.addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				if (state == State.NORMAL)
					setHoverState();
			}
		});
		panel.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				if (state == State.HOVER)
					setNormalState();
			}
		});

		html = new HTML();
		html.addStyleName(Resources.INSTANCE.css().html());
		html.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (state == State.NORMAL)
					setHoverState();
			}
		});
		html.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (state == State.HOVER)
					setNormalState();
			}
		});
		panel.add(html);

		setText(null);

		initWidget(panel);
	}

	public void setText(String text) {
		this.text = text;
		if (!Strings.isNullOrEmpty(text)) {
			html.setText(text);
			html.removeStyleName(Resources.INSTANCE.css().htmlEmpty());
		}
		else {
			html.addStyleName(Resources.INSTANCE.css().htmlEmpty());
			if (!Strings.isNullOrEmpty(emptyText))
				html.setText(emptyText);
			else
				html.setHTML("&nbsp");
		}
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
		setText(text);
	}

	private void setNormalState() {
		html.removeStyleName(Resources.INSTANCE.css().htmlHighlight());
		setText(text);
		state = State.NORMAL;
	}

	private void setHoverState() {
		html.addStyleName(Resources.INSTANCE.css().htmlHighlight());
		html.setText("Перетащите файлы сюда"); // TODO
		state = State.HOVER;
	}

	public void addDropFilesHandler(DropFilesHandler handler) {
		panel.addDropFilesHandler(handler);
	}
}
