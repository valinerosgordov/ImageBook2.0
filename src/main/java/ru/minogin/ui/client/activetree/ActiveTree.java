package ru.minogin.ui.client.activetree;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import gwtquery.plugins.draggable.client.DraggableOptions.RevertOption;
import gwtquery.plugins.draggable.client.events.DragEvent;
import gwtquery.plugins.draggable.client.events.DragEvent.DragEventHandler;
import gwtquery.plugins.draggable.client.events.DragStartEvent;
import gwtquery.plugins.draggable.client.events.DragStartEvent.DragStartEventHandler;
import gwtquery.plugins.draggable.client.events.DragStopEvent;
import gwtquery.plugins.draggable.client.events.DragStopEvent.DragStopEventHandler;
import gwtquery.plugins.droppable.client.events.DropEvent;
import gwtquery.plugins.droppable.client.events.DropEvent.DropEventHandler;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent;
import gwtquery.plugins.droppable.client.events.OutDroppableEvent.OutDroppableEventHandler;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent;
import gwtquery.plugins.droppable.client.events.OverDroppableEvent.OverDroppableEventHandler;
import gwtquery.plugins.droppable.client.gwt.DroppableWidget;
import ru.minogin.ui.client.activetree.DropTarget.Type;
import ru.minogin.ui.client.tree.*;

import java.util.*;

/** Active tree for displaying, selecting and reordering hierarchical items.
 * Every item is rendered as a row of widgets.
 * <br/><br/>
 * Values are provided by {@link #setRootValue(Object)} or {@link #setValues(Collection)}.
 * <br/><br/>
 * You may add tools (e.g. buttons) for the tree items using {@link TreeTools}.
 * 
 * @author Andrey Minogin
 * 
 * @param <T> - tree item type. */
public class ActiveTree<T> extends Composite implements HasMouseOutHandlers {
	public static final int CHILD_DROP_TOLERANCE = 30;

	private final TreeRenderer<T> renderer;

	private Tree tree;
	private Set<T> openedValues = new HashSet<T>();
	private boolean dragging;
	private boolean dragOver;

	private final TreeDelegate<T> delegate;
	private TreeItem<T> selectedItem;
	private Map<T, TreeItem<T>> treeItems = new HashMap<T, TreeItem<T>>();

	private DivElement div;

