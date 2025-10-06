package org.example.catering.cateringserviceapp.controller.admin;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.enums.EventType;
import org.example.catering.cateringserviceapp.enums.ProductType;
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
    public String getProductForm(Model model) {

        model.addAttribute("vm", new ProductViewModel());

        return "pages/admin/products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("vm") ProductViewModel vm,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if(vm.getImageFile().isEmpty()) {

            bindingResult.addError(new FieldError("vm", "imageFile", "Slika je obavezna!."));
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("vm", vm);
            return "pages/admin/products/form";
        }

        var product = new Product();

        product.setId(vm.getId());
        product.setName(vm.getName());
        product.setPrice(vm.getPrice());
        product.setQuantity(vm.getQuantity());
        product.setServings(vm.getServings());
        product.setDescription(vm.getDescription());
        product.setActive(vm.isActive());
        product.setProductType(ProductType.valueOf(vm.getProductType()));
        product.setEventType(EventType.valueOf(vm.getEventType()));

        MultipartFile  imageFile = vm.getImageFile();

        try {
            productService.save(product,  imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Proizvod je uspešno sačuvan.");
            return "redirect:/admin/products/form";
        }
        catch(IOException e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Greška prilikom čuvanja slike proizvoda");
            return "redirect:/admin/products/form";
        }
        catch (IllegalArgumentException e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/products/form";
        }
        catch (Exception e) {
            System.out.println("GREŠKA! " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Došlo je do greške prilikom dodavanja proizvoda, pokušajte ponovo kasnije...");
            return "redirect:/admin/products/form";
        }

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
