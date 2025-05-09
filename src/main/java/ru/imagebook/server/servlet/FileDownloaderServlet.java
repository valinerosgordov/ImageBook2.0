package ru.imagebook.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.imagebook.server.service.FileConfig;
import ru.minogin.core.server.flow.download.Downloads;
import ru.minogin.core.server.flow.remoting.FlowServlet;


public class FileDownloaderServlet extends FlowServlet{
	
	private static final long serialVersionUID = 8290653151858143649L;
	
	public static final String FILE_CONFIG = "fileConfig";
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
				
		FileConfig fileConfig = getBean(FILE_CONFIG);			
		
		//получить из response имя файла
		String name = request.getParameter("filename");
		
		//получить из конфигов путь к папке с файлами
		String path = fileConfig.getDownloadPath() + "/" + name;
		
		//отправить полученные данные классу Downloads
		Downloads.startDownload(path, name, request, response);
	}
}
