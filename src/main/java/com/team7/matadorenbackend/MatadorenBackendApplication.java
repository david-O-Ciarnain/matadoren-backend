package com.team7.matadorenbackend;

import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserRepo;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleService;
import com.team7.matadorenbackend.appuser.roles.RolesRepo;
import com.team7.matadorenbackend.registration.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class MatadorenBackendApplication {


    public static void main(String[] args) {
        SpringApplication.run(MatadorenBackendApplication.class, args);

    }

    @Bean
    CommandLineRunner run(AppUserService appUserService, RoleService roleService){

        return args -> {
            roleService.saveRoles();
            appUserService.signUpAppUser(new AppUser("demo","demo","admin","123",new ArrayList<>()));
            appUserService.signUpAppUser(new AppUser("user","user","user","321",new ArrayList<>()));

            appUserService.addRoleToAppUSer("user","USER");
            appUserService.addRoleToAppUSer("admin","ADMIN");
        };
    }
}
