package com.example.convenience_store.controller;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.model.entity.Product;
import com.example.convenience_store.repository.ProductRepository;
import com.example.convenience_store.service.CustomerService;
import com.example.convenience_store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public String showSearchPage(HttpSession session, Model model0) {
        Customer customer = (Customer) session.getAttribute("customer");
        if(customer == null){
            return "redirect:/login";
        }
        return "search";
    }

    @GetMapping("/reserve")
    public String reservationPage() {
        return "reserve";
    }

    @PostMapping("/search")
    public String searchItem(@RequestParam String name, Model model) {
        List<Product> foundProducts = productService.findProductByName(name);

        if (!foundProducts.isEmpty()) {
            model.addAttribute("foundProducts", foundProducts);
        } else {
            model.addAttribute("message", "물건을 찾을 수 없습니다.");
        }

        return "search";
    }

    @GetMapping("/reserve/{id}")
    public String reserveForm(@PathVariable Integer id, Model model) {
        Product productResponse = productService.read(id);

        Product productRequest = Product.builder()
                .store(productResponse.getStore())
                .name(productResponse.getName())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();


        model.addAttribute("productinfo", productRequest);
        return "reserve";
    }
}
