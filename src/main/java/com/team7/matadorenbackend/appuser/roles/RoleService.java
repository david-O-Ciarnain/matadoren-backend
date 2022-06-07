package com.team7.matadorenbackend.appuser.roles;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final RolesRepo rolesRepo;


        public List<Roles> saveRoles(){
            List<Roles> preSetRoles;


                preSetRoles = List.of(
                        new Roles("USER"),
                        new Roles("ADMIN")
                );

                List<Roles> rolesList = new ArrayList<>(preSetRoles);
                return rolesRepo.saveAll(rolesList);

    }

    public List<Roles> getAllRolls() {
        return rolesRepo.findAll();
    }
}
