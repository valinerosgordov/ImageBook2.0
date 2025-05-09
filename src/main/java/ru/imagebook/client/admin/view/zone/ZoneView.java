package ru.imagebook.client.admin.view.zone;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.imagebook.shared.model.Zone;

import com.google.gwt.user.client.ui.IsWidget;

public interface ZoneView extends IsWidget {
	void updateZones();

	void showZones(List<Zone> zones, int offset, int totalCount);

	void showAddForm(List<Country> countries, List<Region> regions);

	String getZip();

	void setZip(String zip);

	Zone getZoneProperties();

	void hideAddForm();

	Zone getSelectedZone();

	void showEditForm(List<Country> countries, List<Region> regions, Zone zone);

	void hideEditForm();

	void confirmDelete();

	List<Zone> getSelectedZones();

	void alertSelectDeleteZones();

	void alertSelectEditZones();

	void showImportForm();

	void closeImportForm();

	void showFileParsingProgressBox();

	void updateFileLoadingProgressBox(double percent);

	void updateFileParsingProgressBox(double percent);

	void closeFileLoadingProgressBox();

	void closeFileParsingProgressBox();

	void showParseErrorMessage(int rowIndex, int fieldIndex);

	void showFileTypeErrorMessage();

	void setPresenter(ZonePresenter presenter);

	void setRegions(List<Region> result);
}
