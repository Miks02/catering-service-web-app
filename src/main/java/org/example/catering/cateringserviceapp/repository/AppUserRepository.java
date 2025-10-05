package org.example.catering.cateringserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {

    public Optional<AppUser> findUserByUsername(String username);

    public Optional<AppUser> findUserByEmail(String email);

}
