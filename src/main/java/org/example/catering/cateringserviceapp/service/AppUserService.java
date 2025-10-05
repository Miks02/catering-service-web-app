package org.example.catering.cateringserviceapp.service;

import org.example.catering.cateringserviceapp.enums.Role;
import org.example.catering.cateringserviceapp.exceptions.UserAlreadyExistsException;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepo.findUserByUsername(username);

        if(user.isEmpty()) {
            throw new UsernameNotFoundException("Korisnik nije pronadjen: " + username);
        }

        return User.withUsername(user.get().getUsername())
                .password(user.get().getPassword())
                .roles(user.get().getRole().name())
                .build();


    }

    public void registerUser(AppUser user) {

        if(userRepo.findUserByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Navedeno korisničko ime je zauzeto");

        if(userRepo.findUserByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("Navedena email adresa je zauzeta");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.CLIENT);

        userRepo.save(user);


    }


}
