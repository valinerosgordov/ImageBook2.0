package ru.imagebook.server.model.export.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "order")
@XmlType(propOrder = {"number", "article", "deadline", "copies", "pageLamination",
    "coverLamination", "nPages", "coverType"})
public class OrderExpItem {
    private String number;
    private String article;
    private String deadline;
    private Integer copies;
    private String pageLamination;
    private String coverLamination;
    private Integer nPages;
    private String coverType;
    
    public OrderExpItem() {
    }

    @XmlElement(name = "order_id")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @XmlElement(name = "page_lamination")
    public String getPageLamination() {
        return pageLamination;
    }

    public void setPageLamination(String pageLamination) {
        this.pageLamination = pageLamination;
    }

    @XmlElement(name = "cover_lamination")
    public String getCoverLamination() {
        return coverLamination;
    }

    public void setCoverLamination(String coverLamination) {
        this.coverLamination = coverLamination;
    }

    @XmlElement(name = "n_pages")
    public Integer getnPages() {
        return nPages;
    }

    public void setnPages(Integer nPages) {
        this.nPages = nPages;
    }

    @XmlElement(name = "cover_type")
    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }
}