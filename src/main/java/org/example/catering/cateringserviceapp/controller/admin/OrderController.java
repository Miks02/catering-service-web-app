package org.example.catering.cateringserviceapp.controller.admin;

import org.example.catering.cateringserviceapp.enums.DeliveryStatus;
import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.models.Order;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.service.OrderService;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.hibernate.annotations.NotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.stream.Collectors;

@Controller("adminOrderController")
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {

        this.orderService = orderService;
    }

    @GetMapping({"/list", ""})
    public String listOrders(@RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int pageSize,
                             Model model) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Order> orderPage = orderService.getAllOrders(pageable);

        model.addAttribute("orders", orderPage.getContent().stream().sorted(Comparator.comparing(Order::getOrderDate)).toList().reversed());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("pageSize", pageSize);


        return "/pages/admin/orders/list";

    }

    @GetMapping("/delete")
    public String deleteAllOrders() {
        orderService.deleteAllOrders();

        return  "redirect:/admin/orders";
    }

    @GetMapping("/confirm")
    public String confirmOrder(Long id) {

        try {
            orderService.updateStatus(DeliveryStatus.DELIVERED, id);
        }
        catch(Exception e) {
            AppLogger.error(e.getMessage());
            return "redirect:/admin/orders";
        }

        return "redirect:/admin/orders";

    }


}
