package ru.minogin.ui.server.ckfinder;

import com.ckfinder.connector.ConnectorServlet;
import com.ckfinder.connector.configuration.ConfigurationFactory;
import com.ckfinder.connector.configuration.IConfiguration;
import com.ckfinder.connector.utils.AccessControlUtil;
import ru.minogin.util.shared.exceptions.Exceptions;

import javax.servlet.ServletException;

/**
 * This overriding is needed because of class loader problems with
 * {@link ConnectorServlet}.
 * 
 */
public class CKFinderConnectorServlet extends ConnectorServlet {
	private static final long serialVersionUID = 700700846974638252L;

	@Override
	public void init() throws ServletException {
		super.init();

		IConfiguration configuration = new XConfiguration(getServletConfig());
		try {
			configuration.init();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
		AccessControlUtil.getInstance(configuration).loadACLConfig();
		ConfigurationFactory.getInstace().setConfiguration(configuration);
	}
}
