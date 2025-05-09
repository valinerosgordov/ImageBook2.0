package ru.imagebook.server.service2.app.delivery.sdek;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryResponse;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.CalculatePriceRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.response.CalculationPriceResponse;

@Component
public class SDEKClientImpl implements SDEKClient {
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${imagebook.delivery.sdek.url}/new_orders.php")
    private String orderUrl;

    @Override
    public DeliveryResponse sendRequestIM(DeliveryRequest deliveryRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, DeliveryRequest> map = new LinkedMultiValueMap<>();
        map.add("xml_request", deliveryRequest);

        HttpEntity<MultiValueMap<String, DeliveryRequest>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(orderUrl, request, DeliveryResponse.class).getBody();
    }

    @Override
    public CalculationPriceResponse sendCalculationRequest(CalculatePriceRequest calculatePriceRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CalculatePriceRequest> requestHttpEntity = new HttpEntity<>(calculatePriceRequest, headers);

        return restTemplate.postForEntity(
                "http://api.cdek.ru/calculator/calculate_price_by_json.php",
                requestHttpEntity,
                CalculationPriceResponse.class
        ).getBody();
    }
}