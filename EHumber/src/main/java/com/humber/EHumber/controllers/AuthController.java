package com.humber.EHumber.controllers;

import com.humber.EHumber.models.MyUser;
import com.humber.EHumber.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Value("E-Humber")
    private String shopName;

    @GetMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {
        model.addAttribute("message", message);
        model.addAttribute("shopName", shopName);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        System.out.println("Logging out!");
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login?message=Logged Out";
    }

    @GetMapping("/register")
    public String register(Model model, @RequestParam (required = false) String message) {
        model.addAttribute("message", message);
        model.addAttribute("user", new MyUser());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute MyUser user) {
        int saveUserCode = userService.saveUser(user);

        if (saveUserCode == 0) {
            return "redirect:/register?message=Username Occupied!";
        }
        return "redirect:/login?message=User Registered!";
    }
}
