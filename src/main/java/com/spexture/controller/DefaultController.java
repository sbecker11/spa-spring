package com.spexture.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);
    public DefaultController() {
        logger.info("Initializing DefaultController bean");
    }

    @GetMapping("/")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView redirectToHome() {
        logger.info("DefaultController handling GET [/]");
        return new RedirectView("/home");
    }

    @GetMapping("")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public RedirectView redirectToHomeFallback() {
        logger.info("DefaultController handling GET []");
        return new RedirectView("/home");
    }

    @GetMapping("/about")
    public String about() {
        logger.info("DefaultController handling GET [/about]");
        return "about"; // Return the name of the about view (e.g., about.html)
    }

} 

