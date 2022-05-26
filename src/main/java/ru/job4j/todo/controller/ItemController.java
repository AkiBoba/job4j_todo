package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CatService;
import ru.job4j.todo.service.ItemDbService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@ThreadSafe
public class ItemController {

    private final ItemDbService itemDbService;
    private final CatService catService;

    public ItemController(ItemDbService itemDbService, CatService catService) {
        this.itemDbService = itemDbService;
        this.catService = catService;
    }

    @GetMapping("/items")
    public String candidates(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("items", itemDbService.getItems());
        return "items";
    }

    @GetMapping("/done")
    public String done(Model model) {
       model.addAttribute("items", itemDbService.findAlldone());
        return "done";
    }

    @PostMapping("/updateItem")
    public String updatePost(@ModelAttribute Item item) {
        itemDbService.update(item);
        return "redirect:/items";
    }

    @PostMapping("/addItem")
    public String addPost(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("item", new Item(0, "Заполните поле", true, user));
        model.addAttribute("cats", catService.findAll());
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(HttpServletRequest req, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String description = req.getParameter("description");
        boolean done = Boolean.parseBoolean(req.getParameter("done"));
        String[] cats = req.getParameterValues("cat");
        Item item = new Item(1, description, done, user);
        Arrays.stream(cats).forEach(cat -> item
                .getCategoryes()
                .add(catService
                        .findById(Integer.parseInt(cat))
                )
            );
        itemDbService.add(item);
        return "redirect:/items";
    }

    @GetMapping("/formUpdateItem/{itemId}")
    public String formUpdateItem(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemDbService.findById(id));
        return "updateItem";
    }

    @GetMapping("/doneItem/{itemId}")
    public String doneItem(@PathVariable("itemId") int id) {
        itemDbService.done(id);
        return "redirect:/items";
    }

    @GetMapping("/newItems")
    public String newItems(Model model) {
        model.addAttribute("items", itemDbService.newItems());
        return "newItems";
    }

    @GetMapping("/item/{itemId}")
    public String item(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemDbService.findById(id));
        return "item";
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(@PathVariable("itemId") int id) {
        itemDbService.delete(id);
        return "redirect:/items";
    }
}
