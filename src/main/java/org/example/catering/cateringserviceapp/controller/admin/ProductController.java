package org.example.catering.cateringserviceapp.controller.admin;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.enums.EventType;
import org.example.catering.cateringserviceapp.enums.ProductType;
import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.example.catering.cateringserviceapp.viewmodels.ProductViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String listProducts(@RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               Model model) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Product> productPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        return "pages/admin/products/list";
    }

    @GetMapping("/form")
    public String getProductForm(Model model, @RequestParam(required = false) Long id) {
        var vm = new ProductViewModel();

        if(id != null) {
            AppLogger.info("Izvlačenje proizvoda sa ID: " + id);

            var product = productService.findById(id);

            if(product.isEmpty()) {
                AppLogger.error("Ne postoji proizvoda sa ID: " + id);
                AppLogger.info("Preusmeravanje korisnika na formu za dodavanje proizvoda...");
                return "pages/admin/products/form";
            }

            vm.setName(product.get().getName());
            vm.setPrice(product.get().getPrice());
            vm.setQuantity(product.get().getQuantity());
            vm.setServings(product.get().getServings());
            vm.setActive(product.get().isActive());
            vm.setId(product.get().getId());
            vm.setImagePath(product.get().getImagePath());
            vm.setDescription(product.get().getDescription());
            vm.setProductType(product.get().getProductType().getDisplayName());
            vm.setEventType(product.get().getEventType().getDisplayName());

        }

        model.addAttribute("vm", vm);

        return "pages/admin/products/form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("vm") ProductViewModel vm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        MultipartFile imageFile = vm.getImageFile();

        if (vm.getId() == null && (imageFile == null || imageFile.isEmpty())) {
            bindingResult.addError(new FieldError("vm", "imageFile", "Slika je obavezna!"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("vm", vm);
            return "pages/admin/products/form";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            vm.setImagePath(null);
        }

        Product product = new Product();
        product.setId(vm.getId());
        product.setName(vm.getName());
        product.setPrice(vm.getPrice());
        product.setQuantity(vm.getQuantity());
        product.setServings(vm.getServings());
        product.setDescription(vm.getDescription());
        product.setActive(vm.isActive());
        product.setProductType(ProductType.valueOf(vm.getProductType()));
        product.setEventType(EventType.valueOf(vm.getEventType()));

        AppLogger.info("Proizvod se šalje u servis: " + product.getId());

        try {
            productService.save(product, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Proizvod je uspešno sačuvan.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Greška prilikom čuvanja slike proizvoda.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Došlo je do greške prilikom dodavanja proizvoda, pokušajte ponovo kasnije...");
        }

        return "redirect:/admin/products/list";
    }


    @GetMapping("/list/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Proizvod je uspešno obrisan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Greška pri brisanju proizvoda: " + e.getMessage());
        }

        return "redirect:/admin/products/list";
    }
}
