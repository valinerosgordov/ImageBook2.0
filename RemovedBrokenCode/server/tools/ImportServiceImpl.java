package ru.imagebook.server.tools;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.imagebook.shared.model.Email;
import ru.imagebook.shared.model.Phone;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.UserAccount;
import ru.minogin.core.client.CoreFactory;
import ru.minogin.core.client.crypto.Hasher;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportServiceImpl implements ImportService {
    private static final String TAG = "24com";

    private static final String PASSWORD = "jTWAfofsA0Ul";

    private final JdbcTemplate jdbcTemplate;

    private final CoreFactory coreFactory;

    @PersistenceContext
    private EntityManager entityManager;

    public ImportServiceImpl(DataSource importSource, CoreFactory coreFactory) {
        this.coreFactory = coreFactory;
        jdbcTemplate = new JdbcTemplate(importSource);
    }

    @Override
    public void load() {
        List<XContact> contacts = jdbcTemplate.query("select * from contact group by email",
                new RowMapper<XContact>() {
                    @Override
                    public XContact mapRow(ResultSet rs, int rowNum) throws SQLException {
                        XContact contact = new XContact();
                        contact.setId(rs.getInt("id"));
                        contact.setName(rs.getString("name"));
                        contact.setPhone(rs.getString("phone"));
                        contact.setEmail(rs.getString("email"));
                        return contact;
                    }
                });

        List<Request> requests = jdbcTemplate.query("select * from request", new RowMapper<Request>() {
            @Override
            public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
                Request request = new Request();
                request.setName(rs.getString("name"));
                request.setDate(rs.getDate("date"));
                request.setText(rs.getString("description"));
                request.setContactId(rs.getInt("initiator"));
                return request;
            }
        });
        Map<Integer, Request> contact2Request = new HashMap<Integer, Request>();
        for (Request request : requests) {
            contact2Request.put(request.getContactId(), request);
        }

        Hasher hasher = coreFactory.getHasher();
        String hash = hasher.hash(PASSWORD);


        Session session = entityManager.unwrap(Session.class);
        Transaction tx = session.beginTransaction();

        try {
            int i = 0;
            for (XContact contact : contacts) {
                String name = contact.getName();
                if (name == null || name.isEmpty())
                    continue;

                String email = contact.getEmail();
                if (email == null || email.isEmpty())
                    continue;

                Request request = contact2Request.get(contact.getId());
                if (request == null)
                    continue;

                Criteria criteria = session.createCriteria(User.class);
                criteria.setProjection(Projections.rowCount());
                criteria.add(Restrictions.eq(User.USER_NAME, email));
                long count = (Long) criteria.uniqueResult();
                if (count > 0)
                    continue;

                User user = new User();
                user.setUserName(email);
                user.setName(name);

                Email xEmail = user.getFirstEmail();
                xEmail.setActive(true);
                xEmail.setEmail(email);

                Phone phone = new Phone();
                phone.setPhone(contact.getPhone());
                user.addPhone(phone);

                String info = TAG + "\n";

                info += "Номер: " + request.getName() + "\n";
                info += "Дата: " + request.getDate() + "\n";
                info += request.getText();

                user.setInfo(info);

                session.save(user);

                UserAccount account = new UserAccount(email, hash, true, user);
                session.save(account);

                i++;
            }

            tx.commit();
        }
        finally {
            session.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clean() {
        Session session = entityManager.unwrap(Session.class);
        Transaction tx = session.beginTransaction();

        try {
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.like(User.INFO, TAG, MatchMode.START));
            List<User> users = criteria.list();
            for (User user : users) {
                criteria = session.createCriteria(UserAccount.class);
                criteria.add(Restrictions.eq(UserAccount.USER, user));
                UserAccount account = (UserAccount) criteria.uniqueResult();
                session.delete(account);

                session.delete(user);
            }

            tx.commit();
        }
        finally {
            session.close();
        }
    }
}
