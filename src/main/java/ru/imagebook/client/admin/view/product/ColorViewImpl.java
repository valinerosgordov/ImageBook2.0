package ru.imagebook.client.admin.view.product;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.ctl.product.ColorView;
import ru.imagebook.client.admin.ctl.product.DeleteColorsMessage;
import ru.imagebook.client.admin.ctl.product.DeleteColorsRequestMessage;
import ru.imagebook.client.admin.ctl.product.UpdateColorsMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.util.i18n.I18n;
import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.i18n.locale.Locales;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ColorViewImpl extends View implements ColorView {
	public static final String NAME_RU = "nameRu";
	public static final String NAME_EN = "nameEn";

	private final Widgets widgets;
	private final ColorConstants constants;
	private final CommonConstants appConstants;
	private ListStore<BeanModel<Color>> store;
	private EditorGrid<BeanModel<Color>> grid;

	@Inject
	public ColorViewImpl(Dispatcher dispatcher, Widgets widgets, ColorConstants constants,
			CommonConstants appConstants) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showColors(List<Color> colors) {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
		desktop.removeAll();

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeading(constants.colorHeading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Color color = new Color();
				BeanModel<Color> model = new BeanModel<Color>(color);
				grid.stopEditing();
				store.insert(model, 0);
				grid.startEditing(0, 0);
			}
		}));
		toolBar.add(new Button(appConstants.delete(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<BeanModel<Color>> selectedItems = grid.getSelectionModel().getSelectedItems();
				List<Color> colors = new ArrayList<Color>();
				for (BeanModel<Color> model : selectedItems) {
					colors.add(model.getBean());
				}
				send(new DeleteColorsRequestMessage(colors));
			}
		}));
		panel.setTopComponent(toolBar);

		store = new ListStore<BeanModel<Color>>();
		for (Color color : colors) {
			color.set(NAME_RU, color.getName().get(Locales.RU));
			color.set(NAME_EN, color.getName().get(Locales.EN));
			store.add(new BeanModel<Color>(color));
		}

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		ColumnConfig numberConfig = new ColumnConfig(Color.NUMBER, constants.numberColumn(), 50);
		numberConfig.setAlignment(HorizontalAlignment.RIGHT);
		NumberField numberField = new NumberField();
		numberField.setPropertyEditorType(Integer.class);
		numberConfig.setEditor(new CellEditor(numberField));
		columns.add(numberConfig);

		ColumnConfig nameRuConfig = new ColumnConfig(NAME_RU, constants.nameRuColumn(), 200);
		TextField<String> nameRuField = new TextField<String>();
		nameRuConfig.setEditor(new CellEditor(nameRuField));
		columns.add(nameRuConfig);

		ColumnConfig nameEnConfig = new ColumnConfig(NAME_EN, constants.nameEnColumn(), 200);
		TextField<String> nameEnField = new TextField<String>();
		nameEnConfig.setEditor(new CellEditor(nameEnField));
		columns.add(nameEnConfig);

		grid = new EditorGrid<BeanModel<Color>>(store, new ColumnModel(columns));
		grid.setSelectionModel(new GridSelectionModel<BeanModel<Color>>());
		grid.getView().setSortingEnabled(false);
		panel.add(grid);

		panel.addButton(new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<Record> modifiedRecords = store.getModifiedRecords();
				List<Color> colors = new ArrayList<Color>();
				for (Record record : modifiedRecords) {
					BeanModel<Color> model = (BeanModel<Color>) record.getModel();
					Color color = model.getBean();
					String nameRu = color.get(NAME_RU);
					String nameEn = color.get(NAME_EN);
					color.setName(I18n.ms(nameRu, nameEn));
					colors.add(color);
				}
				send(new UpdateColorsMessage(colors));
			}
		}));
		panel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				store.rejectChanges();
			}
		}));
		panel.setButtonAlign(HorizontalAlignment.LEFT);

		desktop.add(panel);
		desktop.layout();
	}

	@Override
	public void confirmDeleteColors(final List<Color> colors) {
		new ConfirmMessageBox(appConstants.warning(), constants.confirmDeleteColors(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						send(new DeleteColorsMessage(colors));
					}
				});
	}

	@Override
	public void alertColorUsed() {
		MessageBox.alert(appConstants.error(), constants.colorUsedError(), null);
	}
}
