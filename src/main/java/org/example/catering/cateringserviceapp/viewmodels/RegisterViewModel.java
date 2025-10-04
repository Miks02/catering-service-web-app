package org.example.catering.cateringserviceapp.viewmodels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterViewModel {

    @NotEmpty(message = "Ime je obavezno")
    private String firstName;

    @NotEmpty(message = "Prezime je obavezno")
    private String lastName;

    @Size(min = 4, message = "Korisničko ime mora imati najmanje 4 karaktera")
    @NotEmpty(message = "Korisničko ime je obavezno")
    private String username;

    @NotEmpty(message = "Email je obavezan")
    @Email(message = "Unesite validan email")
    private String email;

    @Size(min = 4, message = "Lozinka mora imati najmanje 4 karaktera")
    @NotEmpty(message = "Lozinka je obavezna")
    private String password;

    @NotEmpty(message = "Potvrdite lozinku")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}
