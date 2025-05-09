package ru.imagebook.client.app;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import ru.imagebook.client.app.ctl.AppActivityMapper;
import ru.imagebook.client.app.ctl.AppController;
import ru.imagebook.client.app.ctl.AppPlaceHistoryMapper;
import ru.imagebook.client.app.ctl.order.OrderActionPresenter;
import ru.imagebook.client.app.ctl.order.OrderActionPresenterFactory;
import ru.imagebook.client.app.ctl.order.OrderActionPresenterImpl;
import ru.imagebook.client.app.ctl.register.RegisterActivity;
import ru.imagebook.client.app.ctl.register.RegisterActivityFactory;
import ru.imagebook.client.app.ctl.register.RegisterPresenter;
import ru.imagebook.client.app.view.AppView;
import ru.imagebook.client.app.view.AppViewImpl;
import ru.imagebook.client.app.view.order.OrderActionView;
import ru.imagebook.client.app.view.order.OrderActionViewImpl;
import ru.imagebook.client.app.view.order.OrderView;
import ru.imagebook.client.app.view.order.OrderViewImpl;
import ru.imagebook.client.app.view.payment.BillPanelFactory;
import ru.imagebook.client.app.view.payment.BillsView;
import ru.imagebook.client.app.view.payment.BillsViewImpl;
import ru.imagebook.client.app.view.payment.DeliveryView;
import ru.imagebook.client.app.view.payment.DeliveryViewImpl;
import ru.imagebook.client.app.view.payment.PaymentMethodsView;
import ru.imagebook.client.app.view.payment.PaymentMethodsViewImpl;
import ru.imagebook.client.app.view.personal.PersonalView;
import ru.imagebook.client.app.view.personal.PersonalViewImpl;
import ru.imagebook.client.app.view.personal.PersonalWidgetFactory;
import ru.imagebook.client.app.view.process.ProcessBillPanelFactory;
import ru.imagebook.client.app.view.process.ProcessView;
import ru.imagebook.client.app.view.process.ProcessViewImpl;
import ru.imagebook.client.app.view.sent.SentBillPanelFactory;
import ru.imagebook.client.app.view.sent.SentView;
import ru.imagebook.client.app.view.sent.SentViewImpl;
import ru.imagebook.client.app.view.support.SupportView;
import ru.imagebook.client.app.view.support.SupportViewImpl;
import ru.imagebook.client.common.ctl.register.RegisterView;
import ru.imagebook.client.common.ctl.user.UserView;
import ru.imagebook.client.common.service.app.AppServiceImpl;
import ru.imagebook.client.common.util.DeliveryAddressDisplayHelper;
import ru.imagebook.client.common.util.DeliveryMethodDisplayHelper;
import ru.imagebook.client.common.view.register.RegisterViewImpl;
import ru.imagebook.client.common.view.user.UserViewImpl;
import ru.imagebook.shared.util.delivery.DeliveryAddressHelper;
import ru.imagebook.shared.util.delivery.DeliveryMethodHelper;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.flow.BaseDispatcher;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.gwt.GWTCoreFactory;
import ru.saasengine.client.service.app.AppService;


public class AppModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
        bind(ActivityMapper.class).to(AppActivityMapper.class).in(Singleton.class);
        bind(PlaceHistoryMapper.class).to(AppPlaceHistoryMapper.class).in(Singleton.class);

        bind(AppController.class).in(Singleton.class);
        bind(AppService.class).to(AppServiceImpl.class);

        // views
        bind(AppView.class).to(AppViewImpl.class);
        bind(OrderView.class).to(OrderViewImpl.class);
        bind(BillsView.class).to(BillsViewImpl.class);
        bind(DeliveryView.class).to(DeliveryViewImpl.class);
        bind(PaymentMethodsView.class).to(PaymentMethodsViewImpl.class);
        bind(ProcessView.class).to(ProcessViewImpl.class);
        bind(SentView.class).to(SentViewImpl.class);
        bind(PersonalView.class).to(PersonalViewImpl.class);
        bind(SupportView.class).to(SupportViewImpl.class);
        bind(OrderActionView.class).to(OrderActionViewImpl.class);
        bind(UserView.class).to(UserViewImpl.class);
        bind(RegisterView.class).to(RegisterViewImpl.class);
        bind(ru.imagebook.client.app.view.register.RegisterView.class)
            .to(ru.imagebook.client.app.view.register.RegisterViewImpl.class);

        // An old mechanism of messages used in application,
        // it's better to replace it with activity/place approach
        bind(Dispatcher.class).to(BaseDispatcher.class);

        bind(CoreFactory.class).to(GWTCoreFactory.class);
        bind(DeliveryMethodHelper.class).to(DeliveryMethodDisplayHelper.class);
        bind(DeliveryAddressHelper.class).to(DeliveryAddressDisplayHelper.class);

        install(new GinFactoryModuleBuilder().build(AppActivityMapper.ActivityFactory.class));
        install(new GinFactoryModuleBuilder().implement(OrderActionPresenter.class, OrderActionPresenterImpl.class)
            .build(OrderActionPresenterFactory.class));
        install(new GinFactoryModuleBuilder().implement(RegisterPresenter.class, RegisterActivity.class)
            .build(RegisterActivityFactory.class));
        install(new GinFactoryModuleBuilder().build(BillPanelFactory.class));
        install(new GinFactoryModuleBuilder().build(ProcessBillPanelFactory.class));
        install(new GinFactoryModuleBuilder().build(SentBillPanelFactory.class));
        install(new GinFactoryModuleBuilder().build(PersonalWidgetFactory.class));
    }

    @Singleton
    @Provides
    public PlaceController providePlaceController(EventBus eventBus) {
        return new PlaceController(eventBus);
    }

    @Singleton
    @Provides
    public ActivityManager provideActivityManager(ActivityMapper activityMapper, EventBus eventBus) {
        return new ActivityManager(activityMapper, eventBus);
    }

    @Singleton
    @Provides
    public PlaceHistoryHandler providePlaceHistoryHandler(PlaceHistoryMapper mapper, EventBus eventBus,
                                                          PlaceController placeController) {
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(mapper);
        historyHandler.register(placeController, eventBus, Place.NOWHERE);
        return historyHandler;
    }
}
