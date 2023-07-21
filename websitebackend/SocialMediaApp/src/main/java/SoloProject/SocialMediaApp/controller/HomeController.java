package SoloProject.SocialMediaApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HomeController {
    @GetMapping("/home")
    String home(){
        return "hello user, welcome to my react app, it is a basic CRUD application.";
    }

    @GetMapping("/about")
    String about() {return "This project's purpose was to solidify my learning of react and spring boot functionality.";}
}
