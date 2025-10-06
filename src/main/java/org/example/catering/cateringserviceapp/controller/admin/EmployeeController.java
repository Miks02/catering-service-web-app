package org.example.catering.cateringserviceapp.controller.admin;

import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.viewmodels.EmployeeViewModel;
import org.example.catering.cateringserviceapp.viewmodels.ProductViewModel;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

            var product = userService.getUserById(id);

            if(product.isEmpty()) {
                AppLogger.error("Ne postoji zaposleni sa ID: " + id);
                AppLogger.info("Preusmeravanje korisnika na formu za dodavanje zaposlenog...");
                return "pages/admin/employees/form";
            }

            vm.setFirstName(product.get().getFirstName());
            vm.setLastName(product.get().getLastName());
            vm.setEmail(product.get().getEmail());
            vm.setPassword(product.get().getPassword());
            vm.setAddress(product.get().getAddress());
            vm.setPhone(product.get().getPhone());
            vm.setId(product.get().getId());
            vm.setImagePath(product.get().getImagePath());

        }


        model.addAttribute("vm", vm);
        return "/pages/admin/employees/form";
    }

}
