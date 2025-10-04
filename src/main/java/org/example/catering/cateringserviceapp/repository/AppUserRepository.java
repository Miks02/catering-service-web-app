package org.example.catering.cateringserviceapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.catering.cateringserviceapp.models.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Integer> {

    public AppUser findUserByUsername(String username);

    public AppUser findUserByEmail(String email);

}
