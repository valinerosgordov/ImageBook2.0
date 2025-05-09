package ru.imagebook.client.admin.ctl.vendor;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class VendorController extends Controller {
	private final VendorView view;

	@Inject
	public VendorController(Dispatcher dispatcher, VendorView view) {
		super(dispatcher);

		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(VendorMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showSection();

				send(new LoadVendorsMessage());
			}
		});

		addHandler(VendorMessages.LOAD_AGENTS_RESULT, new MessageHandler<LoadVendorsResultMessage>() {
			@Override
			public void handle(LoadVendorsResultMessage message) {
				view.showAgents(message.getAgents());
			}
		});

		addHandler(VendorMessages.SHOW_ADD_FORM, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showAddForm();
			}
		});

		addHandler(VendorMessages.SAVE_AGENT_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideAddForm();

				send(new LoadVendorsMessage());
			}
		});

		addHandler(VendorMessages.EDIT_AGENT, new MessageHandler<EditVendorMessage>() {
			@Override
			public void handle(EditVendorMessage message) {
				Vendor vendor = message.getAgent();
				view.showEditForm(vendor);
			}
		});

		addHandler(VendorMessages.UPDATE_AGENT_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideEditForm();

				send(new LoadVendorsMessage());
			}
		});
	}
}
