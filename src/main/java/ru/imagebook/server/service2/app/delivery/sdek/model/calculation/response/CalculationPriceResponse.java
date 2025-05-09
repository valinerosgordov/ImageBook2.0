package ru.imagebook.server.service2.app.delivery.sdek.model.calculation.response;

public class CalculationPriceResponse {
    private ResultDto result;

    public CalculationPriceResponse(ResultDto result) {
        this.result = result;
    }

    public ResultDto getResult() {
        return result;
    }

    public void setResult(ResultDto result) {
        this.result = result;
    }
}
