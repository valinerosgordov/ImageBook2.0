package ru.imagebook.server.service2.app.delivery.sdek;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.imagebook.server.service2.app.delivery.DeliveryConfig;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryPackage;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.SDEKPackage;


@Service
public class SDEKDeliveryServiceImpl implements SDEKDeliveryService {
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private DeliveryConfig config;
    @Autowired
    private SDEKHelper sdekHelper;
    @Autowired
    private SDEKTransferService transferSendingService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start() {
        logger.debug("SDEK scheduler is starting...");

        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    transferSendingService.transfer();
                } catch (Exception e) {
                    logger.error("Creating of sending in SDEK failed", e);
                }
            }
        }, 0, config.getSdekSendDelayInSec(), TimeUnit.SECONDS);

        logger.debug("SDEK scheduler started");
    }

    @Override
    public List<SDEKPackage> computePackages(Bill bill) {
        List<SDEKPackage> sdekPackages = new ArrayList<>();
        for (DeliveryPackage p : sdekHelper.computePackages(bill)) {
            double weightKg = BigDecimal.valueOf(p.getWeight())
                .divide(BigDecimal.valueOf(1000), 3, BigDecimal.ROUND_HALF_EVEN)
                .doubleValue();
            sdekPackages.add(new SDEKPackage(p.getSizeA(), p.getSizeB(), p.getSizeC(), weightKg));
        }
        return sdekPackages;
    }
}
