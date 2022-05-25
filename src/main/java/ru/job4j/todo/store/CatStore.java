package ru.job4j.todo.store;

import net.bytebuddy.description.type.TypeList;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.ArrayList;
import java.util.List;

@Repository
@ThreadSafe
public class CatStore {
    private final SessionFactory sf;

    public CatStore(SessionFactory sf) {
        this.sf = sf;
    }

    public List<Category> findAll() {
        List<Category> result = new ArrayList<>();
        try (Session session = sf.openSession();
             ) {
            session.beginTransaction();
            result = session.createQuery("from Category").list();
            session.getTransaction().commit();
        }
        return result;
    }

    public Category findById(int id) {
        Category result = null;
        try (Session session = sf.openSession();
        ) {
            session.beginTransaction();
            result = session.get(Category.class, id);
            session.getTransaction().commit();
        }
        return result;
    }
}
