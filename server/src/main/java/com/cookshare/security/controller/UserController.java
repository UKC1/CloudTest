package com.cookshare.security.controller;

import com.cookshare.domain.User;
import com.cookshare.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequest request) {
        User user = User.builder()
                .nickName(request.getNickname())
                .password(request.getPassword())
                .location(request.getLocation())
                .mobileNumber(request.getMobileNumber())
                .role("user")
                .build();
        User registeredUser = userService.registerUser(user);

        if (registeredUser != null) {
            return ResponseEntity.ok(registeredUser);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/Allusers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // @GetMapping("/{mobileNumber}")
    // public ResponseEntity<User> getUserByMobileName(@PathVariable String mobileNumber) {
    //     Optional<User> user = userService.findByMobileNumber(mobileNumber);
    //     return user.map(ResponseEntity::ok)
    //             .orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // @PutMapping("/update/{mobileNumber}")
    // public ResponseEntity<User> updateUser(@PathVariable String mobileNumber, @RequestBody User userDetails) {
    //     Optional<User> userData = userService.findByMobileNumber(mobileNumber);
    //     if (userData.isPresent()) {
    //         User user = userData.get();
    //         user.setNickName(userDetails.getNickName());
    //         user.setLocation(userDetails.getLocation());
    //         user.setMobileNumber(userDetails.getMobileNumber());
    //         user = userService.updateUserByMobileNumber(user);
    //         return ResponseEntity.ok(user);
    //     }
    //     return ResponseEntity.notFound().build();
    // }


    // @DeleteMapping("/delete/{mobileNumber}")
    // public ResponseEntity<?> deleteUser(@PathVariable String mobileNumber) {
    //     Optional<User> user = userService.findByMobileNumber(mobileNumber);
    //     if (user.isPresent()) {
    //         userService.deleteUserByMobileNumber(mobileNumber);
    //         return ResponseEntity.ok().build();
    //     }
    //     return ResponseEntity.notFound().build();
    // }

}
