package ru.imagebook.client.editor.ctl.file;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadImagesResultMessage extends BaseMessage {
	private static final long serialVersionUID = 2026496883384109142L;

	public static final String NAMES = "names";

	LoadImagesResultMessage() {}

	public LoadImagesResultMessage(List<String> names) {
		super(FileMessages.LOAD_IMAGES_RESULT);

		addAspects(RemotingAspect.CLIENT);

		set(NAMES, names);
	}

	public List<String> getNames() {
		return get(NAMES);
	}
}
