package ru.imagebook.server.service.backup;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.ServiceLogger;

public class BackupJob extends QuartzJobBean {
	private BackupService service;

	public void setService(BackupService service) {
		this.service = service;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			service.backup();
		}
		catch (Exception e) {
			ServiceLogger.log(e);
			Exceptions.rethrow(e);
		}
	}
}
