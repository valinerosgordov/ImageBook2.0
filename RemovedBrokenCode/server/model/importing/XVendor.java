package ru.imagebook.server.model.importing;

import java.io.Serializable;

import ru.imagebook.shared.model.Vendor;

public class XVendor implements Serializable {
    private String key;
    private String password;

    public XVendor() {
    }

    public XVendor(String key, String password) {
        this.key = key;
        this.password = password;
    }

    public static XVendor create(Vendor vendor) {
        return new XVendor(vendor.getKey(), null); // TODO vendor password
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
