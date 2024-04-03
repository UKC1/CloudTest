package com.anyang.convenience_store.controller;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/signup")
    public String signupProcess(@RequestParam String name, @RequestParam String id, @RequestParam String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setLoginId(id);
        customer.setPasswordHash(password);

        customerService.save(customer);
        return "redirect:/index";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String id, @RequestParam String password, HttpSession session, Model model) {
        Optional<Customer> customer = customerService.authenticate(id, password);

        if (customer.isPresent()) {
            session.setAttribute("customer", customer.get());
            return "redirect:/search";
        } else {
            model.addAttribute("error", "로그인 실패!");
            return "login";
        }
    }
}
