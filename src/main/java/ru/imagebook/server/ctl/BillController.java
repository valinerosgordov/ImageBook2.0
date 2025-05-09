package ru.imagebook.server.ctl;

import java.util.List;

import ru.imagebook.client.admin.ctl.bill.BillMessages;
import ru.imagebook.client.admin.ctl.bill.DeleteBillsMessage;
import ru.imagebook.client.admin.ctl.bill.LoadBillsMessage;
import ru.imagebook.client.admin.ctl.bill.LoadBillsResultMessage;
import ru.imagebook.client.admin.ctl.bill.MarkPaidMessage;
import ru.imagebook.client.admin.ctl.bill.UpdateBillMessage;
import ru.imagebook.server.service.BillService;
import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.flow.remoting.ReplyMessage;

public class BillController extends Controller {
	private final BillService service;

	public BillController(Dispatcher dispatcher, final BillService service) {
		super(dispatcher);

		this.service = service;
	}

	@Override
	public void registerHandlers() {
		addHandler(BillMessages.LOAD_BILLS, new MessageHandler<LoadBillsMessage>() {
			@Override
			public void handle(LoadBillsMessage message) {
				int offset = message.getOffset();
				int limit = message.getLimit();
				List<Bill> bills = service.loadBills(offset, limit, message.getBillFilter());
				long total = service.countBills(message.getBillFilter());

				send(new LoadBillsResultMessage(bills, offset, total, message.getBillFilter() ));
			}
		});

		addHandler(BillMessages.MARK_PAID, new MessageHandler<MarkPaidMessage>() {
			@Override
			public void handle(MarkPaidMessage message) {
				service.markPaid(message.getIds());

				Message reply = new BaseMessage(BillMessages.MARK_PAID_RESULT);
				reply.addAspects(RemotingAspect.CLIENT);
				sendReply(reply, message);
			}
		});

		addHandler(BillMessages.UPDATE_BILL, new MessageHandler<UpdateBillMessage>() {
			@Override
			public void handle(UpdateBillMessage message) {
				service.updateBill(message.getBill());
				send(new ReplyMessage(BillMessages.UPDATE_BILL_RESULT));
			}
		});

		addHandler(BillMessages.DELETE_BILLS, new MessageHandler<DeleteBillsMessage>() {
			@Override
			public void handle(DeleteBillsMessage message) {
				service.deleteBills(message.getIds());

				send(new ReplyMessage(BillMessages.DELETE_BILLS_RESULT));
			}
		});
	}
}
