package ru.minogin.core.server.flow.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import ru.minogin.core.client.exception.Exceptions;

public class Downloads {
	public static void startDownload(String path, String name, HttpServletRequest request,
			HttpServletResponse response) {
		startDownload(new File(path), name, request, response);
	}

	public static void startDownload(File file, String name, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			ServletContext context = request.getSession().getServletContext();
			String mimeType = context.getMimeType(name);
			if (mimeType == null)
				mimeType = "application/octet-stream";
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			response.setCharacterEncoding("UTF-8");

			String userAgent = request.getHeader("USER-AGENT").toLowerCase();
			if (userAgent != null) {
				if (userAgent.indexOf("opera") != -1) {
					name = URLEncoder.encode(name, "UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename*=utf-8''" + name);
				}
				else if (userAgent.indexOf("msie") != -1 || userAgent.indexOf("chrome") != -1) {
					name = URLEncoder.encode(name, "UTF-8");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
				}
				else if (userAgent.indexOf("mozilla") != -1) {
//					name = URLEncoder.encode(name, "UTF-8");
//					response.setHeader("Content-Disposition", "attachment; filename*=\"utf-8'" + name + "\"");
					response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
				}
			}
			else {
				name = URLEncoder.encode(name, "UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename*=\"utf-8'" + name + "\"");
			}

			FileInputStream is = new FileInputStream(file);
			BufferedOutputStream os = new BufferedOutputStream(response.getOutputStream());
			IOUtils.copy(is, os);
			is.close();
			os.flush();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
