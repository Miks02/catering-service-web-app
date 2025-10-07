package org.example.catering.cateringserviceapp.controller.global;

import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/public")
public class MenuController {

    private final ProductService productService;

    public MenuController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/menu")
    public String menu(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model) {

        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> menuPage = productService.getAllProducts(pageable);

        model.addAttribute("products", menuPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", menuPage.getTotalPages());
        model.addAttribute("totalItems", menuPage.getTotalElements());
        model.addAttribute("heroTitle", "Meni");
        model.addAttribute("heroSubtitle", "Širok spektar ponude za sve prilike");
        return "pages/public/menu";
    }

}
