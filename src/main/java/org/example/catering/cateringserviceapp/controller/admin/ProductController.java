package org.example.catering.cateringserviceapp.controller.admin;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.enums.EventType;
import org.example.catering.cateringserviceapp.enums.ProductType;
import org.example.catering.cateringserviceapp.models.Product;
import org.example.catering.cateringserviceapp.service.ProductService;
import org.example.catering.cateringserviceapp.viewmodels.ProductViewModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/form")
    public String getProductForm() {
        return "pages/admin/products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute ProductViewModel vm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if(vm.getImageFile().isEmpty()) {
            bindingResult.addError(new FieldError("vm", "imageFile", "Slika je obavezna!."));
        }

        if(bindingResult.hasErrors()) {
            return "pages/admin/products/form";
        }

        var product = new Product();

        product.setId(vm.getId());
        product.setName(vm.getName());
        product.setPrice(vm.getPrice());
        product.setQuantity(vm.getQuantity());
        product.setDescription(vm.getDescription());
        product.setActive(vm.isActive());
        product.setProductType(ProductType.valueOf(vm.getProductType()));
        product.setEventType(EventType.valueOf(vm.getEventType()));

        MultipartFile  imageFile = vm.getImageFile();

        try {
            productService.save(product,  imageFile);
            redirectAttributes.addFlashAttribute("success", "Proizvod je uspešno sačuvan.");
            return "redirect:/admin/products/form";
        }
        catch(IOException e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Greška prilikom čuvanja slike proizvoda");
            return "redirect:/admin/products/form";
        }
        catch (IllegalArgumentException e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/products/form";
        }
        catch (Exception e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Došlo je do greške prilikom dodavanja proizvoda, pokušajte ponovo kasnije...");
            return "redirect:/admin/products/form";
        }

    }
}
