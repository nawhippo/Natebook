package SoloProject.SocialMediaApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // The base path for all endpoints in this controller
public class HomeController {
    @GetMapping("/home") // The endpoint URL: /api/home
    public String home() {
        return "{\"message\": \"hello user, welcome to my react app, it is a basic CRUD application.\"}";
    }

    @GetMapping("/about") // The endpoint URL: /api/about
    public String about() {
        return "{\"message\": \"Facebook clone DUDE!.\"}";
    }
}