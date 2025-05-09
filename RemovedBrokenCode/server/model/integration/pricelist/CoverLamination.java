package ru.imagebook.server.model.integration.pricelist;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ru.minogin.core.client.i18n.MultiString;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coverLamination", propOrder = {
    "id", 
    "name",
    "pageLaminations"
})
public class CoverLamination {
	
	/**  
	 * Иденктификатор типа ламинации обложки
	 */
	@XmlElement(required = false)
	private Integer id;

	/**  
	 * Название типа ламинации обложки
	 */
	@XmlElement(required = true)
	private String name;
	
	/**  
	 * Варианты ламинации страниц
	 */
	@XmlElement(required = true)
	private List<PageLamination> pageLaminations;

	public CoverLamination(){
		
	}
	
	public CoverLamination(Integer id, String name,
			List<PageLamination> pageLaminations) {
		super();
		this.id = id;
		this.name = name;
		this.pageLaminations = pageLaminations;
	}

	public List<PageLamination> getPageLaminations() {
		return pageLaminations;
	}

	public void setPageLaminations(List<PageLamination> pageLaminations) {
		this.pageLaminations = pageLaminations;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
