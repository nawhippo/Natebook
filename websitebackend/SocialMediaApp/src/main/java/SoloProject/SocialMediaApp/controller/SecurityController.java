package SoloProject.SocialMediaApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api") // The base path for all endpoints in this controller
public class SecurityController {

    @PostMapping("/login")
    public String handleLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        // Perform login logic here, validate credentials, etc.
        // If login is successful, redirect to the home page
        // If login fails, return an error message or redirect to the login page with an error message

        return "redirect:/api/home"; // Replace '/api/home' with the actual URL of your home page
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}