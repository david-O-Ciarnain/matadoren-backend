package com.team7.matadorenbackend.appuser.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends JpaRepository<Roles,String> {

    Roles findByName(String roleName);
    Boolean existsByName(String name);
}
