package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemDbService;
import ru.job4j.todo.store.ItemHbItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@ThreadSafe
public class ItemController {

    private final ItemDbService itemDbService;

    public ItemController(ItemDbService itemDbService) {
        this.itemDbService = itemDbService;
    }

    @GetMapping("/items")
    public String index(Model model) {
        model.addAttribute("items", itemDbService.findAll());
        return "items";
    }

    @GetMapping("/done")
    public String done(Model model) {
       /* model.addAttribute("items", itemDbService.findAlldone());*/
        return "done";
    }

    @PostMapping("/updateItem")
    public String updatePost(@ModelAttribute Item item) {
        itemDbService.update(item);
        return "redirect:/items";
    }

    @PostMapping("/addItem")
    public String addPost(Model model) {
        model.addAttribute("item", new Item(0, "Заполните поле", true));
        return "addItem";
    }

    @PostMapping("/createItem")
    public String createItem(HttpServletRequest req) {
        String description = req.getParameter("description");
        Boolean done = Boolean.valueOf(req.getParameter("done"));
        itemDbService.add(new Item(1, description, done));
        return "redirect:/items";
    }

    @GetMapping("/formUpdateItem/{itemId}")
    public String formUpdateItem(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemDbService.findById(id));
        return "updateItem";
    }

    @GetMapping("/doneItem/{itemId}")
    public String doneItem(Model model, @PathVariable("itemId") int id) {
       /* model.addAttribute("item", itemDbService.done(id));*/
        return "redirect:/items";
    }

    @GetMapping("/newItems")
    public String newItems(Model model) {
       /* model.addAttribute("items", itemDbService.newItems());*/
        return "newItems";
    }

    @GetMapping("/item/{itemId}")
    public String item(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemDbService.findById(id));
        return "item";
    }
}
