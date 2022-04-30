package ru.job4j.todo.service;


import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.ItemDbStore;

import java.util.Collection;
import java.util.List;

@Service
@ThreadSafe
public class ItemDbService {

    private final ItemDbStore store;

    public ItemDbService(ItemDbStore store) {
        this.store = store;

    }

    public Collection<Item> findAll() {
        return store.findAll();

    }

    public void add(Item item) {
        store.add(item);

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

    public Item done(int id) {
        return store.done(id);
    }

    public List<Item> findAlldone() {
        return store.findAlldone();
    }
}
