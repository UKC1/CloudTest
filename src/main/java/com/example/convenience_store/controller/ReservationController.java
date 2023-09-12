package com.example.convenience_store.controller;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.model.entity.Product;
import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.service.ProductService;
import com.example.convenience_store.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ProductService productService;

    @GetMapping("/reserve")
    public String reservationPage(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if(customer == null){
            return "redirect:/login";
        }

        return "reserve";
    }

    @GetMapping("/reserve/{id}")
    public String reserveForm(@PathVariable Integer id, Model model, HttpSession session) {
        Product productResponse = productService.read(id);
        session.setAttribute("product", productResponse);

        model.addAttribute("productinfo", productResponse);
        return "reserve";
    }

    @GetMapping("/mypage")
    public String showReservations(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if(customer == null){
            return "redirect:/login";
        }

        List<Reservation> reservations = reservationService.getAllReservations(session);
        model.addAttribute("reservations", reservations);

        return "mypage";
    }


    @PostMapping("/delete/{id}")
    public String RollbackReservation(@PathVariable Integer id, HttpSession session) {
        Optional<Reservation> reservationOptional = reservationService.getReservationWithProduct(id);

        if (reservationOptional.isPresent()) {
            Reservation reservation = reservationOptional.get();
            Product product = reservation.getProduct();

            // 이제 예약 정보와 상품 정보를 사용할 수 있습니다.
//            System.out.println("Reservation ID: " + reservation.getNum());
//            System.out.println("Reservation Quantity: " + reservation.getQuantity());
//            System.out.println("Product ID: " + product.getProductId());
//            System.out.println("Product Name: " + product.getName());

            // 여기서 추가 작업 수행 가능
            productService.rollBack(product, reservation.getQuantity());
            // 예약 삭제
            reservationService.delete(id);
        }
        return "redirect:/mypage";
    }
}
