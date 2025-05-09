package ru.imagebook.client.editor;

import ru.imagebook.client.common.ctl.auth.AuthView;
import ru.imagebook.client.common.ctl.register.RegisterView;
import ru.imagebook.client.common.ctl.user.UserView;
import ru.imagebook.client.common.service.editor.AppServiceImpl;
import ru.imagebook.client.common.view.auth.AuthViewImpl;
import ru.imagebook.client.common.view.register.RegisterViewImpl;
import ru.imagebook.client.common.view.user.UserViewImpl;
import ru.imagebook.client.editor.ctl.EditorView;
import ru.imagebook.client.editor.ctl.file.FileView;
import ru.imagebook.client.editor.ctl.order.OrderView;
import ru.imagebook.client.editor.ctl.pages.PagesView;
import ru.imagebook.client.editor.ctl.spread.SpreadView;
import ru.imagebook.client.editor.view.EditorViewImpl;
import ru.imagebook.client.editor.view.file.FileViewImpl;
import ru.imagebook.client.editor.view.order.OrderViewImpl;
import ru.imagebook.client.editor.view.pages.PagesViewImpl;
import ru.imagebook.client.editor.view.spread.SpreadViewImpl;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.app.ApplicationModule;
import ru.minogin.core.client.flow.BaseDispatcher;
import ru.minogin.core.client.flow.BaseWidgets;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gwt.GWTCoreFactory;
import ru.saasengine.client.ctl.browser.BrowserView;
import ru.saasengine.client.ctl.failure.FailureView;
import ru.saasengine.client.service.app.AppService;
import ru.saasengine.client.view.browser.BrowserViewImpl;
import ru.saasengine.client.view.failure.FailureViewImpl;

public class EditorModule extends ApplicationModule {
	@Override
	protected void configure() {
		bind(Dispatcher.class).to(BaseDispatcher.class);
		bind(AppService.class).to(AppServiceImpl.class);
		bind(CoreFactory.class).to(GWTCoreFactory.class);
		bind(BrowserView.class).to(BrowserViewImpl.class);
		bind(AuthView.class).to(AuthViewImpl.class);
		bind(EditorView.class).to(EditorViewImpl.class);
		bind(Widgets.class).to(BaseWidgets.class);
		bind(FailureView.class).to(FailureViewImpl.class);
		bind(FileView.class).to(FileViewImpl.class);
		bind(SpreadView.class).to(SpreadViewImpl.class);
		bind(OrderView.class).to(OrderViewImpl.class);
		bind(PagesView.class).to(PagesViewImpl.class);
		bind(UserView.class).to(UserViewImpl.class);
		bind(RegisterView.class).to(RegisterViewImpl.class);
	}
}
