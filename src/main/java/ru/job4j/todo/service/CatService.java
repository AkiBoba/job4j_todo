package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CatStore;

import java.util.Collection;

@Service
@ThreadSafe
public class CatService {

    private final CatStore store;

    public CatService(CatStore store) {
        this.store = store;
    }

    public Collection<Category> findAll() {
        return store.findAll();
    }

    public Category findById(int id) {
        return store.findById(id);
    }
}
