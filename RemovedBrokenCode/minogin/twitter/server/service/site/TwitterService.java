package ru.minogin.twitter.server.service.site;

import java.util.List;

import ru.minogin.twitter.shared.model.site.Twit;

/**
 * Сервис получения записей из твиттера
 * 
 * @author pavel.shirokih
 * 
 */
public interface TwitterService {
	/**
	 * Получить новости
	 * 
	 * @return Список записей в твиттере
	 */
	List<Twit> getNews();
}
