package org.example.catering.cateringserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    public Optional<AppUser> findUserByUsername(String username);

    public Optional<AppUser> findUserByEmail(String email);

    public Optional<AppUser> findUserById(Long id);

}
