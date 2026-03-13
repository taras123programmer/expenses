package com.expenses.controllers;

import com.expenses.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }

        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(String username, String password, String confirm_password, Model model) {
        if(!Objects.equals(password, confirm_password)){
            model.addAttribute("error", "Паролі не співпадають!");
            return "redirect:/register";
        }
        if(authService.register(username, password)){
            return "redirect:/";
        }
        else{
            model.addAttribute("error", "Користувач із таким логіном вже існує!");
            return "redirect:/register";
        }
    }

}
