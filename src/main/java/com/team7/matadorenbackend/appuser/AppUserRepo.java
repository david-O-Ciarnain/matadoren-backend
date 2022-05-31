package com.team7.matadorenbackend.appuser;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser,String> {

    Optional<AppUser>findByUsernameAndFirstNameAndLastName(String name);

   boolean existsByUsername(String username);
}
