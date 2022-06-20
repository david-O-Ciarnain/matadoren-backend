package com.team7.matadorenbackend;

import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleService;
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
            appUserService.signUpAppUser(new AppUser("demo","demo","admin","demo@live.se","123",new ArrayList<>()));
            appUserService.signUpAppUser(new AppUser("user","user","user","demo951@live.se","321",new ArrayList<>()));

            appUserService.addRoleToAppUSer("user","USER");
            appUserService.addRoleToAppUSer("admin","ADMIN");
        };
    }
}
