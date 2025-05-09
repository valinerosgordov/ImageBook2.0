package ru.imagebook.server.servlet.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import ru.imagebook.shared.model.Album;

public abstract class FilterLoaderServlet<T> extends BaseLoaderServlet<T> {
	
	protected List<String> getExcludeAblbumMasks(String accountName) {
		IntegrationConfig integConfig = getBean(INTEGRATION_CONFIG);
		
		if (! integConfig.getAccounts().containsKey(accountName)) {
			throw new RuntimeException("Account with name '" + accountName + "' does not registered");
		}
		
		return integConfig.getAccounts().get(accountName).getExcludeAlbumMasks();
	}
		
	protected List<String> getExcludeAblbumMasks(HttpServletRequest request) {
		return getExcludeAblbumMasks(request.getParameter(INTEGRATION_ACCOUNT));
	}
	
	protected List<Album> filterAlbums(String accountName, List<Album> albums) {
		if ((albums == null) || (albums.isEmpty())) {
			return null;
		}
		
		List<String> excludeAlbumMasks = getExcludeAblbumMasks(accountName);
		List<Pattern> filter = getPatterns(excludeAlbumMasks);
		
		List<Album> filteredAlbums = new ArrayList<Album>();
		for(Album album: albums) {
			if (! isAlbumMatch(filter, "01-" 
					+ (album.getType() > 9 ? album.getType() : "0" + album.getType())  
					+ "-" 
					+ (album.getNumber() > 9 ? album.getNumber() : "0" + album.getNumber())
					)) {
				filteredAlbums.add(album);
			}
		}
		
		return filteredAlbums;
	}

	private List<Pattern> getPatterns(List<String> excludeAlbumMasks) {
		if ((excludeAlbumMasks == null) || (excludeAlbumMasks.isEmpty())) {
			return null;
		}
		
		List<Pattern> patterns = new ArrayList<Pattern>();
		String template = "";
		for(String mask: excludeAlbumMasks) {
			template = mask.replace("AA", "(.*)");
			template = template.replace("BB", "(.*)");
			template = template.replace("CC", "(.*)");
			Pattern pattern = Pattern.compile(template);
			patterns.add(pattern);
		}
		
		return patterns;
	}
	
	private boolean isAlbumMatch(List<Pattern> patterns, String article) {
		if ((patterns == null) || (patterns.isEmpty())) {
			return false;
		}
			
		for(Pattern pattern: patterns) {
			Matcher matcher = pattern.matcher(article);
			if (matcher.find())	{
				return true;
			}
		}
		
		return false;
	}
	
}
