package com.team7.matadorenbackend.appuser;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppUserService {

private final AppUserRepo appUserRepo;


public AppUser getAppUser(String name){

    return appUserRepo.findByUsernameAndFirstNameAndLastName(name).orElseThrow();
}
}
