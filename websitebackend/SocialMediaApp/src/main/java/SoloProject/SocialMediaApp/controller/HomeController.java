package SoloProject.SocialMediaApp.controller;

import SoloProject.SocialMediaApp.models.AppUser;
import SoloProject.SocialMediaApp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api") // The base path for all endpoints in this controller
public class HomeController {

    private final AppUserService userserviceimpl;

    @Autowired
    public HomeController(AppUserService userserviceimpl) {
        this.userserviceimpl = userserviceimpl;
    }

    @GetMapping("/home")
    public ResponseEntity<List<AppUser>> getHomePageData() {
        return userserviceimpl.getAllUsers();
    }

    @GetMapping("/about") // The endpoint URL: /api/about
    public String about() {
        return "{\"message\": \"Welcome to my about page, it is a react/java application. It is a simple CRUD application, " +
                "where users are able to post, comment, friend, and message each other.\"}";
    }
}