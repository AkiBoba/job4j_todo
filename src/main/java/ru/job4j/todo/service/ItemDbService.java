package ru.job4j.todo.service;


import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.ItemHbItem;

import java.util.Collection;
import java.util.List;

@Service
@ThreadSafe
public class ItemDbService {

    private final ItemHbItem store;

    public ItemDbService(ItemHbItem store) {
        this.store = store;

    }

    public Collection<Item> findAll() {
        return store.findAll();

    }

    public Item add(Item item) {
        return store.add(item);

    }

    public Item findById(int id) {
        return store.findById(id);
    }

    public void update(Item item) {
        store.update(item);
    }

    public List<Item> newItems() {
        return store.newItems();
    }

    public void done(int id) {
        store.done(id);
    }

    public List<Item> findAlldone() {
        return store.findAlldone();
    }

    public void delete(int id) {
        store.delete(id);
    }
}
