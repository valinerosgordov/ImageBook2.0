package ru.minogin.twitter.server.config.site;

/**
 * Конфигурационные параметры для твиттера
 * 
 * @author pavel.shirokih, dmitry.urbanovich
 * 
 */
public class TwitterConfig {

	/** Имя пользователя отображаемой ленты */
	private String twitterLogin;

	/** html шаблон */
	private String template;

	private String oAuthConsumerKey;
	private String oAuthConsumerSecret;
	private String oAuthAccessToken;
	private String oAuthAccessTokenSecret;
	private long cacheLifetime;
	private int twitsCount = 10;

	public String getTwitterLogin() {
		return twitterLogin;
	}

	public void setTwitterLogin(String twitterLogin) {
		this.twitterLogin = twitterLogin;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getOAuthConsumerKey() {
		return oAuthConsumerKey;
	}

	public void setOAuthConsumerKey(String oAuthConsumerKey) {
		this.oAuthConsumerKey = oAuthConsumerKey;
	}

	public String getOAuthConsumerSecret() {
		return oAuthConsumerSecret;
	}

	public void setOAuthConsumerSecret(String oAuthConsumerSecret) {
		this.oAuthConsumerSecret = oAuthConsumerSecret;
	}

	public String getOAuthAccessToken() {
		return oAuthAccessToken;
	}

	public void setOAuthAccessToken(String oAuthAccessToken) {
		this.oAuthAccessToken = oAuthAccessToken;
	}

	public String getOAuthAccessTokenSecret() {
		return oAuthAccessTokenSecret;
	}

	public void setOAuthAccessTokenSecret(String oAuthAccessTokenSecret) {
		this.oAuthAccessTokenSecret = oAuthAccessTokenSecret;
	}

	public long getCacheLifetime() {
		return cacheLifetime;
	}

	public void setCacheLifetime(long cacheLifetime) {
		this.cacheLifetime = cacheLifetime;
	}

	public int getTwitsCount() {
		return twitsCount;
	}

	public void setTwitsCount(int twitsCount) {
		this.twitsCount = twitsCount;
	}

}
