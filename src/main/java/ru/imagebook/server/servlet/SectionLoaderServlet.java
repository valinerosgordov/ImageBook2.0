package ru.imagebook.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service2.web.WebService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.site.Section;
import ru.minogin.core.server.spring.SpringContextAwareServlet;

/**
 * Сервлет предоставления информации об иерархии разделов (групп) альбомов
 * 
 * @author Svyatoslav Gulin
 * @since 20.09.2011
 */
public class SectionLoaderServlet extends SpringContextAwareServlet {
	
	public static final String WEB_SERVICE = "webService";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		WebService webService = getBean(WEB_SERVICE);
		List<Section> sections = webService.loadSections1();
		
		
		response.setContentType("text/xml; charset=utf-8");
		writeAlbums(response.getWriter(), sections);
	}
	
	private void writeAlbums(PrintWriter out, List<Section> sections) {
		out.write("<albums>");
		
		out.write("</albums>");
	}
	
	
	private StringBuilder buildAlbumDataAsXml(Album album) {
		StringBuilder sb = new StringBuilder();
		sb.append("<album>");
		
		
		sb.append("</album>");
		return sb;
		
	}
	
	
}