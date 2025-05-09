package ru.imagebook.shared.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import ru.minogin.core.shared.model.BaseEntityImpl;

@Entity
@Table(name = "vellum")
public class Vellum extends BaseEntityImpl {
    public static final String NUMBER = "number";

    private Boolean active;
    private Integer number;
    private String name;
    private String innerName;
    private String colorRGB;
    private String appImageFilename;
    private String sliderImageFilename;
    private Integer price;
    private Integer phPrice;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }

    public String getColorRGB() {
        return colorRGB;
    }

    public void setColorRGB(String colorRGB) {
        this.colorRGB = colorRGB;
    }

    public String getAppImageFilename() {
        return appImageFilename;
    }

    public void setAppImageFilename(String appImageFilename) {
        this.appImageFilename = appImageFilename;
    }

    public String getSliderImageFilename() {
        return sliderImageFilename;
    }

    public void setSliderImageFilename(String sliderImageFilename) {
        this.sliderImageFilename = sliderImageFilename;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPhPrice() {
        return phPrice;
    }

    public void setPhPrice(Integer phPrice) {
        this.phPrice = phPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Vellum))
            return false;

        Vellum vellum = (Vellum) obj;
        if (vellum.getId() == null || getId() == null)
            return false;

        return vellum.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }
}