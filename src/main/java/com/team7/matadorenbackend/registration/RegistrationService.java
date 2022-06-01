package com.team7.matadorenbackend.registration;

import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleService;
import com.team7.matadorenbackend.appuser.roles.Roles;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final RoleService roleService;

    public AppUser register(RegistrationRequest request){

        AppUser user = appUserService.signUpAppUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getPassword(),
                new ArrayList<>()
        ));

        return user;
    }
    public List<Roles>getRoles(){
        return roleService.getAllRolls();
    }
}
