package ru.minogin.ui.server.ckfinder;

import com.ckfinder.connector.configuration.Configuration;
import ru.minogin.util.server.spring.SpringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

public class XConfiguration extends Configuration {
	private CKFinderConfig config;

	public XConfiguration(ServletConfig servletConfig) {
		super(servletConfig);

		config = SpringUtil.getBean(servletConf.getServletContext(),
				CKFinderConfig.class);
	}

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	protected Configuration createConfigurationInstance() {
		return new XConfiguration(servletConf);
	}

	@Override
	public boolean checkAuthentication(HttpServletRequest request) {
		return config.checkAuthentication(request);
	}

	@Override
	public String getBaseDir() {
		return config.getBaseDir();
	}

	@Override
	public String getBaseURL() {
		return config.getBaseURL();
	}
}
