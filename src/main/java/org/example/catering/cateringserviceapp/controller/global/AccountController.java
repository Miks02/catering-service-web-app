package org.example.catering.cateringserviceapp.controller.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class AccountController {

    @GetMapping("/login")
    public String login() {
        return "pages/public/login";
    }

}
