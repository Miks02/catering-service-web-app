package org.example.catering.cateringserviceapp.controller.user;

import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.service.CartService;
import org.example.catering.cateringserviceapp.service.OrderService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("userOrderController")
@Secured("ROLE_CLIENT")
@RequestMapping("/user/order")
public class OrderController {

    private final OrderService orderService;
    private final AppUserService appUserService;

    public OrderController(OrderService orderService, AppUserService appUserService, CartService cartService) {
        this.orderService = orderService;
        this.appUserService = appUserService;}

    private AppUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userOpt = appUserService.getUserByUsername(authentication.getName());
        if (userOpt.isEmpty()) throw new RuntimeException("Korisnik nije pronadjen");
        return userOpt.get();
    }

    @PostMapping("/confirm")
    public String confirmOrder(@RequestParam("shippingAddress") String shippingAddress,
                               RedirectAttributes redirectAttributes) {
        try {
            Long userId = getUser().getId();
            var result = orderService.createOrder(userId, shippingAddress);

            StringBuilder message = new StringBuilder("Porudžbina je uspešno kreirana.");
            if (result.discountApplied()) {
                message.append(" Ostvarili ste 10% popust zahvaljujući skupljenim poenima!");
            }
            message.append(" Ukupno: ").append((long) result.finalPrice()).append(" RSD");

            redirectAttributes.addFlashAttribute("successMessage", message.toString());
            return "redirect:/user/cart";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/cart";
        }
    }
}
