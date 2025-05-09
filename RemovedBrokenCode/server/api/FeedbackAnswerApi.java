package ru.imagebook.server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.imagebook.server.service.FeedbackAnswerService;
import ru.imagebook.shared.model.site.FeedbackAnswer;

/**
 * Created by zinchenko on 14.09.14.
 */
@Controller
@RequestMapping("/feedback_answer")
public class FeedbackAnswerApi extends BaseGenericRestController<FeedbackAnswer, Integer> {

    private FeedbackAnswerService service;

    @Autowired
    public FeedbackAnswerApi(FeedbackAnswerService service) {
        super(service);
        this.service = service;
    }



}
