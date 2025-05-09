package ru.imagebook.server.ctl.action;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;

import ru.imagebook.client.admin.ctl.action.ActionMessages;
import ru.imagebook.client.admin.ctl.action.AddActionMessage;
import ru.imagebook.client.admin.ctl.action.AddCodeResultMessage;
import ru.imagebook.client.admin.ctl.action.AddCodesMessage;
import ru.imagebook.client.admin.ctl.action.DeleteActionsMessage;
import ru.imagebook.client.admin.ctl.action.DeleteBonusCodesMessage;
import ru.imagebook.client.admin.ctl.action.DeleteBonusCodesResultMessage;
import ru.imagebook.client.admin.ctl.action.GenerateCodesMessage;
import ru.imagebook.client.admin.ctl.action.LoadActionsMessage;
import ru.imagebook.client.admin.ctl.action.LoadActionsResultMessage;
import ru.imagebook.client.admin.ctl.action.LoadAlbumsMessage;
import ru.imagebook.client.admin.ctl.action.LoadAlbumsResultForBonusActionMessage;
import ru.imagebook.client.admin.ctl.action.LoadBonusCodesWithOrderInfoMessage;
import ru.imagebook.client.admin.ctl.action.LoadBonusCodesWithOrderInfoResultMessage;
import ru.imagebook.client.admin.ctl.action.LoadStatusRequestsMessage;
import ru.imagebook.client.admin.ctl.action.LoadStatusRequestsResultMessage;
import ru.imagebook.client.admin.ctl.action.RejectRequestMessage;
import ru.imagebook.client.admin.ctl.action.SendStatusCodeMessage;
import ru.imagebook.client.admin.ctl.action.SendStatusCodeResultMessage;
import ru.imagebook.client.admin.ctl.action.ShowActionsSectionResultMessage;
import ru.imagebook.client.admin.ctl.action.UpdateActionMessage;
import ru.imagebook.server.service.ActionService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.server.flow.remoting.MessageError;

public class ActionController extends Controller {
	private final ActionService service;
	private final VendorService vendorService;
	
	public ActionController(Dispatcher dispatcher, final ActionService service, final VendorService vendorService) {
		super(dispatcher);

		this.service = service;
		this.vendorService = vendorService;
	}

