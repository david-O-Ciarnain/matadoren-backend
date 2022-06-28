package com.team7.matadorenbackend.registration;

import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleRequest;
import com.team7.matadorenbackend.appuser.roles.RoleService;
import com.team7.matadorenbackend.appuser.roles.Roles;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "register/user")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AppUserService appUserService;
    private final RoleService roleService;

    @PostMapping(path = "save")
    private ResponseEntity<AppUser> registerAppUser(@RequestBody RegistrationRequest request) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/register/user/save").toUriString());
        return ResponseEntity.created(uri).body(registrationService.register(request));
    }

    @GetMapping(path = "get/{name}")
    public ResponseEntity<List<AppUser>> getAppUser(@PathVariable String name) {
        return ResponseEntity.ok().body(appUserService.getAppUser(name));
    }
    @GetMapping(path = "get")
    public ResponseEntity<List<AppUser>> getAllAppUser(String name) {
        return ResponseEntity.ok().body(appUserService.getAppUser(name));
    }

    @DeleteMapping(path = "delete/{username}")
    public void deleteAppUser(@PathVariable String username) {
        appUserService.deleteAppUserByUserName(username);
    }

    @PutMapping(path = "update/{username}")
    public AppUser updateAppUser(@RequestBody AppUser appUser, @PathVariable String username) {
        return appUserService.updateAppUser(appUser, username);
    }

    @GetMapping(path = "roles/get")
    public List<Roles> getAllRoles() {
        return registrationService.getRoles();
    }

    @PostMapping(path = "roles/save")
    public List<Roles> saveRoles() {
        return roleService.saveRoles();
    }

    @PostMapping(path = "roles/addroletouser")
    public ResponseEntity<?> roleToAppUser(@RequestBody RoleRequest request) {
        appUserService.addRoleToAppUSer(request.getUsername(), request.getRoleName());
        return ResponseEntity.ok().build();
    }
}