	/**
	 * @param renderer - renders specified item as a row of widgets.
	 * @param delegate - provides acces to the child elements of an item.
	 */
	public ActiveTree(TreeRenderer<T> renderer, TreeDelegate<T> delegate) {
		this.renderer = renderer;
		this.delegate = delegate;

		Resources.INSTANCE.css().ensureInjected();

		FlowPanel panel = new FlowPanel();
		panel.addStyleName(Resources.INSTANCE.css().treePanel());

		tree = new Tree();
		tree.addOpenHandler(new OpenHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onOpen(OpenEvent event) {
				final TreeItem<T> item = (TreeItem<T>) event.getWidget();
				openedValues.add(item.getValue());
			}
		});
		tree.addCloseHandler(new CloseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onClose(CloseEvent event) {
				final TreeItem<T> item = (TreeItem<T>) event.getWidget();
				openedValues.remove(item.getValue());
			}
		});

		DroppableWidget<Tree> dropTree = new DroppableWidget<Tree>(tree);
		dropTree.addDropHandler(new DropEventHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void onDrop(DropEvent event) {
				TreeItem<T> item = (TreeItem<T>) event.getDraggableWidget();
				DropTarget<T> dropTarget = getDropTarget(item);
				if (dropTarget != null) {
					fireEvent(new MoveEvent<T>(item.getValue(), dropTarget.getTarget()
							.getValue(), dropTarget.getIndex()));
				}
			}
		});
		dropTree.addOverDroppableHandler(new OverDroppableEventHandler() {
			@Override
			public void onOverDroppable(OverDroppableEvent event) {
				dragOver = true;
			}
		});
		dropTree.addOutDroppableHandler(new OutDroppableEventHandler() {
			@Override
			public void onOutDroppable(OutDroppableEvent event) {
				dragOver = false;
			}
		});
		panel.add(dropTree);

		initWidget(panel);
	}

	@SuppressWarnings("unchecked")
	public void setRootValue(T value) {
		setValues(Arrays.asList(value));
	}

	public void setValues(Collection<T> values) {
		tree.clear();
		treeItems.clear();

		for (T value : values) {
			addItem(value, null);
		}
	}

	@SuppressWarnings("unchecked")
	private void addItem(final T value, TreeItem<T> parentItem) {
		final TreeItem<T> item = new TreeItem<T>(value, renderer) {
			@Override
			protected void onOver(MouseOverEvent sourceEvent) {
				ActiveTree.this.fireEvent(new ItemMouseOverEvent<T>(value, this,
						sourceEvent));
			}

			@Override
			protected void onOut(Widget widget, MouseOutEvent sourceEvent) {
				ActiveTree.this.fireEvent(new ItemMouseOutEvent<T>(value, widget, this,
						sourceEvent));
			}

			@Override
			protected void onSelect() {
				ActiveTree.this.setSelected(this);
			}
		};
		item.setDistance(5);
		item.setDraggingCursor(Cursor.MOVE);
		item.setDraggingOpacity(0.8f);
		item.setRevert(RevertOption.ON_INVALID_DROP);
		item.addDragStartHandler(new DragStartEventHandler() {
			@Override
			public void onDragStart(DragStartEvent event) {
				dragging = true;
				dragOver = false;
			}
		});
		item.addDragStopHandler(new DragStopEventHandler() {
			@Override
			public void onDragStop(DragStopEvent event) {
				dragging = false;
			}
		});
		item.addDragHandler(new DragEventHandler() {
			@Override
			public void onDrag(DragEvent event) {
				if (dragOver) {
					TreeItem<T> item = (TreeItem<T>) event.getDraggableWidget();
					DropTarget<T> dropTarget = getDropTarget(item);
					if (dropTarget != null) {
						if (dropTarget.getType() == Type.NEIGHBOUR) {
							TreeItem<T> target = dropTarget.getTarget();
							TreeItem<T> childItem = (TreeItem<T>) tree
									.getChildWidgets(target).get(dropTarget.getIndex());
							DivElement div = getDiv();
							div.setClassName(Resources.INSTANCE.css().drop());
							div.getStyle().setWidth(100, Unit.PX);
							div.getStyle().setHeight(0, Unit.PX);
							childItem.getElement().insertFirst(div);
						}
						else {
							TreeItem<T> target = dropTarget.getTarget();
							DivElement div = getDiv();
							div.setClassName(Resources.INSTANCE.css().dropChild());
							div.getStyle().setWidth(100, Unit.PX);
							div.getStyle().setHeight(20, Unit.PX);
							target.getElement().appendChild(div);
						}
					}
				}
			}
		});

		treeItems.put(value, item);

		tree.add(item, parentItem);

		if (openedValues.contains(value))
			tree.setOpen(item, true);

		for (T childValue : delegate.getChildren(value)) {
			addItem(childValue, item);
		}
	}

	private DivElement getDiv() {
		if (div == null)
			div = Document.get().createDivElement();
		return div;
	}

	@SuppressWarnings("unchecked")
	private DropTarget<T> getDropTarget(TreeItem<T> source) {
		int top = source.getAbsoluteTop();
		int bottom = top + source.getOffsetHeight();
		int left = source.getAbsoluteLeft();

		for (TreeItem<T> item : treeItems.values()) {
			if (item == source)
				continue;

			if (item.getAbsoluteTop() < bottom
					&& bottom < item.getAbsoluteTop() + item.getOffsetHeight()
					&& left < item.getAbsoluteLeft() + CHILD_DROP_TOLERANCE) {
				TreeItem<T> parent = (TreeItem<T>) tree.getParentWidget(item);
				int index = tree.getChildIndex(item, parent);
				return new DropTarget<T>(Type.NEIGHBOUR, parent, index);
			}

			if (item.getAbsoluteTop() < top
					&& top < item.getAbsoluteTop() + item.getOffsetHeight()
					&& left > item.getAbsoluteLeft() + CHILD_DROP_TOLERANCE) {
				return new DropTarget<T>(Type.CHILD, item, 0);
			}
		}
		return null;
	}

	public HandlerRegistration addItemMouseOverHandler(
			ItemMouseOverHandler<T> handler) {
		return addHandler(handler, ItemMouseOverEvent.TYPE);
	}

	public HandlerRegistration addItemMouseOutHandler(
			ItemMouseOutHandler<T> handler) {
		return addHandler(handler, ItemMouseOutEvent.TYPE);
	}

	public HandlerRegistration addSelectHandler(SelectHandler<T> handler) {
		return addHandler(handler, SelectEvent.TYPE);
	}

	public boolean isDragging() {
		return dragging;
	}

	public void expandAll() {
		for (Widget widget : tree.getChildWidgets()) {
			expand(widget);
		}
	}

	private void expand(Widget widget) {
		tree.setOpen(widget, true);
		for (Widget child : tree.getChildWidgets(widget)) {
			expand(child);
		}
	}

	public void expandFirstLevel() {
		for (Widget widget : tree.getChildWidgets()) {
			tree.setOpen(widget, true);
		}
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public void setSelected(T value) {
		TreeItem<T> item = treeItems.get(value);
		setSelected(item);
	}

	private void setSelected(TreeItem<T> item) {
		if (selectedItem != null && selectedItem != item)
			selectedItem.deselect();

		if (item != null && item != selectedItem)
			item.select();

		selectedItem = item;

		if (item != null)
			ActiveTree.this.fireEvent(new SelectEvent<T>(item.getValue()));
	}

	/**
	 * Add item reordering handler.
	 */
	public HandlerRegistration addMoveHandler(MoveHandler<T> handler) {
		return addHandler(handler, MoveEvent.TYPE);
	}
}
