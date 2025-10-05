
package org.example.catering.cateringserviceapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class DashboardController {

    @GetMapping({"", "/"})
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Kontrolna tabla");

        model.addAttribute("totalOrders", 245);
        model.addAttribute("pendingOrders", 12);
        model.addAttribute("totalProducts", 87);
        model.addAttribute("totalUsers", 1234);

        return "pages/admin/dashboard/dashboard";
    }
}