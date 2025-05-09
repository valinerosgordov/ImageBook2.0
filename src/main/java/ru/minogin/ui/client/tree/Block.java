package ru.minogin.ui.client.tree;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class Block extends Composite {
	private final Widget widget;
	private final Block parentBlock;
	private final List<Block> childBlocks = new ArrayList<Block>();

	public Block(Widget widget, Block parentBlock) {
		this.widget = widget;
		this.parentBlock = parentBlock;
	}
	
	public Widget getWidget() {
		return widget;
	}

	public Block getParentBlock() {
		return parentBlock;
	}

	public void addChildBlock(Block block) {
		childBlocks.add(block);
	}

	public boolean hasChildren() {
		return !childBlocks.isEmpty();
	}

	public List<Block> getChildBlocks() {
		return childBlocks;
	}
	
	public void clear() {
		childBlocks.clear();
	}
}
