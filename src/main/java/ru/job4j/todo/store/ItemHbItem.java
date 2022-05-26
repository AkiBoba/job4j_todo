package ru.job4j.todo.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;

@Repository
@ThreadSafe
public class ItemHbItem {

    private final SessionFactory sf;

    public ItemHbItem(SessionFactory sf) {
        this.sf = sf;
    }

    public List getItems() {
        List list;
        try (Session session = sf.openSession()
        ) {
            session.beginTransaction();
            list = session.createQuery(
                    "select distinct it from Item it join fetch it.categoryes"
            ).list();
            session.getTransaction().commit();
        }
        return list;
    }

    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public boolean update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = session.get(Item.class, id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public List<Item> findAlldone() {
        List<Item> items;
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Item.class, "Item");
        criteria.add(Restrictions.eq("done", true));
        items = criteria.list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public List<Item> newItems() {
        List<Item> items;
        Session session = sf.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(Item.class, "Item");
        criteria.add(Restrictions.eq("done", false));
        criteria.addOrder(Order.desc("created"));
        items = criteria.list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public void done(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item itmTmp = session.get(Item.class, id);
        itmTmp.setDone(true);
        session.update(itmTmp);
        session.getTransaction().commit();
        session.close();
    }
}
