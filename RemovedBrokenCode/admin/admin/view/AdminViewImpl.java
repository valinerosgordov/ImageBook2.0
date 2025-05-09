package ru.imagebook.client.admin.view;

import ru.imagebook.client.admin.ctl.AdminView;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.inject.Inject;

public class AdminViewImpl extends View implements AdminView {
	private final CommonConstants appConstants;
	private final AdminConstants constants;

	@Inject
	public AdminViewImpl(Dispatcher dispatcher, CommonConstants appConstants, AdminConstants constants) {
		super(dispatcher);

		this.appConstants = appConstants;
		this.constants = constants;
	}

	@Override
	public void permissionDenied() {
		MessageBox.alert(appConstants.error(), constants.permissionDenied(), null);
	}

	@Override
	public void infoBackupComplete() {
		MessageBox.info(appConstants.info(), constants.backupComplete(), null);
	}
	
	@Override
	public void infoUpdateComplete() {
		MessageBox.info(appConstants.info(), constants.updateComplete(), null);
	}
	
	@Override
	public void infoCleanComplete() {
		MessageBox.info(appConstants.info(), constants.cleanComplete(), null);
	}
}
