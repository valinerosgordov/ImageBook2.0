package ru.imagebook.server.service;

import ru.imagebook.server.model.feedback.FeedbackView;
import ru.imagebook.shared.model.site.Feedback;

import java.util.List;

/**
 * Created by zinchenko on 07.09.14.
 */
public interface FeedbackService extends Service<Feedback, Integer> {

    List<Feedback> getRange(Integer from, Integer to);

    List<Feedback> getFirstPage();

    Feedback save(FeedbackView feedbackView);

}
