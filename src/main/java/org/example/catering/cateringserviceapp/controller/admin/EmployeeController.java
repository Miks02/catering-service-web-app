package org.example.catering.cateringserviceapp.controller.admin;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.viewmodels.EmployeeViewModel;
import org.example.catering.cateringserviceapp.viewmodels.ProductViewModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Secured("ROLE_ADMIN")
@RequestMapping("/admin/employees")
public class EmployeeController {

    private final AppUserService userService;

    public EmployeeController(AppUserService userService) {
        this.userService = userService;
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

        return "/pages/admin/employees/form";

    }

}
