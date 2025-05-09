package ru.imagebook.server.service.site;

import javax.servlet.ServletConfig;

import ru.minogin.core.server.spring.SpringContextSupport;

import com.ckfinder.connector.configuration.Configuration;

public class CKFinderSiteConfiguration extends Configuration {
	public CKFinderSiteConfiguration(ServletConfig servletConfig) {
		super(servletConfig);
	}

	@Override
	protected Configuration createConfigurationInstance() {
		return new CKFinderSiteConfiguration(servletConf);
	}

	@Override
	public void init() throws Exception {
		super.init();

		try {
			SiteConfig siteConfig = SpringContextSupport.getBean(
					servletConf.getServletContext(), SiteConfig.class);
			baseDir = siteConfig.getFilesPath();
			baseURL = siteConfig.getFilesUrl();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
