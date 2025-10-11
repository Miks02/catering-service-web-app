
package org.example.catering.cateringserviceapp.controller.admin;

import org.example.catering.cateringserviceapp.enums.DeliveryStatus;
import org.example.catering.cateringserviceapp.enums.Role;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.service.OrderService;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/dashboard")
public class DashboardController {

    private final ProductService productService;
    private final OrderService orderService;

    private final AppUserService  appUserService;

    public DashboardController(ProductService productService, OrderService orderService, AppUserService appUserService) {
        this.productService = productService;
        this.orderService = orderService;
        this.appUserService = appUserService;
    }

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Kontrolna tabla");

        var productsCount = productService.getAllProducts().size();
        var orders = orderService.getAllOrders();

        model.addAttribute("totalOrders", orders.size());
        model.addAttribute("pendingOrders", orders.stream().filter(order -> order.getStatus() == DeliveryStatus.PENDING).count());
        model.addAttribute("totalProducts", productsCount);
        model.addAttribute("totalUsers", appUserService.getAllUsers().stream().filter(appUser -> appUser.getRole() != Role.ADMIN).count());
        model.addAttribute("ordersToday", orders.stream().filter(order -> order.getOrderDate().toLocalDate().equals(LocalDate.now())).count());
        model.addAttribute("registeredToday", appUserService.getAllUsers().stream().filter(appUser -> appUser.getCreatedAt().toLocalDate().equals(LocalDate.now())).count());

        return "pages/admin/dashboard/dashboard";
    }
}