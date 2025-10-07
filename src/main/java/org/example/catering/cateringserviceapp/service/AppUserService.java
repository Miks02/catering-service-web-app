package org.example.catering.cateringserviceapp.service;

import org.example.catering.cateringserviceapp.enums.Role;
import org.example.catering.cateringserviceapp.exceptions.UserAlreadyExistsException;
import org.example.catering.cateringserviceapp.helpers.AppLogger;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.example.catering.cateringserviceapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Date;
import java.util.Optional;

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

    public void registerUser(AppUser user, Role role) {

        if(userRepo.findUserByUsername(user.getUsername()).isPresent())
            throw new UserAlreadyExistsException("Navedeno korisničko ime je zauzeto");

        if(userRepo.findUserByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException("Navedena email adresa je zauzeta");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);

        userRepo.save(user);


    }

    public void registerEmployee(AppUser employee, MultipartFile imageFile) throws IOException, IllegalArgumentException {
        AppUser employeeToSave = new AppUser();

        if(employee.getId() == null) {
            if(getUserByUsername(employee.getUsername()).isPresent())
                throw new UserAlreadyExistsException("Korisničko ime je zauzeto");

            if(getUserByEmail(employee.getEmail()).isPresent())
                throw new UserAlreadyExistsException("Email adresa je zauzeta");

            AppLogger.info("Korisnik se dodaje");

        }
        else {
            employeeToSave = getUserById(employee.getId()).orElseThrow(() -> new IllegalArgumentException("Korisnik nije pronadjen"));
            if(!employeeToSave.getUsername().equals(employee.getUsername())) {
                if(getUserByUsername(employee.getUsername()).isPresent())
                    throw new UserAlreadyExistsException("Korisničko ime je zauzeto");
                if(getUserByEmail(employee.getEmail()).isPresent())
                    throw new UserAlreadyExistsException("Email adresa je zauzeta");
            }

            AppLogger.info("Korisnik se ažurira");

        }

        AppLogger.info("Validacija korisnika je prošla uspešno!");


        if(imageFile != null && !imageFile.isEmpty()) {
            String storageFileName = new Date(System.currentTimeMillis()) + "_" + imageFile.getOriginalFilename();

            String uploadDir = "uploads/";
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                employeeToSave.setImagePath(storageFileName);
            }
        }

        employeeToSave.setFirstName(employee.getFirstName());
        employeeToSave.setLastName(employee.getLastName());
        employeeToSave.setUsername(employee.getUsername());
        employeeToSave.setEmail(employee.getEmail());
        employeeToSave.setRole(Role.MANAGER);
        employeeToSave.setAddress(employee.getAddress());
        employeeToSave.setPhone(employee.getPhone());
        employeeToSave.setCreatedAt(new Date(System.currentTimeMillis()));

        if (employee.getId() == null || !employee.getPassword().isEmpty()) {
            employeeToSave.setPassword(passwordEncoder.encode(employee.getPassword()));
        }

        userRepo.save(employeeToSave);

    }

    public void seedUser(AppUser user) {

        var existingUser = userRepo.findUserByUsername(user.getUsername());

        if(existingUser.isPresent()) {
            System.out.println("Seedovanje se prekida, korisnik već postoji");
            return;
        }


        user.setCreatedAt(new Date(System.currentTimeMillis()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<AppUser> getUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public Optional<AppUser> getUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }


}
