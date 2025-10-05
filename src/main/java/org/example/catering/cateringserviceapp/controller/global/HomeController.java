package org.example.catering.cateringserviceapp.controller.global;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToPublic() {
        return "redirect:/public";
    }
    @RequestMapping("/public")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Početna");
        model.addAttribute("heroTitle", "Dobrodošli u Ketering Službu");
        model.addAttribute("heroSubtitle", "Najbolja hrana za sve prilike");
        model.addAttribute("showHeroButtons", true);
        return "pages/public/index";
    }

    @GetMapping("/public/about")
    public String about() {
        return "pages/public/about";
    }

}
