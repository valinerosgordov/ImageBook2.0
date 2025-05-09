package ru.minogin.undo.server.action.child;

import java.util.List;

public interface Delegate<P, C> {
	Class<P> getParentClass();
	
	Class<C> getChildClass();
	
	P getParent(C child);

	List<C> getChildren(P parent);
}
