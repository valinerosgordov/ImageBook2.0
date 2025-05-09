package ru.imagebook.server.repository;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.server.hibernate.BaseRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionCategoryRepositoryImpl extends BaseRepository implements QuestionCategoryRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<QuestionCategory> loadQuestionCategories(int offset, int limit) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionCategory.class);
        criteria.addOrder(Order.asc(QuestionCategory.NUMBER));
        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);
        return criteria.list();
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
    public void deleteQuestionCategories(List<Integer> ids) {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionCategory.class);
        criteria.add(Restrictions.in(QuestionCategory.ID, ids));
        List<QuestionCategory> questionCategories = criteria.list();
        for (QuestionCategory questionCategory : questionCategories) {
            session.delete(questionCategory);
        }
    }

    @Override
    public QuestionCategory getQuestionCategory(int id) {
        Session session = getSession();
        return (QuestionCategory) session.get(QuestionCategory.class, id);

    }

    @Override
    public void saveQuestionCategory(QuestionCategory questionCategory) {
        Session session = getSession();
        session.save(questionCategory);
    }


    @Override
    public long countQuestionCategories() {
        Session session = getSession();
        Criteria criteria = session.createCriteria(QuestionCategory.class);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }
}
