package ru.minogin.ui.client.tree;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class NormalBlock extends Block {
	private final FlexTable table;
	private final Image toggleImage;
	private final VerticalPanel childPanel;

	private boolean open;

	public NormalBlock(Widget widget, Block parentBlock) {
		super(widget, parentBlock);

		table = new FlexTable();
		initWidget(table);

		toggleImage = new Image(Resources.INSTANCE.treeExpand());
		toggleImage.addStyleName(Resources.INSTANCE.css().toggleImage());
		toggleImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setOpen(!open);
			}
		});
		table.setWidget(0, 0, toggleImage);

		table.setWidget(0, 1, widget);

		childPanel = new VerticalPanel();

		open = false;
		update();
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		if (this.open == open)
			return;

		this.open = open;
		update();

		if (open)
			onOpen();
		else
			onClose();
	}

	@Override
	public void addChildBlock(Block block) {
		super.addChildBlock(block);

		childPanel.add(block);
		update();
	}

	private void update() {
		if (hasChildren()) {
			if (open) {
				toggleImage.setResource(Resources.INSTANCE.treeCollapse());
				childPanel.setVisible(true);
				table.setWidget(1, 1, childPanel);
			}
			else {
				toggleImage.setResource(Resources.INSTANCE.treeExpand());
				childPanel.setVisible(false);
				if (table.getRowCount() > 1)
					table.removeRow(1);
			}
		}
		else {
			toggleImage.setResource(Resources.INSTANCE.treeNone());
		}
	}

	protected abstract void onClose();

	protected abstract void onOpen();

	@Override
	public void clear() {
		super.clear();

		childPanel.clear();
	}
}
