package org.example.catering.cateringserviceapp.controller.global;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class ContactController {

    @GetMapping("/contact")
    public String showContactPage(Model model)
    {
        model.addAttribute("heroTitle", "Kontakt");
        model.addAttribute("heroSubtitle", "Stojimo vam na raspolaganju za sva vaša pitanja");
        return "pages/public/contact";
    }

}
