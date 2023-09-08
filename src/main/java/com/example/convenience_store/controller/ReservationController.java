//package com.example.convenience_store.controller;
//
//import com.example.convenience_store.model.entity.Product;
//import com.example.convenience_store.repository.ReservationRepository;
//import com.example.convenience_store.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//
//@Controller
//public class ReservationController {
//    @Autowired
//    private ReservationRepository reservationRepository;
//    @Autowired
//    private ProductService productService;
//    @GetMapping("/reserve")
//    public String reservationPage() {
//        return "reserve";
//    }
//
//    @GetMapping("/confirm")
//    public String confirmPage() {
//
//        return "confirm";
//    }
//    @GetMapping("/reserve/{id}")
//    public String reservationForm(@PathVariable Integer id, Model model) {
//        Product productResponse = productService.read(id);
//
//        Product productRequest = Product.builder()
//                .store(productResponse.getStore())
//                .name(productResponse.getName())
//                .quantity(productResponse.getQuantity())
//                .price(productResponse.getPrice())
//                .build();
//
//
//        model.addAttribute("productinfo", productRequest);
//        return "reserve";
//    }
//}