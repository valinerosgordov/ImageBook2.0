package ru.imagebook.server.service.order;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import ru.imagebook.server.service.OrderService;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;

public class NotificationPickupDeliveryJob extends QuartzJobBean {
	private OrderService service;

	public void setService(OrderService service) {
		this.service = service;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			service.callNotificationPickupDelivery();
		} catch (Exception e) {
			ServiceLogger.log(e);
			Exceptions.rethrow(e);
		}
	}
}
