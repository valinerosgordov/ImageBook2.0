package ru.imagebook.shared.model.app;

import java.io.Serializable;


public class MajorData implements Serializable {
    private int cost;
    private int time;

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
