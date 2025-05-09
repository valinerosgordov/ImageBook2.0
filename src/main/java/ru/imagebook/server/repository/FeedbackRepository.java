package ru.imagebook.server.repository;

import ru.imagebook.shared.model.site.Feedback;

import java.util.List;

/**
 * Created by zinchenko on 07.09.14.
 */
public interface FeedbackRepository extends Repository<Feedback, Integer> {

    List<Feedback> getRange(Integer from, Integer to);

}
