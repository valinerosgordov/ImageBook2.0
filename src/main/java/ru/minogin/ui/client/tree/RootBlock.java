package ru.minogin.ui.client.tree;

import com.google.gwt.user.client.ui.VerticalPanel;

public class RootBlock extends Block {
	private VerticalPanel panel;

	public RootBlock() {
		super(null, null);

		panel = new VerticalPanel();
		initWidget(panel);
	}

	@Override
	public void addChildBlock(Block block) {
		super.addChildBlock(block);

		panel.add(block);
	}

	@Override
	public void clear() {
		super.clear();
		
		panel.clear();
	}
}
