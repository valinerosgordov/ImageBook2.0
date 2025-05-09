package ru.imagebook.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.imagebook.server.model.feedback.FeedbackView;
import ru.imagebook.server.service.FeedbackService;
import ru.imagebook.shared.model.site.Feedback;

import java.util.List;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackApi extends BaseGenericRestController<Feedback, Integer> {

    private FeedbackService feedbackService;

    @Autowired
    public FeedbackApi(FeedbackService service) {
        super(service);
        feedbackService = service;
    }

    @RequestMapping(value = "/range/{from}/{to}", method = RequestMethod.GET)
    public @ResponseBody
    List<Feedback> getRange(@PathVariable Integer from, @PathVariable Integer to) {
        return feedbackService.getRange(from, to);
    }

    @RequestMapping(value = "/saveView", method = RequestMethod.POST)
    public @ResponseBody
    Feedback saveView(@RequestBody FeedbackView feedbackView) {
        return feedbackService.save(feedbackView);
    }


}
