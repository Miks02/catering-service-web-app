package org.example.catering.cateringserviceapp.controller.admin;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.enums.Role;
import org.example.catering.cateringserviceapp.exceptions.UserAlreadyExistsException;
import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.viewmodels.EmployeeViewModel;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/employees")
public class EmployeeController {

    private final AppUserService userService;

    public EmployeeController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listEmployees(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int pageSize,
                                Model model) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<AppUser> userPage = userService.getAllUsers(pageable);
        List<AppUser> users = userPage.getContent()
                .stream()
                .sorted(Comparator.comparing(AppUser::getRole).reversed())
                .filter(appUser -> !appUser.getRole().equals(Role.ADMIN))
                .toList();

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("pageSize", pageSize);

        return "pages/admin/employees/list";

    }

    @GetMapping("/form")
    public String getEmployeeForm(Model model, @RequestParam(required = false) Long id) {
        var vm = new EmployeeViewModel();

        if(id != null) {
            AppLogger.info("Izvlačenje zaposlenog sa ID: " + id);

            var employee = userService.getUserById(id);

            if(employee.isEmpty()) {
                AppLogger.error("Ne postoji zaposleni sa ID: " + id);
                AppLogger.info("Preusmeravanje korisnika na formu za dodavanje zaposlenog...");
                return "pages/admin/employees/form";
            }

            vm.setFirstName(employee.get().getFirstName());
            vm.setLastName(employee.get().getLastName());
            vm.setUsername(employee.get().getUsername());
            vm.setEmail(employee.get().getEmail());
            vm.setPassword(employee.get().getPassword());
            vm.setAddress(employee.get().getAddress());
            vm.setPhone(employee.get().getPhone());
            vm.setId(employee.get().getId());
            vm.setImagePath(employee.get().getImagePath());

        }


        model.addAttribute("vm", vm);
        return "/pages/admin/employees/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("vm") EmployeeViewModel vm,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        MultipartFile imageFile = vm.getImageFile();

        if (vm.getId() == null && (imageFile == null || imageFile.isEmpty())) {
            bindingResult.addError(new FieldError("vm", "imageFile", "Slika je obavezna!"));
        }

        if(!vm.getPassword().equals(vm.getConfirmPassword())) {
            bindingResult.addError(new FieldError("vm", "confirmPassword", "Lozinke se ne poklapaju"));
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("vm", vm);
            return "/pages/admin/employees/form";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            vm.setImagePath(null);
        }


        var employee = new AppUser();
        employee.setFirstName(vm.getFirstName());
        employee.setLastName(vm.getLastName());
        employee.setUsername(vm.getUsername());
        employee.setEmail(vm.getEmail());
        employee.setPassword(vm.getPassword());
        employee.setAddress(vm.getAddress());
        employee.setPhone(vm.getPhone());
        employee.setId(vm.getId());

        AppLogger.info("Korisnik se šalje u servis: " + employee.getUsername());


        try {
            userService.registerEmployee(employee, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Zaposleni je uspešno sačuvan.");
        }
        catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch(UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Došlo je do greške prilikom dodavanja zaposlenog, pokušajte ponovo kasnije...");

        }

        return "redirect:/admin/employees/list";

    }

    @GetMapping("/list/delete/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Korisnik je uspešno obrisan!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Greška pri brisanju korisnika: " + e.getMessage());
        }

        return "redirect:/admin/employees/list";
    }

}
