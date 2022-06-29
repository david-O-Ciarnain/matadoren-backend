package com.team7.matadorenbackend.appuser;

import com.team7.matadorenbackend.appuser.roles.Roles;
import com.team7.matadorenbackend.appuser.roles.RolesRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class AppUserService implements UserDetailsService {

    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RolesRepo rolesRepo;


   public List<AppUser> getAppUser(String name) {

        if (name == null || name.isEmpty()) {
            return appUserRepo.findAll();
        }
        return appUserRepo.search(name);
    }
    public AppUser getUser(String username){
        return appUserRepo.findByUsername(username).orElseThrow(IllegalStateException::new);
    }

    public AppUser signUpAppUser(AppUser appUser) {
        boolean userExists = appUserRepo.existsByUsername(appUser.getUsername());

        if (userExists) {
            throw new IllegalStateException("User already exists");
        }

        String encode = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encode);

        return appUserRepo.save(appUser);
    }


    public void deleteAppUserByUserName(String username) {

        appUserRepo
                .deleteByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username));
    }


    public AppUser updateAppUser(AppUser appUser, String username) {
        String encode = bCryptPasswordEncoder.encode(appUser.getPassword());
        return appUserRepo
                .findByUsername(username)
                .map(user -> {
                    user.setUsername(appUser.getUsername());
                    user.setFirstName(appUser.getFirstName());
                    user.setLastName(appUser.getLastName());

                    user.setPassword(encode);
                    user.setEmail(appUser.getEmail());

                    return appUserRepo.save(user);
                })
                .orElseGet(() -> {
                    appUser.setUsername(username);

                    return appUserRepo.save(appUser);
                });
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(roles -> authorities.add(new SimpleGrantedAuthority(roles.getName())));

        return new User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    public void addRoleToAppUSer(String username, String roleName) {
        AppUser appUser = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("user with username " + username + " not exists"));

        Roles roles = rolesRepo.findByName(roleName);
        appUser.getRoles().add(roles);
    }


}
