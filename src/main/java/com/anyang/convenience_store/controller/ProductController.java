package com.anyang.convenience_store.controller;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.model.entity.Product;
import com.anyang.convenience_store.model.entity.Reservation;
import com.anyang.convenience_store.service.ProductService;
import com.anyang.convenience_store.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/search")
    public String showSearchPage(HttpSession session, Model model0) {
        Customer customer = (Customer) session.getAttribute("customer");
        if(customer == null){
            return "redirect:/login";
        }
        return "search";
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

    @PostMapping("/update")
    public String ProductUpdateForm(@ModelAttribute Product productRequest, HttpSession session) {
        Product sessionProduct = (Product) session.getAttribute("product");
        Customer sessionCustomer = (Customer) session.getAttribute("customer");

        sessionProduct.setQuantity(productRequest.getQuantity());
        Product updatedProduct = productService.update(sessionProduct);

        if(updatedProduct == null){
            return "fail";
        }

        // ReservationService에서 만들 코드
        Reservation newReservation = new Reservation();
        newReservation.setQuantity(productRequest.getQuantity());
        newReservation.setPrice(sessionProduct.getPrice());
        newReservation.setTime(Timestamp.from(Instant.now()));
        newReservation.setCustomer(sessionCustomer);
        newReservation.setProduct(updatedProduct);
        newReservation.setStore(sessionProduct.getStore());

        reservationService.save(newReservation);

        return "redirect:/mypage";
    }

}
