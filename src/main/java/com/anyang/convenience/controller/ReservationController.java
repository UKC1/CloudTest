package com.anyang.convenience.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.anyang.convenience.model.entity.Customer;
import com.anyang.convenience.model.entity.Product;
import com.anyang.convenience.model.entity.Reservation;
import com.anyang.convenience.service.ProductService;
import com.anyang.convenience.service.ReservationService;

@Controller
public class ReservationController {
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private ProductService productService;

	@GetMapping("/reserve")
	public String reservationPage(HttpSession session) {
		Customer customer = (Customer)session.getAttribute("customer");
		if (customer == null) {
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
		Customer customer = (Customer)session.getAttribute("customer");
		if (customer == null) {
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
			productService.rollBack(product, reservation.getQuantity());
			reservationService.delete(id);
		}
		return "redirect:/mypage";
	}
}