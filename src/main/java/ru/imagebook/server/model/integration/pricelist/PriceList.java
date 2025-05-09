package ru.imagebook.server.model.integration.pricelist;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Для каждого товара прайс лист задает цену альбома с заданными параметрами:
 * обложки
 * ламинации обложки
 * ламинации страниц
 * количества заказываемых экземпляров
 * бонусного статуса
 * количества страниц альбома  
 * 
 * @author Svyatoslav Gulin
 * @version 27.09.2011
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "priceList", propOrder = {
    "products" 
})
public class PriceList {
	
	/**
	 * Информация о продуктах
	 */
	@XmlElement(required = true)
	private List<Product> products;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	public void addProduct(Product product) {
		if (this.products == null) {
			this.products = new ArrayList<Product>();
		}
		
		this.products.add(product);
	}
	
	
	public static void main(String[] args) {
		PriceInformation priceInfo1 = new PriceInformation(20, 520);
		PriceInformation priceInfo2 = new PriceInformation(22, 522);
		PriceInformation priceInfo3 = new PriceInformation(24, 524);

		Quantity q1 = new Quantity(1, 100, 0, Arrays.asList(priceInfo1, priceInfo2, priceInfo3));
		Quantity q2 = new Quantity(2, 102, 0, Arrays.asList(priceInfo1, priceInfo2, priceInfo3));
		Quantity q3 = new Quantity(3, 103, 0, Arrays.asList(priceInfo1, priceInfo2, priceInfo3));
		
		PageLamination p1 = new PageLamination(1, "нет", Arrays.asList(q1, q2, q3));
		PageLamination p2 = new PageLamination(2, "нет", Arrays.asList(q1, q2, q3));
		PageLamination p3 = new PageLamination(3, "нет", Arrays.asList(q1, q2, q3));
		
		CoverLamination c1 = new CoverLamination(1, "Матовая", Arrays.asList(p1, p2, p3));
		CoverLamination c2 = new CoverLamination(2, "Матовая", Arrays.asList(p1, p2, p3));
		CoverLamination c3 = new CoverLamination(3, "Матовая", Arrays.asList(p1, p2, p3));
		
		Color col1 = new Color(1, "Цвет 1", Arrays.asList(c1, c2, c3));
		Color col2 = new Color(2, "Цвет 1", Arrays.asList(c1, c2, c3));
		Color col3 = new Color(3, "Цвет 1", Arrays.asList(c1, c2, c3));
		
		Product product = new Product("Формат 1", "Бумага 1", Arrays.asList(col1, col2, col3));
		
		List<Product> products = Arrays.asList(product);
		PriceList price = new PriceList();
		
		price.setProducts(products);
		
        final StringWriter sw = new StringWriter();

        try
        {
            JAXBContext jc = JAXBContext.newInstance(PriceList.class);
            Marshaller m = jc.createMarshaller();
            m.marshal(price, sw);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
		System.out.println(sw.toString());
		
	}
	
}
