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

}
