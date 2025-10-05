package org.example.catering.cateringserviceapp.controller.global;

import jakarta.validation.Valid;
import org.example.catering.cateringserviceapp.exceptions.UserAlreadyExistsException;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.example.catering.cateringserviceapp.viewmodels.RegisterViewModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;

@Controller
@RequestMapping("/public")
public class AccountController {

    private final AppUserService userService;

    public AccountController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "pages/public/login";
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("registerVm", new RegisterViewModel());

        return "pages/public/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerVm") RegisterViewModel registerVm,
                           BindingResult result,
                           Model model, RedirectAttributes redirectAttributes) {

        if(result.hasErrors()) {
            model.addAttribute("error", true);
            return "pages/public/register";
        }

        if(!registerVm.getPassword().equals(registerVm.getConfirmPassword())) {
            result.addError(new FieldError("registerVm", "confirmPassword", "Lozinke se ne poklapaju"));
            return "pages/public/register";
        }

        try {
            var user = new AppUser();

            user.setUsername(registerVm.getUsername());
            user.setPassword(registerVm.getPassword());
            user.setEmail(registerVm.getEmail());
            user.setFirstName(registerVm.getFirstName());
            user.setLastName(registerVm.getLastName());
            user.setCreatedAt(new Date(System.currentTimeMillis()));

            userService.registerUser(user);

            redirectAttributes.addFlashAttribute("successMessage", "Uspešna registracija!");
            return "redirect:/public/register";
        }
        catch(UserAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/public/register";
        }
        catch(Exception e) {
            model.addAttribute("error", true);
            return "pages/public/register";
        }






    }

}
