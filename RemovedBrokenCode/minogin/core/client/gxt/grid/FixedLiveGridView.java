package ru.minogin.core.client.gxt.grid;

import com.extjs.gxt.ui.client.widget.grid.LiveGridView;

/**
 * <p>This is a bug fix for GXT 2.2.3.</p>
 * <p>When live grid is empty it starts to reload infinitely.
 * See <a href="http://www.sencha.com/forum/showthread.php?129777-Version-2.2.3-Livegrid-not-working-anymore-with-RpcProxy">this thread</a>
 * </p>
 * <p>This bug should be fixed in the next official GXT release.</p>
 *
 * @author Andrey Minogin
 */
public class FixedLiveGridView extends LiveGridView {
	@Override
	protected boolean isCached(int index) {
		if (liveStore.getCount() == 0)
			return true;
		else
			return super.isCached(index);
	}
}
