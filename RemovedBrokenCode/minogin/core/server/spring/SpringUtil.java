package ru.minogin.core.server.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.client.text.StringUtil;
import ru.minogin.core.server.file.FileUtil;

public class SpringUtil {
	public static String read(Resource resource) {
		try {
			return FileUtil.read(resource.getFile());
		}
		catch (IOException e) {
			return Exceptions.rethrow(e);
		}
	}

	public static void executeSql(JdbcTemplate jdbcTemplate, String sql) {
		String[] statements = sql.split(";");
		for (String statement : statements) {
			statement = StringUtil.trim(statement);
			if (statement != null)
				jdbcTemplate.execute(statement);
		}
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes.getRequest();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}
}
