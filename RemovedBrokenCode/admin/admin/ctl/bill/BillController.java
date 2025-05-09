package ru.imagebook.client.admin.ctl.bill;

import ru.imagebook.client.common.service.I18nService;
import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class BillController extends Controller {
	private final BillView view;
	private final I18nService i18nService;

	@Inject
	public BillController(Dispatcher dispatcher, final BillView view, final I18nService i18nService) {
		super(dispatcher);

		this.view = view;
		this.i18nService = i18nService;
	}

	@Override
	public void registerHandlers() {
		addHandler(BillMessages.SHOW_SECTION, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showSection();
			}
		});

		addHandler(BillMessages.LOAD_BILLS_RESULT, new MessageHandler<LoadBillsResultMessage>() {
			@Override
			public void handle(LoadBillsResultMessage message) {
				view.showBills(message.getBills(), message.getOffset(), (int) message.getTotal(),
						i18nService.getLocale());
			}
		});

		addHandler(BillMessages.MARK_PAID_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
			}
		});

		addHandler(BillMessages.BILL_SELECTED, new MessageHandler<BillSelectedMessage>() {
			@Override
			public void handle(BillSelectedMessage message) {
				Bill bill = message.getBill();
				view.showBill(bill);
			}
		});

		addHandler(BillMessages.UPDATE_BILL_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideEditForm();
				view.reload();
			}
		});

		addHandler(BillMessages.DELETE_BILLS_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.reload();
			}
		});
	}
}
