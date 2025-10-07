package org.example.catering.cateringserviceapp.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class CartController {

    @GetMapping("/cart")
    public String cart() {
        return "/pages/user/cart";
    }

}
