package ru.minogin.twitter.server.service.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.twitter.server.config.site.TwitterConfig;
import ru.minogin.twitter.shared.model.site.Twit;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Получение новостей из твиттера
 * 
 * @author pavel.shirokih, dmitry.urbanovich
 * 
 */
public class TwitterServiceImpl implements TwitterService {

	private final Pattern linkSiteFormat = Pattern.compile("http://t.co/[\\S]*");

	@Autowired
	protected TwitterConfig twitterConfig;

	private TwitterFactory twitterFactory;

	private long lastUpdatedTimeInMillis;
	private List<Twit> cachedTwits;

	@PostConstruct
	public void init() {
		if (twitterConfig.getOAuthAccessToken() == null || twitterConfig.getOAuthAccessTokenSecret() == null
				|| twitterConfig.getOAuthConsumerKey() == null || twitterConfig.getOAuthConsumerSecret() == null
				|| twitterConfig.getOAuthAccessToken().isEmpty() || twitterConfig.getOAuthAccessTokenSecret().isEmpty()
				|| twitterConfig.getOAuthConsumerKey().isEmpty() || twitterConfig.getOAuthConsumerSecret().isEmpty()) {
			throw new IllegalStateException("One of OAuth parameters is not present");
		}
		if (twitterConfig.getCacheLifetime() < 0) {
			throw new IllegalStateException("Cache lifetime cannot be negative");
		}

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey(twitterConfig.getOAuthConsumerKey())
				.setOAuthConsumerSecret(twitterConfig.getOAuthConsumerSecret())
				.setOAuthAccessToken(twitterConfig.getOAuthAccessToken())
				.setOAuthAccessTokenSecret(twitterConfig.getOAuthAccessTokenSecret())
				.setRestBaseURL("https://api.twitter.com/1.1/").setUseSSL(true);
		twitterFactory = new TwitterFactory(cb.build());
	}

	public void setCountNews(int countNews) {
		if (twitterConfig.getTwitsCount() != countNews) {
			twitterConfig.setTwitsCount(countNews);
			cachedTwits = null;
		}
	}

	/**
	 * Получить новости
	 */
	@Override
	public synchronized List<Twit> getNews() {
		long currentTime = System.currentTimeMillis();

		if (cachedTwits != null
				&& (currentTime - lastUpdatedTimeInMillis) <= twitterConfig.getCacheLifetime() * 60 * 1000) {
			return cachedTwits;
		}

		List<Twit> twits = new ArrayList<Twit>();

		try {
			Twitter twitter = twitterFactory.getInstance();
			Paging paging = new Paging(1, twitterConfig.getTwitsCount());
			String twitterLogin = twitterConfig.getTwitterLogin();
			List<Status> statuses = Collections.emptyList();
			if (twitterLogin == null || twitterLogin.isEmpty()) {
				statuses = twitter.getUserTimeline(paging);
			} else {
				statuses = twitter.getUserTimeline(twitterLogin, paging);
			}
			for (Status status : statuses) {
				String text = status.getText();

				Twit currentTwit = new Twit();

				currentTwit.setCreatedDate(status.getCreatedAt());
				currentTwit.setLink("http://twitter.com/" + status.getUser().getScreenName() + "/status/"
						+ status.getId());

				Matcher m = linkSiteFormat.matcher(text);
				if (m.find()) {
					currentTwit.setLinkSite(m.group(0));
				}
				currentTwit.setText(text.replaceAll(linkSiteFormat.pattern(), ""));
				twits.add(currentTwit);
			}
		} catch (TwitterException e1) {
			Exceptions.rethrow(e1);
		}

		lastUpdatedTimeInMillis = System.currentTimeMillis();
		cachedTwits = Collections.unmodifiableList(twits);

		return cachedTwits;
	}

}
