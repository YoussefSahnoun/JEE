package com.mycompany.obitemservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {

    @GetMapping("/api/users/success")
    public String success() {
        return "redirect:/api/v1/items"; // Redirect to /success-page after successful login
    }
}
