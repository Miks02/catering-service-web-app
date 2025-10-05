package org.example.catering.cateringserviceapp.helpers;

import jakarta.annotation.PostConstruct;
import org.example.catering.cateringserviceapp.enums.Role;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.service.AppUserService;
import org.springframework.stereotype.Component;

@Component
public class Seeder {

    private final AppUserService userService;

    public Seeder(AppUserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init(){
        var admin = new AppUser();

        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.setFirstName("Milan");
        admin.setLastName("Nikolić");
        admin.setEmail("milan.miki.nikolic2002@gmail.com");
        admin.setRole(Role.ADMIN);

        var manager = new AppUser();

        manager.setUsername("manager");
        manager.setPassword("123456");
        manager.setFirstName("Andrija");
        manager.setLastName("Pantović");
        manager.setEmail("andrija25922@gmail.com");
        manager.setRole(Role.MANAGER);

        userService.seedUser(admin);
        userService.seedUser(manager);

    }

}
