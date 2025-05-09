package ru.imagebook.server.model.importing;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import ru.imagebook.shared.model.User;

@XmlRootElement(name = "user")
public class XUser implements Serializable {
    private String username;
    private String prevUsername;
    private boolean photographer;
    private boolean wholesaler;
    private boolean registered;

    private XVendor vendor;

    public XUser() {
    }

    public XUser(String username, boolean photographer, boolean registered, XVendor vendor) {
        this.username = username;
        this.photographer = photographer;
        this.registered = registered;
        this.vendor = vendor;
    }

    public static XUser create(User user) {
        XVendor vendor = XVendor.create(user.getVendor());
        return new XUser(user.getUserName(), user.isPhotographer(), user.isRegistered(), vendor);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrevUsername() {
        return prevUsername;
    }

    public void setPrevUsername(String prevUsername) {
        this.prevUsername = prevUsername;
    }

    public boolean isPhotographer() {
        return photographer;
    }

    public void setPhotographer(boolean photographer) {
        this.photographer = photographer;
    }

    public boolean isWholesaler() {
        return wholesaler;
    }

    public void setWholesaler(boolean wholesaler) {
        this.wholesaler = wholesaler;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public XVendor getVendor() {
        return vendor;
    }

    public void setVendor(XVendor vendor) {
        this.vendor = vendor;
    }
}
