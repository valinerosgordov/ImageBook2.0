package ru.imagebook.server.service2.app.delivery;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.imagebook.client.app.service.PostamateUnavailableException;
import ru.imagebook.shared.model.app.PickPointData;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sergey Boykov
 */
public class PickPointDeliveryServiceImpl implements PickPointDeliveryService {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private PickPointHelper pickPointHelper;
    @Autowired
    private DeliveryConfig config;
    @Autowired
    private PickPointTransferSendingService pickPointTransferSendingService;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @SuppressWarnings("unchecked")
    @Override
    public PickPointData getZoneInfoByPostamateId(String postamateId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(postamateId), "postamateId should be provided");

        Map<String, PickPointData> zonesInfo = pickPointHelper.getZones();

        if (zonesInfo == null || zonesInfo.isEmpty()) {
            String msg = "Zones array is empty";
            logger.error(msg);
            throw new RuntimeException(msg);
        }

        PickPointData foundPostamateData = zonesInfo.get(postamateId);
        if (foundPostamateData == null) {
            String msg = String.format("Can't get zoneInfo for postamateId=[%s]", postamateId);
            logger.error(msg);
            throw new PostamateUnavailableException(msg);
        }

        return foundPostamateData;
    }

    @Override
    public void start() {
        logger.debug("PickPoint scheduler is starting...");

        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    pickPointTransferSendingService.transfer();
                } catch (Exception e) {
                    logger.error("Creating of sending in PickPoint failed", e);
                }
            }
        }, 0, config.getPickPointSendPeriodicity(), TimeUnit.MILLISECONDS);

        logger.debug("PickPoint scheduler started");
    }
}
