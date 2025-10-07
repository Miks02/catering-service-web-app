package org.example.catering.cateringserviceapp.controller.user;

import org.example.catering.cateringserviceapp.dto.CartItemDTO;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.models.Cart;
import org.example.catering.cateringserviceapp.models.CartItem;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.service.CartService;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Secured("ROLE_CLIENT")
@RequestMapping("/user")
public class CartController {

    private final CartService cartService;
    private final AppUserService appUserService;
    private final ProductService productService;

    public CartController(CartService cartService,  AppUserService appUserService, ProductService productService) {
        this.cartService = cartService;
        this.appUserService = appUserService;
        this.productService = productService;
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

        double productsPrice = 0;
        double finalPrice = 0;
        double delivery = 0;

        if(!cart.getItems().isEmpty()) {
            for(CartItem cartItem : cart.getItems()) {
                for(int j = 0; j < cartItem.getQuantity(); j++) {
                    productsPrice = cartItem.getPrice() + productsPrice;
                }
            }
            delivery = 500;
            finalPrice = productsPrice + delivery;
        }
        model.addAttribute("finalPrice", finalPrice);
        model.addAttribute("delivery", delivery);
        model.addAttribute("cart", cart);
        return "pages/user/cart";
    }

    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        try {
            Long userId = getUserId();

            CartItem newItem = new CartItem();

            Product product = productService.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Proizvod ne postoji"));

            newItem.setProduct(product);
            newItem.setQuantity(cartItemDTO.getQuantity());
            newItem.setPrice(cartItemDTO.getPrice());

            cartService.addToCart(userId, newItem);

            return ResponseEntity.ok("Proizvod je dodat u korpu: " + cartItemDTO.getQuantity());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Greška prilikom dodavanja u korpu: " + e.getMessage());
        }
    }



}
