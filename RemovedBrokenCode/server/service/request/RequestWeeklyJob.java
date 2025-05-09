package ru.imagebook.server.service.request;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;

public class RequestWeeklyJob extends QuartzJobBean {
	private RequestService service;

	public void setService(RequestService service) {
		this.service = service;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			service.sendWeeklyBookRequest();
		}
		catch (Exception e) {
			ServiceLogger.log(e);
			Exceptions.rethrow(e);
		}
	}
}