package com.team7.matadorenbackend.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public AppUser getAppUser(String name) {

        return appUserRepo.findByUsernameAndFirstNameAndLastName(name).orElseThrow();
    }

    public AppUser signUpAppUser(AppUser appUser) {
        boolean userExists = appUserRepo.existsByUsername(appUser.getUsername());

        if (userExists) {
            throw new IllegalStateException("User do not exists");
        }
        String encode = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encode);
        return appUserRepo.save(appUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
