package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class UserService {

    private final UserStore store;

    public UserService(UserStore store) {
        this.store = store;

    }

    public Collection<User> findAll() {
        return store.findAll();

    }

    public Optional<User> add(User user) {
        return store.add(user);

    }

    public Role addRole(Role role) {
        return store.addRole(role);

    }

    public User findById(int id) {
        return store.findById(id);
    }

    public void update(User user) {
        store.update(user);
    }

    public void delete(int id) {
        store.delete(id);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return store.findUserByEmailAndPwd(email, password);
    }
}

