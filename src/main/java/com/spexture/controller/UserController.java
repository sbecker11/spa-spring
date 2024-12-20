package com.spexture.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.spexture.model.User;
import com.spexture.service.UserService;

import org.springframework.ui.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RedirectView register(
        @RequestParam @Email(message = "Invalid email format") String email,
        @RequestParam @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return new RedirectView("/register");
        }
        try {
            userService.registerUser(email, password);
            RedirectView redirectView = new RedirectView("/login");
            redirectAttributes.addFlashAttribute("success", "Registration successful, please login");
            return redirectView;
        } catch (IOException e) {
            return handleException(redirectAttributes, e, "/register", "An IO error occurred during registration");
        } catch (RuntimeException e) {
            return handleException(redirectAttributes, e, "/register", "Error during registration");
        }
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
                String email = ((UserDetails) auth.getPrincipal()).getUsername();
                User user = userService.findUserByEmail(email);
                model.addAttribute("user", user);
                return "profile";
            } else {
                return "redirect:/login";
            }
        } catch (IOException e) {
            logger.error("IO Error fetching user profile", e);
            model.addAttribute("error", "An error occurred while fetching your profile: " + e.getMessage());
            return "error"; // or handle this within the profile view
        }
    }

    private RedirectView handleException(RedirectAttributes redirectAttributes, Exception e, String redirectUrl, String errorMessagePrefix) {
        logger.error(errorMessagePrefix, e);
        redirectAttributes.addFlashAttribute("error", errorMessagePrefix + ": " + e.getMessage());
        return new RedirectView(redirectUrl);
    }
}
