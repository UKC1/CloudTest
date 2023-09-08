package com.example.convenience_store.controller;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.model.entity.Product;
import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.service.CustomerService;
import com.example.convenience_store.service.ProductService;
import com.example.convenience_store.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/reserve") // 이 컨트롤러의 URL 경로를 /reservation으로 설정
public class ReservationController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/confirm")
    public String confirmReservation(@RequestParam Integer productId, @RequestParam Integer quantity, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            return "redirect:/login";
        }

        // 상품 정보 가져오기
        Product product = productService.read(productId);

        // 예약 생성
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setProduct(product);
        reservation.setStore(product.getStore());
        reservation.setQuantity(quantity);
        reservation.setPrice(product.getPrice() * quantity); // 가격 계산

        // DB에 예약 정보 추가
        reservationService.create(reservation);

        model.addAttribute("reservation", reservation);
        return "reservation/confirm";
    }
}
