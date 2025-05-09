package ru.minogin.twitter.shared.model.site;

import java.util.Date;

/**
 * Запись в твиттере
 * 
 * @author pavel.shirokih
 * 
 */
public class Twit {
	/** Дата написания твита */
	private Date createdDate;

	/** Текст твита */
	private String text;

	/** Ссылка на этот твит */
	private String link;

	/** Ссылка на новость с фотографиями на сайте */
	private String linkSite;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkSite() {
		return linkSite;
	}

	public void setLinkSite(String linkSite) {
		this.linkSite = linkSite;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Twit other = (Twit) obj;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		return true;
	}

}
