package ru.imagebook.server.repository;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.server.hibernate.BaseRepository;

import java.util.List;

public class QuestionRepositoryImpl extends BaseRepository implements QuestionRepository {
    @SuppressWarnings("unchecked")
    @Override
    public List<Question> loadQuestions(Integer questionCategoryId, int offset, int limit) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Question.class);
        criteria.setFetchMode(Question.QUESTION_CATEGORY, FetchMode.JOIN);
        if (questionCategoryId == null) {
            criteria.add(Restrictions.isNull(Question.QUESTION_CATEGORY + "." + Question.ID));
        } else {
            criteria.add(Restrictions.eq(Question.QUESTION_CATEGORY + "." + Question.ID, questionCategoryId));
        }
        criteria.addOrder(Order.desc(Question.DATE));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteQuestions(List<Integer> ids) {
        if (!ids.isEmpty()) {
            Session session = getSession();
            Criteria criteria = session.createCriteria(Question.class);
            criteria.add(Restrictions.in(Question.ID, ids));
            List<Question> questions = criteria.list();
            for (Question question : questions) {
                session.delete(question);
            }
        }
    }

    @Override
    public Question getQuestion(int id) {
        Session session = getSession();
        return (Question) session.get(Question.class, id);

    }

    @Override
    public void saveQuestion(Question question) {
        Session session = getSession();
        session.save(question);
    }

    @Override
    public QuestionCategory getQuestionCategory(Integer questionCategoryId) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionCategory.class);
        criteria.add(Restrictions.eq(QuestionCategory.ID, questionCategoryId));
        return (QuestionCategory) criteria.uniqueResult();
    }

    @Override
    public long countQuestions(Integer questionCategoryId) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Question.class);
        criteria.setFetchMode(Question.QUESTION_CATEGORY, FetchMode.JOIN);
        if (questionCategoryId == null) {
            criteria.add(Restrictions.isNull(Question.QUESTION_CATEGORY + "." + Question.ID));
        } else {
            criteria.add(Restrictions.eq(Question.QUESTION_CATEGORY + "." + Question.ID, questionCategoryId));
        }
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuestionCategory> loadAllQuestionCategories() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionCategory.class);
        criteria.addOrder(Order.asc(QuestionCategory.NUMBER));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Question> getCategoryQuestions(int categoryId, int offset, int limit) {
        Criteria criteria = createCriteria(Question.class);
        criteria.add(Restrictions.eq(Question.QUESTION_CATEGORY + "." + Question.ID, categoryId));
        criteria.add(Restrictions.eq(Question.PUBL, Boolean.TRUE));
        criteria.addOrder(Order.desc(Question.DATE));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @Override
    public long countCategoryQuestions(int categoryId) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(Question.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq(Question.PUBL, Boolean.TRUE));
        criteria.add(Restrictions.eq(Question.QUESTION_CATEGORY + "." + Question.ID, categoryId));
        return (Long) criteria.uniqueResult();
    }

    @Override
    public QuestionCategory getQuestionCategory(int id) {
        Criteria criteria = createCriteria(QuestionCategory.class);
        criteria.add(Restrictions.idEq(id));
        return (QuestionCategory) criteria.uniqueResult();
    }
}