	@Override
	public void registerHandlers() {
		addHandler(ActionMessages.LOAD_ACTIONS, new MessageHandler<LoadActionsMessage>() {
			@Override
			public void handle(LoadActionsMessage message) {
				final Integer vendorId = message.getVendorId();
				final String query = message.getQuery();
				List<BonusAction> actions;
				if (vendorId == null) {
					actions = service.loadActions(query);
				} else {
					Vendor vendor = vendorService.getVendorById(vendorId);
					actions = service.loadActions(vendor, query);
				}
				send(new LoadActionsResultMessage(actions));
			}
		});

		addHandler(ActionMessages.LOAD_ALBUMS, new MessageHandler<LoadAlbumsMessage>() {
			@Override
			public void handle(LoadAlbumsMessage message) {
				List<Album> albums = service.loadAlbums();
				BonusAction bonusAction = message.getBonusAction();
				if (bonusAction != null) {
				bonusAction.setAlbums(service.getBonusActionWithAlbums(bonusAction).getAlbums());
				}
			send(new LoadAlbumsResultForBonusActionMessage(albums, bonusAction));
			}
		});

		addHandler(ActionMessages.ADD_ACTION, new MessageHandler<AddActionMessage>() {
			@Override
			public void handle(AddActionMessage message) {
				service.addAction(message.getAction());

				Message reply = new BaseMessage(ActionMessages.ADD_ACTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ActionMessages.UPDATE_ACTION, new MessageHandler<UpdateActionMessage>() {
			@Override
			public void handle(UpdateActionMessage message) {
				BonusAction action = message.getAction();
				service.updateAction(action);

				Message reply = new BaseMessage(ActionMessages.UPDATE_ACTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ActionMessages.DELETE_ACTIONS, new MessageHandler<DeleteActionsMessage>() {
			@Override
			public void handle(DeleteActionsMessage message) {
				List<Integer> ids = message.getIds();
				service.deleteActions(ids);

				Message reply = new BaseMessage(ActionMessages.DELETE_ACTION_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ActionMessages.LOAD_BONUS_CODES_WITH_ORDER_INFO, new MessageHandler<LoadBonusCodesWithOrderInfoMessage>() {
			@Override
			public void handle(LoadBonusCodesWithOrderInfoMessage message) {
				int actionId = message.getActionId();
				Map<BonusCode, List<Order<?>>> map;
				map = service.loadOrdersForBonusAction(actionId);
				send(new LoadBonusCodesWithOrderInfoResultMessage(map));
			}
		});

		addHandler(ActionMessages.DELETE_BONUS_CODES_ACTIONS, new MessageHandler<DeleteBonusCodesMessage>() {
			@Override
			public void handle(DeleteBonusCodesMessage message) {
				final List<Integer> ids = message.getIds();
				final int bonusActionId = message.getBonusActionId();
				try {
					service.deleteBonusCodesFromAction(ids, bonusActionId);
				} catch (DataIntegrityViolationException e) {
					throw new MessageError(ActionMessages.BONUS_CODE_ORDER_EXISTS);
				}

				send(new DeleteBonusCodesResultMessage(bonusActionId));
			}
		});

		addHandler(ActionMessages.SHOW_ACTIONS_SECTION_WITH_VENDORS, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				List<Vendor> vendors = vendorService.loadVendors();
				
				Message reply = new ShowActionsSectionResultMessage(vendors);
				send(reply);
			}
		});

		addHandler(ActionMessages.GENERATE_CODES, new MessageHandler<GenerateCodesMessage>() {
			@Override
			public void handle(GenerateCodesMessage message) {
				int actionId = message.getActionId();
				int quantity = message.getQuantity();
				service.generateCodes(actionId, quantity);

				Message reply = new BaseMessage(ActionMessages.GENERATE_CODES_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ru.imagebook.server.ctl.action.ActionMessages.SHOW_CODES,
				new MessageHandler<ShowCodesMessage>() {
					@Override
					public void handle(ShowCodesMessage message) {
						service.showCodes(message.getActionId(), message.getWriter());
					}
				});

		addHandler(ActionMessages.LOAD_STATUS_REQUESTS,
				new MessageHandler<LoadStatusRequestsMessage>() {
					@Override
					public void handle(LoadStatusRequestsMessage message) {
						List<StatusRequest> codes = service.loadStatusRequests();
						send(new LoadStatusRequestsResultMessage(codes));
					}
				});

		addHandler(ActionMessages.SEND_STATUS_CODE, new MessageHandler<SendStatusCodeMessage>() {
			@Override
			public void handle(SendStatusCodeMessage message) {
				service.sendStatusCode(message.getRequestId(), message.getEmail());
				send(new SendStatusCodeResultMessage());
			}
		});

		addHandler(ActionMessages.REJECT_REQUEST, new MessageHandler<RejectRequestMessage>() {
			@Override
			public void handle(RejectRequestMessage message) {
				service.rejectRequest(message.getRequestId(), message.getReason());

				Message reply = new BaseMessage(ActionMessages.REJECT_REQUEST_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				send(reply);
			}
		});

		addHandler(ru.imagebook.server.ctl.action.ActionMessages.ACTIVATE_REQUEST,
				new MessageHandler<ActivateRequestMessage>() {
					@Override
					public void handle(ActivateRequestMessage message) {
						service.activateRequest(message.getRequestId(), message.getCode(), message.getWriter());
					}
				});

		addHandler(ActionMessages.ADD_CODES, new MessageHandler<AddCodesMessage>() {
			@Override
			public void handle(AddCodesMessage message) {
				boolean haveAdded = service.addCodes(message.getActionId(), message.getCodes());

				send(new AddCodeResultMessage(haveAdded));
			}
		});
	}
}
