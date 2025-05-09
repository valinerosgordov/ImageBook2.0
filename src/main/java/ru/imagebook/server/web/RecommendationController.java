package ru.imagebook.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.imagebook.server.service.RecommendationService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping("/recommendation")
public class RecommendationController {

    public static final String RECOMMENDATIONS_KEY = "recommendations";

    @Autowired
    private RecommendationService recommendationService;

    @RequestMapping("/test")
    public String test() {
        return "admin2/recommendationTest";
    }

    @RequestMapping("/initApp")
    public @ResponseBody Map<String, Object> initApp() {
        Map<String, Object> bean = new HashMap<String, Object>();
        bean.put(RECOMMENDATIONS_KEY, recommendationService.findAll());
        return bean;
    }

    public RecommendationService getRecommendationService() {
        return recommendationService;
    }

    public void setRecommendationService(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }
}
