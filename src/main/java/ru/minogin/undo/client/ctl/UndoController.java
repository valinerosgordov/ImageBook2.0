package ru.minogin.undo.client.ctl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.web.bindery.event.shared.EventBus;
import ru.minogin.gwt.client.rpc.XAsyncCallback;
import ru.minogin.undo.client.service.UndoRemoteService;
import ru.minogin.undo.client.service.UndoRemoteServiceAsync;
import ru.minogin.undo.client.view.UndoView;
import ru.minogin.undo.shared.UndoInfo;

public class UndoController implements UndoPresenter {
	private final UndoView view;

	private EventBus eventBus;
	private UndoRemoteServiceAsync service = GWT.create(UndoRemoteService.class);

	public UndoController(UndoView view, EventBus eventBus) {
		this.view = view;
		this.eventBus = eventBus;
		view.setPresenter(this);
	}

	private int count = 0;

	public void start() {
		eventBus.addHandler(MayUndoEvent.TYPE, new MayUndoHandler() {
			@Override
			public void onMayUndo() {
				count++;
				view.showUndoMessage(count);
				view.enableUndoButton();
			}
		});

		eventBus.addHandler(PlaceChangeEvent.TYPE, new PlaceChangeEvent.Handler() {
			@Override
			public void onPlaceChange(PlaceChangeEvent event) {
				reset();
			}
		});

		eventBus.addHandler(ResetUndoStackEvent.TYPE, new ResetUndoStackHandler() {
			@Override
			public void onReset() {
				reset();
			}
		});
	}

	private void reset() {
		service.resetUndoStack(new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				count = 0;
				view.hideUndoPanel();
			}
		});
	}

	@Override
	public void onUndo() {
		view.disableUndoButton();

		service.undo(new XAsyncCallback<UndoInfo>() {
			@Override
			public void onSuccess(UndoInfo info) {
				if (count > 0)
					count--;

				if (count > 0)
					view.showUndoMessage(count);
				else
					view.hideUndoPanel();

				view.enableUndoButton();

				eventBus.fireEvent(new UndoEvent(info));
			}
		});
	}
}
