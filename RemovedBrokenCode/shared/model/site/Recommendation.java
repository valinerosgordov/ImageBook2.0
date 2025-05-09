package ru.imagebook.shared.model.site;

import org.springframework.format.annotation.DateTimeFormat;
import ru.minogin.core.shared.model.BaseEntityImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by zinchenko on 08.09.14.
 */
@Entity
@Table(name = "recommendation")
public class Recommendation extends BaseEntityImpl {

    private String name;

    private String imageName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "image_name")
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
