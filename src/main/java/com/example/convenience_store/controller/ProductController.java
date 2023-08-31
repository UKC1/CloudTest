package com.example.convenience_store.controller;

import com.example.convenience_store.model.entity.Product;
import com.example.convenience_store.repository.ProductRepository;
import com.example.convenience_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }


    @PostMapping("/search")
    public String searchItem(@RequestParam String item, Model model) {
        Product foundProduct = productService.findProductByName(item);

        if (foundProduct != null) {
            model.addAttribute("itemName", foundProduct.getName());
            model.addAttribute("itemQuantity", foundProduct.getQuantity());
        } else {
            model.addAttribute("itemName", "물건을 찾을 수 없습니다.");
            model.addAttribute("itemQuantity", "");
        }

        return "search";
    }
}
