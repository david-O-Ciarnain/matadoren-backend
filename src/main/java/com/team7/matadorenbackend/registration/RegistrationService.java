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
    private final EmailValidator emailValidator;

    public AppUser register(RegistrationRequest request){

        boolean isEmailValid = emailValidator.test(request.getEmail());

        if(!isEmailValid){
            throw new IllegalStateException("email not valid");
        }

        AppUser user = appUserService.signUpAppUser(new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                new ArrayList<>()
        ));

        return user;
    }
    public List<Roles>getRoles(){
        return roleService.getAllRolls();
    }
}
