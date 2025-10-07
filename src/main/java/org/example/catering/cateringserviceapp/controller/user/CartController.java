package org.example.catering.cateringserviceapp.controller.user;

import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.models.Cart;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.service.CartService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured("ROLE_CLIENT")
@RequestMapping("/user")
public class CartController {

    private final CartService cartService;
    private final AppUserService appUserService;

    public CartController(CartService cartService,  AppUserService appUserService) {
        this.cartService = cartService;
        this.appUserService = appUserService;
    }

    private Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = appUserService.getUserByUsername(authentication.getName());

        if(user.isEmpty()) throw new RuntimeException("Korisnik nije pronadjen");

        return user.get().getId();

    }

    @GetMapping("/cart")
    public String cart(Model model) {
        Long userId = getUserId();
        Cart cart = cartService.getCart(userId);
        System.out.println("Klijent ID: " + userId);
        model.addAttribute("cart", cart);
        return "pages/user/cart";
    }



}
