package org.locations.eventsphere.Controllers;

import DTOs.categoryDTO;
import org.locations.eventsphere.Services.categoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class categoryController {
    private final categoryService categoryService;

    public categoryController(org.locations.eventsphere.Services.categoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAll")
    public List<categoryDTO> getCategories(){
        return categoryService.getCategories();
    }
}
