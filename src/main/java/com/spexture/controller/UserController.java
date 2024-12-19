package com.spexture.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.spexture.model.User;
import com.spexture.service.UserService;

import org.springframework.ui.Model;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RedirectView register(
        @RequestParam @Email(message = "Invalid email format") String email,
        @RequestParam @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return new RedirectView("/register?error=Passwords do not match");
        }
        try {
            userService.registerUser(email, password);
            return new RedirectView("/login?success=Registration successful, please login");
        } catch (RuntimeException e) {
            return new RedirectView("/register?error=" + e.getMessage());
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null 
            && auth.isAuthenticated() 
            && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String email = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();
            User user = userService.findUserByEmail(email);
            model.addAttribute("user", user);
            return "profile";
        }
        return "redirect:/login";
    }
}
