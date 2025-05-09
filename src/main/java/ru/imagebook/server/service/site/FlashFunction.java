package ru.imagebook.server.service.site;

import ru.imagebook.server.service.flash.FlashService;
import ru.minogin.core.client.bean.Bean;
import ru.minogin.core.client.lang.Function;

public class FlashFunction implements Function {
	private final FlashService flashService;

	public FlashFunction(FlashService flashService) {
		this.flashService = flashService;
	}

	@Override
	public Object eval(Object[] args, Bean context) {
		String id = (String) args[0];
		String name = (String) args[1];
		String author = "";
		if (args.length > 2)
			author = (String) args[2];
		boolean small = false;
		if (args.length > 3)
			small = (Boolean) args[3];
		return flashService.showWebFlash(id, name, author, small);
	}
}
