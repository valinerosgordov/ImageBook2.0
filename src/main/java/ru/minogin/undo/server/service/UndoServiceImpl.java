package ru.minogin.undo.server.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.minogin.undo.client.service.UndoRemoteService;
import ru.minogin.undo.server.action.UndoableAction;
import ru.minogin.undo.shared.UndoInfo;
import ru.minogin.util.server.spring.SpringUtil;

import javax.servlet.http.HttpSession;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class UndoServiceImpl implements UndoService, UndoRemoteService {
	public static final String UNDO_STACK = "undoStack";

	private static final Logger LOGGER = Logger.getLogger(UndoServiceImpl.class);

	@Override
	public void execute(UndoableAction action) {
		execute(action, null);
	}

	@Override
	public void execute(UndoableAction action, UndoInfo info) {
		LOGGER.debug("Executing: " + action);

		action.execute();

		getUndoStack().push(new UndoRecord(action, info));

		LOGGER.debug("Executed and pushed: " + action);
	}

	@Transactional
	@Override
	public UndoInfo undo() {
		LOGGER.debug("Undoing last action.");

		BlockingDeque<UndoRecord> undoStack = getUndoStack();
		if (undoStack.isEmpty()) {
			LOGGER.debug("Undo stack is empty.");
			return null;
		}
		UndoRecord record = undoStack.pop();
		UndoableAction action = record.getAction();
		LOGGER.debug("Last action is: " + action);
		UndoInfo info = record.getInfo();
		LOGGER.debug("Last undo info is: " + info);

		action.undo();
		LOGGER.debug("Undone: " + action);
		
		return info;
	}

	@SuppressWarnings("unchecked")
	private BlockingDeque<UndoRecord> getUndoStack() {
		HttpSession session = SpringUtil.getSession();
		LOGGER.debug("Got session with id: " + session.getId());

		BlockingDeque<UndoRecord> undoStack = (BlockingDeque<UndoRecord>) session
				.getAttribute(UNDO_STACK);
		if (undoStack == null) {
			LOGGER.debug("Undo stack not found. Creating.");
			undoStack = new LinkedBlockingDeque<UndoRecord>();
			session.setAttribute(UNDO_STACK, undoStack);
		}
		return undoStack;
	}

	@Override
	public void resetUndoStack() {
		LOGGER.debug("Resetting stack.");

		BlockingDeque<UndoRecord> undoStack = getUndoStack();
		undoStack.clear();
	}
}
