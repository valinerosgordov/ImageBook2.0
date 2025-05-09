package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ColorController extends Controller {
	private final ColorView view;

	@Inject
	public ColorController(Dispatcher dispatcher, final ColorView view) {
		super(dispatcher);
		
		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(ProductMessages.SHOW_COLORS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadColorsMessage());
			}
		});

		addHandler(ProductMessages.LOAD_COLORS_RESULT, new MessageHandler<LoadColorsResultMessage>() {
			@Override
			public void handle(LoadColorsResultMessage message) {
				List<Color> colors = message.getColors();
				view.showColors(colors);
			}
		});

		addHandler(ProductMessages.COLORS_UPDATED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadColorsMessage());
			}
		});

		addHandler(ProductMessages.DELETE_COLORS_REQUEST,
				new MessageHandler<DeleteColorsRequestMessage>() {
					@Override
					public void handle(DeleteColorsRequestMessage message) {
						view.confirmDeleteColors(message.getColors());
					}
				});

		addHandler(ProductMessages.COLORS_DELETED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(new LoadColorsMessage());
			}
		});

		addHandler(ProductMessages.COLOR_USED_ERROR, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.alertColorUsed();
			}
		});
	}
}
