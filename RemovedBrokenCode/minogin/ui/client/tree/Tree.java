package ru.minogin.ui.client.tree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A tree widget implementation alternative to the GWT one.
 * 
 * @author Andrey Minogin
 *
 */
public class Tree extends Composite {
	private final RootBlock rootBlock = new RootBlock();
	private final Map<Widget, Block> blocks = new HashMap<Widget, Block>();

	public Tree() {
		Resources.INSTANCE.css().ensureInjected();

		initWidget(rootBlock);
	}

	public void add(final Widget widget) {
		add(widget, null);
	}

	public void add(final Widget widget, Widget parentWidget) {
		Block parentBlock = getBlock(parentWidget);
		NormalBlock block = new NormalBlock(widget, parentBlock) {
			@Override
			protected void onOpen() {
				Tree.this.fireEvent(new OpenEvent(widget));
			}

			@Override
			protected void onClose() {
				Tree.this.fireEvent(new CloseEvent(widget));
			}
		};
		blocks.put(widget, block);
		parentBlock.addChildBlock(block);
	}

	private Block getBlock(Widget widget) {
		if (widget == null)
			return rootBlock;
		else
			return blocks.get(widget);
	}

	public HandlerRegistration addOpenHandler(OpenHandler handler) {
		return addHandler(handler, OpenEvent.TYPE);
	}

	public HandlerRegistration addCloseHandler(CloseHandler handler) {
		return addHandler(handler, CloseEvent.TYPE);
	}

	public void clear() {
		blocks.clear();
		rootBlock.clear();
	}

	public void setOpen(Widget widget, boolean open) {
		NormalBlock normalBlock = (NormalBlock) getBlock(widget);
		if (normalBlock == null)
			throw new RuntimeException("No such widget");
		normalBlock.setOpen(open);
	}

	public List<Widget> getChildWidgets() {
		return getChildWidgets(null);
	}

	public List<Widget> getChildWidgets(Widget widget) {
		Block block = getBlock(widget);
		List<Widget> widgets = new ArrayList<Widget>();
		for (Block childBlock : block.getChildBlocks()) {
			widgets.add(childBlock.getWidget());
		}
		return widgets;
	}

	public Widget getParentWidget(Widget widget) {
		Block block = getBlock(widget);
		Block parentBlock = block.getParentBlock();
		return parentBlock.getWidget();
	}

	public int getChildIndex(Widget widget, Widget parentWidget) {
		return getChildWidgets(parentWidget).indexOf(widget);
	}
}
