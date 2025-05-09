package ru.imagebook.server.model.calc;

import java.io.Serializable;

public class CalcProduct implements Serializable {
    private final int id;
    private final String name;

    public CalcProduct(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
