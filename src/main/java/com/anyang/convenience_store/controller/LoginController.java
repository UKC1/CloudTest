package com.anyang.convenience_store.controller;

import com.anyang.convenience_store.model.entity.Customer;
import com.anyang.convenience_store.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;
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
        customerService.logout(session);
        return "redirect:/login";
    }

    @PostMapping("/signup")
    public String signupProcess(@RequestParam String name, @RequestParam String id, @RequestParam String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setLoginId(id);
        customer.setPasswordHash(password);

        customerRepository.save(customer);
        return "index"; // 회원가입 후 index 페이지로 리다이렉션
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String id, @RequestParam String password, HttpSession session, Model model) {
        Optional<Customer> customer = customerRepository.findByLoginIdAndPasswordHash(id, password);

        if (customer.isPresent()) {
            // 인증 성공 시
            session.setAttribute("customer", customer.get());
            return "redirect:/search"; // 로그인 성공 시 search로 이동
        } else {
            model.addAttribute("error", "로그인 실패!"); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 다시 로그인 페이지로 이동
        }
    }
}
