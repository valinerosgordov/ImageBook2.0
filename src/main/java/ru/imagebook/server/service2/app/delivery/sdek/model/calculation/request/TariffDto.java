package ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request;

public class TariffDto {
    private int id;
    private int priority;

    public TariffDto(int id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
