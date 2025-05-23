package ru.imagebook.client.admin;

import ru.imagebook.client.admin.ctl.AdminView;
import ru.imagebook.client.admin.ctl.DesktopView;
import ru.imagebook.client.admin.ctl.action.ActionView;
import ru.imagebook.client.admin.ctl.bill.BillView;
import ru.imagebook.client.admin.ctl.delivery.DeliveryView;
import ru.imagebook.client.admin.ctl.finishing.FinishingView;
import ru.imagebook.client.admin.ctl.mailing.MailingView;
import ru.imagebook.client.admin.ctl.order.OrderView;
import ru.imagebook.client.admin.ctl.pricing.PricingView;
import ru.imagebook.client.admin.ctl.product.AlbumView;
import ru.imagebook.client.admin.ctl.product.ColorView;
import ru.imagebook.client.admin.ctl.request.RequestView;
import ru.imagebook.client.admin.ctl.site.SiteView;
import ru.imagebook.client.admin.ctl.user.UserView;
import ru.imagebook.client.admin.ctl.vendor.VendorView;
import ru.imagebook.client.admin.view.AdminViewImpl;
import ru.imagebook.client.admin.view.DesktopViewImpl;
import ru.imagebook.client.admin.view.action.ActionViewImpl;
import ru.imagebook.client.admin.view.bill.BillViewImpl;
import ru.imagebook.client.admin.view.country.CountryView;
import ru.imagebook.client.admin.view.country.CountryViewImpl;
import ru.imagebook.client.admin.view.delivery.DeliveryViewImpl;
import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyViewImpl;
import ru.imagebook.client.admin.view.finishing.FinishingViewImpl;
import ru.imagebook.client.admin.view.mailing.MailingViewImpl;
import ru.imagebook.client.admin.view.order.OrderViewImpl;
import ru.imagebook.client.admin.view.pricing.PricingViewImpl;
import ru.imagebook.client.admin.view.product.AlbumViewImpl;
import ru.imagebook.client.admin.view.product.ColorViewImpl;
import ru.imagebook.client.admin.view.product.ProductImageView;
import ru.imagebook.client.admin.view.product.ProductImageViewImpl;
import ru.imagebook.client.admin.view.questions.QuestionCategoryView;
import ru.imagebook.client.admin.view.questions.QuestionCategoryViewImpl;
import ru.imagebook.client.admin.view.request.RequestViewImpl;
import ru.imagebook.client.admin.view.site.SiteViewImpl;
import ru.imagebook.client.admin.view.site.tag.TagView;
import ru.imagebook.client.admin.view.site.tag.TagViewImpl;
import ru.imagebook.client.admin.view.user.UserViewImpl;
import ru.imagebook.client.admin.view.vendor.VendorViewImpl;
import ru.imagebook.client.admin.view.zone.ZoneView;
import ru.imagebook.client.admin.view.zone.ZoneViewImpl;
import ru.imagebook.client.common.service.admin.AppServiceImpl;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.app.ApplicationModule;
import ru.minogin.core.client.flow.BaseDispatcher;
import ru.minogin.core.client.flow.BaseWidgets;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.flow.download.DownloadView;
import ru.minogin.core.client.flow.download.DownloadViewImpl;
import ru.minogin.core.client.gwt.GWTCoreFactory;
import ru.saasengine.client.ctl.auth.AuthView;
import ru.saasengine.client.ctl.browser.BrowserView;
import ru.saasengine.client.ctl.failure.FailureView;
import ru.saasengine.client.service.app.AppService;
import ru.saasengine.client.view.auth.AuthViewImpl;
import ru.saasengine.client.view.browser.BrowserViewImpl;
import ru.saasengine.client.view.failure.FailureViewImpl;

public class AdminModule extends ApplicationModule {
	@Override
	protected void configure() {
		super.configure();

		bind(Dispatcher.class).to(BaseDispatcher.class);
		bind(AppService.class).to(AppServiceImpl.class);
		bind(AdminView.class).to(AdminViewImpl.class);
		bind(VendorView.class).to(VendorViewImpl.class);
		bind(AuthView.class).to(AuthViewImpl.class);
		bind(BrowserView.class).to(BrowserViewImpl.class);
		bind(DesktopView.class).to(DesktopViewImpl.class);
		bind(DownloadView.class).to(DownloadViewImpl.class);
		bind(FailureView.class).to(FailureViewImpl.class);
		bind(UserView.class).to(UserViewImpl.class);
		bind(Widgets.class).to(BaseWidgets.class);
		bind(PricingView.class).to(PricingViewImpl.class);
		bind(AlbumView.class).to(AlbumViewImpl.class);
		bind(ProductImageView.class).to(ProductImageViewImpl.class);
		bind(ColorView.class).to(ColorViewImpl.class);
		bind(OrderView.class).to(OrderViewImpl.class);
		bind(RequestView.class).to(RequestViewImpl.class);
		bind(ActionView.class).to(ActionViewImpl.class);
		bind(BillView.class).to(BillViewImpl.class);
		bind(SiteView.class).to(SiteViewImpl.class);
		bind(DeliveryView.class).to(DeliveryViewImpl.class);
		bind(FinishingView.class).to(FinishingViewImpl.class);
		bind(CoreFactory.class).to(GWTCoreFactory.class);
		bind(MailingView.class).to(MailingViewImpl.class);
		bind(TagView.class).to(TagViewImpl.class);
		bind(CountryView.class).to(CountryViewImpl.class);
		bind(ZoneView.class).to(ZoneViewImpl.class);
		bind(DeliveryAssemblyView.class).to(DeliveryAssemblyViewImpl.class);
		bind(QuestionCategoryView.class).to(QuestionCategoryViewImpl.class);
	}
}
