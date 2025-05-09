package ru.minogin.core.client.gxt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import ru.minogin.core.client.constants.CommonConstants;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public abstract class ListFieldSet<T, F extends Widget> extends FieldSet {
	private final CommonConstants appConstants;
	private final GxtMessages messages;

	private Collection<T> objects;
	private Collection<Block<T, F>> blocks;
	private int minCount = 0;
	private String hint;
	private String addButtonText;
	private String deleteButtonText;

	public ListFieldSet(final Collection<T> objects, final CommonConstants appConstants,
			final GxtMessages messages) {
		this.objects = objects;
		this.appConstants = appConstants;
		this.messages = messages;
		addButtonText = appConstants.add();
		deleteButtonText = appConstants.delete();

		setCollapsible(true);
	}

	public abstract T createObject();

	public abstract F createField(T object);

	public abstract T fetchObject(F field);

	public void fetch() {
		objects.clear();
		for (Block<T, F> block : blocks) {
			T object = fetchObject(block.getField());
			objects.add(object);
		}
	}

	public F getFirstField() {
		if (blocks.isEmpty())
			return null;

		Block<T, F> block = blocks.iterator().next();
		return block.getField();
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	@Override
	protected void onRender(Element parent, int pos) {
		super.onRender(parent, pos);

		setLayout(new RowLayout());

		if (hint != null) {
			Html html = new Html(hint);
			html.setStyleName("hint-plugin");
			html.setStyleAttribute("margin-bottom", "5px");
			add(html);
		}

		blocks = new ArrayList<Block<T, F>>();

		for (T object : objects) {
			F field = createField(object);
			Block<T, F> block = new Block<T, F>(object, field);
			blocks.add(block);
			RowData data = new RowData();
			data.setMargins(new Margins(0, 0, 5, 0));
			add(block, data);
		}

		ButtonBar buttonBar = new ButtonBar();
		buttonBar.add(new Button(addButtonText, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				T object = createObject();
				objects.add(object);

				F field = createField(object);
				Block<T, F> block = new Block<T, F>(object, field);
				blocks.add(block);
				RowData data = new RowData();
				data.setMargins(new Margins(0, 0, 5, 0));
				ListFieldSet.this.insert(block, ListFieldSet.this.getItemCount() - 1, data);
				ListFieldSet.this.layout();
			}
		}));
		buttonBar.add(new Button(deleteButtonText, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				int removeCount = 0;
				Iterator<Block<T, F>> iterator = blocks.iterator();
				while (iterator.hasNext()) {
					Block<T, F> block = iterator.next();
					if (block.isSelected()) {
						removeCount++;
					}
				}
				int count = blocks.size();
				if (count - removeCount < minCount) {
					MessageBox.alert(appConstants.error(), messages.minCountError(minCount), null);
					return;
				}

				iterator = blocks.iterator();
				while (iterator.hasNext()) {
					Block<T, F> block = iterator.next();
					if (block.isSelected()) {
						objects.remove(block.getObject());
						ListFieldSet.this.remove(block);
						iterator.remove();
					}
				}
			}
		}));
		add(buttonBar);
	}

	public void setAddButtonText(String addButtonText) {
		this.addButtonText = addButtonText;
	}

	public void setDeleteButtonText(String deleteButtonText) {
		this.deleteButtonText = deleteButtonText;
	}
}
