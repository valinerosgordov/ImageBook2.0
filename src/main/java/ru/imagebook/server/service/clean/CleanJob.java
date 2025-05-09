package ru.imagebook.server.service.clean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;

public class CleanJob extends QuartzJobBean {
	private CleanService service;

	public void setService(CleanService service) {
		this.service = service;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			service.clean();
		} catch (Exception e) {
			ServiceLogger.log(e);
			Exceptions.rethrow(e);
		}
	}
}
