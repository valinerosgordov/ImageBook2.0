package ru.imagebook.server.ctl.vendor;

import java.util.List;

import ru.imagebook.client.admin.ctl.vendor.AddVendorMessage;
import ru.imagebook.client.admin.ctl.vendor.LoadVendorsMessage;
import ru.imagebook.client.admin.ctl.vendor.LoadVendorsResultMessage;
import ru.imagebook.client.admin.ctl.vendor.UpdateVendorMessage;
import ru.imagebook.client.admin.ctl.vendor.VendorMessages;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class VendorController extends Controller {
	private final VendorService service;

	public VendorController(Dispatcher dispatcher, VendorService service) {
		super(dispatcher);
		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(VendorMessages.LOAD_VENDORS,
				new MessageHandler<LoadVendorsMessage>() {
					@Override
					public void handle(LoadVendorsMessage message) {
						List<Vendor> agents = service.loadVendors();
						send(new LoadVendorsResultMessage(agents));
					}
				});
		
		addHandler(VendorMessages.ADD_VENDOR,
				new MessageHandler<AddVendorMessage>() {
					@Override
					public void handle(AddVendorMessage message) {
						service.saveVendor(message.getVendor());

						Message reply = new BaseMessage(VendorMessages.SAVE_AGENT_RESULT);
						reply.addAspects(RemotingAspect.CLIENT);
						send(reply);
					}
				});

		addHandler(VendorMessages.UPDATE_AGENT,
				new MessageHandler<UpdateVendorMessage>() {
					@Override
					public void handle(UpdateVendorMessage message) {
						service.updateVendor(message.getAgent());

						Message reply = new BaseMessage(VendorMessages.UPDATE_AGENT_RESULT);
						reply.addAspects(RemotingAspect.CLIENT);
						send(reply);
					}
				});
	}
}
