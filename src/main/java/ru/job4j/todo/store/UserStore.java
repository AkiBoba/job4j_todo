package ru.job4j.todo.store;


import net.jcip.annotations.ThreadSafe;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@ThreadSafe
public class UserStore {
    private final SessionFactory sf;

    public UserStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<User> add(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(user);
    }

    public Role addRole(Role role) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(role);
        session.getTransaction().commit();
        session.close();
        return role;
    }

    public boolean update(User user) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public void delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    public List<User> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from User").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public User findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        User result = session.get(User.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(User.class, "User");
        criteria.add(Restrictions.eq("password", password));
        criteria.add(Restrictions.eq("email", email));
        User user = (User) criteria.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return Optional.ofNullable(user);
    }
}
