package com.example.convenience_store.controller;

import com.example.convenience_store.model.entity.Customer;
import com.example.convenience_store.model.entity.Reservation;
import com.example.convenience_store.service.ProductService;
import com.example.convenience_store.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @GetMapping("/mypage")
    public String showReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);

        return "mypage";
    }

    @GetMapping ("/delete/{id}")
    public String deleteStudent(@PathVariable Integer id) {
        reservationService.delete(id);
        System.out.println("삭제완료");
        return "redirect:/mypage";
    }



}
