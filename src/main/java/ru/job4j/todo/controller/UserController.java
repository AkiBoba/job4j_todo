package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.Role;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ThreadSafe
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        Role role = userService.addRole(Role.of("ADMIN"));
        model.addAttribute("user", User.of(
                "Заполните поле",
                "Заполните поле",
                "Заполните поле",
                role
                ));
        return "addUser";
    }

    @PostMapping("/registration")
    public String registration(HttpServletRequest req, Model model) {
        Role role = userService.addRole(Role.of("ADMIN"));
        String name = req.getParameter("name");
        String mail = req.getParameter("mail");
        String password = req.getParameter("password");
        Optional<User> regUser = userService.add(User.of(
                name,
                mail,
                password,
                role
            )
        );

        if (regUser.isEmpty()) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            model.addAttribute("mark", "mark");
            return "/registration";
        }
        model.addAttribute("message", "Пользователь успешно зарегистрирован");
        return "/registration";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findUserByEmailAndPwd(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "items";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
