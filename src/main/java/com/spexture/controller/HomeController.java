package com.spexture.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home"; // Return the name of the home view (e.g., home.html)
    }
    
    @GetMapping("/home")
    public String homePage() {
        return "home"; // Return the name of the home view (e.g., home.html)
    }
}