package com.team7.matadorenbackend.registration;

import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleRequest;
import com.team7.matadorenbackend.appuser.roles.Roles;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "register/user")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AppUserService appUserService;

    @PostMapping(path = "save")
    private AppUser registerAppUser(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "get")
    public List<AppUser> getAppUser(String name) {
        return appUserService.getAppUser(name);
    }

    @DeleteMapping(path = "delete/{username}")
    public void deleteAppUser(@PathVariable  String username) {
        appUserService.deleteAppUserByUserName(username);
    }
    @PutMapping(path = "update/{username}")
    public AppUser updateAppUser(@RequestBody AppUser appUser,@PathVariable String username){
        return appUserService.updateAppUser(appUser,username);
    }

    @GetMapping(path = "roles/get")
    public List<Roles> getAllRoles() {
        return registrationService.getRoles();
    }

    @PostMapping(path = "roles/addroletouser")
    public ResponseEntity<?> roleToAppUser(@RequestBody RoleRequest request){
        appUserService.addRoleToAppUSer(request.getUsername(),request.getRoleName());
        return ResponseEntity.ok().build();
    }
}
