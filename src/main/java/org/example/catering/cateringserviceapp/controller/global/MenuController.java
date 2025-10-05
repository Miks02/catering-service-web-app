package org.example.catering.cateringserviceapp.controller.global;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/public")
public class MenuController {

    @GetMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("heroTitle", "Meni");
        model.addAttribute("heroSubtitle", "Širok spektar ponude za sve prilike");
        return "pages/public/menu";
    }

}
