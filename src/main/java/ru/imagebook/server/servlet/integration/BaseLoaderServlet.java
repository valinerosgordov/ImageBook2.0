package ru.imagebook.server.servlet.integration;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ru.imagebook.server.service.site.SiteConfig;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

public abstract class BaseLoaderServlet<T> extends SpringContextAwareServlet {
	
	protected static final String PRODUCT_SERVICE = "productService";
	protected static final String ORDER_SERVICE = "orderService";
	protected static final String WEB_SERVICE = "webService";
	protected static final String PRICE_SERVICE = "priceService";
	
	private static final String SITE_CONFIG = "siteConfig";
	protected static final String INTEGRATION_CONFIG = "integrationConfig";
	
	private static final String INTEGRATION_CODE = "integration.code";
	protected static final String INTEGRATION_ACCOUNT = "account";
	
	protected abstract Class getObjectClass();
	
	protected void writeResponse(PrintWriter out, T data) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(getObjectClass());
        Marshaller m = jc.createMarshaller();
        m.marshal(data, out);
	}
	
	protected SiteConfig getSiteConfig() {
		return getBean(SITE_CONFIG);
	}
	
	protected void writeEmptyResponse(PrintWriter out) {
		out.write("<" + getObjectClass().getSimpleName() + "/>");
	}
	
	protected void checkIntegrationCode(HttpServletRequest request, HttpServletResponse response) 
	throws IOException {
		
		if (! verifyIntegrationCode(request.getParameter(INTEGRATION_ACCOUNT), 
				request.getParameter(INTEGRATION_CODE))) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	
	private boolean verifyIntegrationCode(String accountName, String integrationCode) {
		IntegrationConfig integConfig = getBean(INTEGRATION_CONFIG);
		
		if (! integConfig.getAccounts().containsKey(accountName)) {
			return false;
		}
		
		String accountCode = integConfig.getAccounts().get(accountName).getAccountCode();
		
		if ((accountCode != null) && (!"".equals(accountCode)) && (accountCode.equals(integrationCode))) {
			return true;
		}
		
		return false;
		
	}

	
	
	
	
}
