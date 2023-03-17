package com.example.garage_f.controller;


import com.example.garage_f.model.Owner;
import com.example.garage_f.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("owners", ownerService.findAll());
        return "owners/list";
    }

    @GetMapping("/registration")
    public String addNewForm(Model model) {
        model.addAttribute("owner", Owner.builder().build());
        return "owners/new";
    }

    @PostMapping
    public String saveOwner(@ModelAttribute("owner") Owner owner) {
        ownerService.save(owner);
        return "redirect:/owners";
    }

    @GetMapping("/{id}/updating")
    public String updateForm(@PathVariable(value = "id") int id, Model model) {
        model.addAttribute("owner", ownerService.findById(id));
        return "owners/update";
    }

    @DeleteMapping("/{id}")
    public String deleteOwner(@PathVariable(value = "id") int id) {
        ownerService.deleteById(id);
        return "redirect:/owners";
    }
}
