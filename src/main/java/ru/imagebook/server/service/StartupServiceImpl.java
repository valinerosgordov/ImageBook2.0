package ru.imagebook.server.service;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import ru.imagebook.server.ctl.*;
import ru.imagebook.server.ctl.action.ActionController;
import ru.imagebook.server.ctl.auth.AuthController;
import ru.imagebook.server.ctl.delivery.DeliveryController;
import ru.imagebook.server.ctl.editor.EditorController;
import ru.imagebook.server.ctl.finishing.FinishingController;
import ru.imagebook.server.ctl.flash.FlashController;
import ru.imagebook.server.ctl.flash.PreviewController;
import ru.imagebook.server.ctl.order.OrderController;
import ru.imagebook.server.ctl.qiwi.QiwiController;
import ru.imagebook.server.ctl.site.SiteController;
import ru.imagebook.server.ctl.vendor.VendorController;
import ru.imagebook.server.service.editor.EditorService;
import ru.imagebook.server.service.flash.FlashService;
import ru.imagebook.server.service.load.LoadService;
import ru.imagebook.server.service.pdf.PdfService;
import ru.imagebook.server.service2.app.delivery.PickPointDeliveryService;
import ru.imagebook.server.service2.app.delivery.sdek.SDEKDeliveryService;
import ru.imagebook.server.service2.pdf.PdfTransferService;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.imagebook.shared.model.pricing.PricingDataBuilder;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.serialization.Serializer;
import ru.minogin.core.server.flow.download.DownloadPostController;
import ru.minogin.core.server.flow.remoting.RemotingPostController;
import ru.minogin.core.server.flow.remoting.RemotingPreController;
import ru.minogin.oo.server.OOClient;

import javax.annotation.PostConstruct;

public class StartupServiceImpl implements StartupService {
    private static Logger LOGGER = Logger.getLogger(StartupServiceImpl.class);

    @Autowired
    private VendorController agentController;
    @Autowired
    private Dispatcher dispatcher;
    @Autowired
    private RemotingPreController remotingPreController;
    @Autowired
    private RemotingPostController remotingPostController;
    @Autowired
    private AuthPreController authPreController;
    @Autowired
    private AuthController authController;
    @Autowired
    private SecurityPreController securityPreController;
    @Autowired
    private OrderController orderController;
    @Autowired
    private UserController userController;
    @Autowired
    private ProductController productController;
    @Autowired
    private CoreFactory coreFactory;
    @Autowired
    private OOClient ooClient;
    @Autowired
    private DownloadPostController downloadPostController;
    @Autowired
    private PricingController pricingController;
    @Autowired
    private AdminOrderController adminOrderController;
    @Autowired
    private CalcController calcController;
    @Autowired
    private RequestController requestController;
    @Autowired
    private ActionController actionController;
    @Autowired
    private AdminController adminController;
    @Autowired
    private BillController billController;
    @Autowired
    private QiwiController qiwiController;
    @Autowired
    private SiteController siteController;
    @Autowired
    private LoadService loadService;
    @Autowired
    private FlashService flashService;
    @Autowired
    private FlashController flashController;
    @Autowired
    private PreviewController previewController;
    @Autowired
    private PdfService pdfService;
    @Autowired
    private DeliveryController deliveryController;
    @Autowired
    private FinishingController finishingController;
    @Autowired
    private EditorController editorController;
    @Autowired
    private EditorService editorService;
    @Autowired
    private RegisterController registerController;
    @Autowired
    private MailingController mailingController;
    @Autowired
    private PdfTransferService pdfTransferService;
    @Autowired
    private PickPointDeliveryService pickPointDeliveryService;
    @Autowired
    private SDEKDeliveryService sdekDeliveryService;

    @Value("${enableOOClient:true}")
    private boolean enableOOClient;
    @Value("${enableAsyncServices:true}")
    private boolean enableAsyncServices;

    @Autowired
    Environment environment;

    @PostConstruct
    @Override
    public void start() throws Exception {
        String activeProfile = environment.getActiveProfiles()[0];

        PropertyConfigurator.configure(new ClassPathResource("app." + activeProfile + ".properties").getURL());

        LOGGER.debug("Active profile is: " + activeProfile);

        registerBuilders();

        if (enableOOClient) {
            ooClient.connect();
        }

        remotingPreController.registerHandlers();
        authPreController.registerHandlers();
        securityPreController.registerHandlers();

        agentController.registerHandlers();
        authController.registerHandlers();
        orderController.registerHandlers();
        userController.registerHandlers();
        pricingController.registerHandlers();
        productController.registerHandlers();
        adminOrderController.registerHandlers();
        calcController.registerHandlers();
        requestController.registerHandlers();
        actionController.registerHandlers();
        adminController.registerHandlers();
        billController.registerHandlers();
        qiwiController.registerHandlers();
        siteController.registerHandlers();
        flashController.registerHandlers();
        previewController.registerHandlers();
        deliveryController.registerHandlers();
        finishingController.registerHandlers();
        editorController.registerHandlers();
        registerController.registerHandlers();
        mailingController.registerHandlers();

        downloadPostController.registerHandlers();
        remotingPostController.registerHandlers();

        if (enableAsyncServices) {
            loadService.startAsync();
            flashService.startAsync();
            pdfService.startAsync();
            editorService.startAsync();
            pdfTransferService.start();

            pickPointDeliveryService.start();
            sdekDeliveryService.start();
        }

        dispatcher.send(new BaseMessage(ServerMessages.START));
    }

    private void registerBuilders() {
        Serializer serializer = coreFactory.getSerializer();
        serializer.registerBuilder(PricingData.TYPE_NAME, new PricingDataBuilder());
    }
}
