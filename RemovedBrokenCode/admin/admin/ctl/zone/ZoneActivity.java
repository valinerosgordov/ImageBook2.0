package ru.imagebook.client.admin.ctl.zone;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.RegionServiceAsync;
import ru.imagebook.client.admin.service.ZoneServiceAsync;
import ru.imagebook.client.admin.view.zone.ZonePresenter;
import ru.imagebook.client.admin.view.zone.ZoneView;
import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.imagebook.shared.model.Zone;
import ru.imagebook.shared.service.admin.delivery.FileTypeErrorMessage;
import ru.imagebook.shared.service.admin.delivery.ParseErrorMessage;
import ru.imagebook.shared.service.admin.delivery.ProgressParseFileMessage;
import ru.imagebook.shared.service.admin.delivery.ProgressUploadFileMessage;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.push.PushMessage;
import ru.minogin.core.client.push.mvp.PushEvent;
import ru.minogin.core.client.push.mvp.PushEventHandler;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ZoneActivity extends AbstractActivity implements ZonePresenter,
		PushEventHandler {
	@Inject
	private ZoneServiceAsync service;
	@Inject
	private RegionServiceAsync auxService;

	private Zone zone;
	private final ZoneView view;

	@Inject
	public ZoneActivity(ZoneView view) {
		view.setPresenter(this);
		this.view = view;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
		eventBus.addHandler(PushEvent.TYPE, this);
	}

	private void getZones(final int offset, int limit, final int total) {
		service.loadZones(offset, limit, new XAsyncCallback<List<Zone>>() {
			@Override
			public void onSuccess(List<Zone> zones) {
				view.showZones(zones, offset, total);
			}
		});
	}

	@Override
	public void addButtonClicked() {
		service.loadCountries(new XAsyncCallback<List<Country>>() {
			@Override
			public void onSuccess(final List<Country> countries) {
				if (!countries.isEmpty()) {
					auxService.loadRegionsForCountry(countries.get(0), new XAsyncCallback<List<Region>>() {
						@Override
						public void onSuccess(List<Region> result) {
							view.showAddForm(countries, result);
						}
					});
				}
			}
		});
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		Zone zone = new Zone();
		zone = view.getZoneProperties();
		service.addZone(zone, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				view.updateZones();
			}
		});
	}

	@Override
	public void editButtonClicked() {
		if (view.getSelectedZones().isEmpty()) {
			view.alertSelectEditZones();
		} else {
			zone = view.getSelectedZone();

			service.loadCountries(new XAsyncCallback<List<Country>>() {
				@Override
				public void onSuccess(final List<Country> countries) {
					auxService.loadRegionsForCountry(zone.getCountry(), new XAsyncCallback<List<Region>>() {
						@Override
						public void onSuccess(List<Region> result) {
							view.showEditForm(countries, result, zone);
						}
					});
				}
			});
		}
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedZones().isEmpty())
			view.alertSelectDeleteZones();
		else
			view.confirmDelete();
	}

	@Override
	public void saveButtonClickedOnEditForm() {
		int zoneId = zone.getId();
		zone = view.getZoneProperties();
		zone.setId(zoneId);
		service.updateZone(zone, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				view.updateZones();
			}
		});
	}

	@Override
	public void deleteConfirmed() {
		List<Zone> zones = view.getSelectedZones();
		List<Integer> ids = new ArrayList<Integer>();
		for (Zone zone : zones) {
			ids.add(zone.getId());
		}
		service.deleteZones(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.updateZones();
			}
		});
	}

	@Override
	public void setCurrentCountry(Country country) {
		auxService.loadRegionsForCountry(country, new XAsyncCallback<List<Region>>() {
			@Override
			public void onSuccess(List<Region> result) {
				view.setRegions(result);
			}
		});
	}
	
	@Override
	public void loadZones(final int offset, final int limit) {
		service.getZonesCount(new XAsyncCallback<Long>() {
			@Override
			public void onSuccess(Long total) {
				long totalCount = (long) total;
				getZones(offset, limit, (int) totalCount);
			}
		});
	}

	@Override
	public void importButtonClicked() {
		view.showImportForm();
	}

	@Override
	public void onPush(PushMessage pushMessage) {
		if (pushMessage instanceof ProgressUploadFileMessage) {
			ProgressUploadFileMessage message = (ProgressUploadFileMessage) pushMessage;
			double percent = message.getPercent();
			view.updateFileLoadingProgressBox(percent);
			if (percent >= 100) {
				view.closeFileLoadingProgressBox();
				view.showFileParsingProgressBox();
			}
		}
		else if (pushMessage instanceof ProgressParseFileMessage) {
			ProgressParseFileMessage message = (ProgressParseFileMessage) pushMessage;
			double percent = message.getPercent();
			view.updateFileParsingProgressBox(percent);
			if (percent >= 100) {
				view.closeFileParsingProgressBox();
				view.closeImportForm();
				view.updateZones();
			}
		}
		else if (pushMessage instanceof ParseErrorMessage) {
			ParseErrorMessage message = (ParseErrorMessage) pushMessage;
			view.showParseErrorMessage(message.getRowId(), message.getFieldId());
			view.closeImportForm();
		}
		else if (pushMessage instanceof FileTypeErrorMessage) {
			view.showFileTypeErrorMessage();
			view.closeImportForm();
		}
	}
}
