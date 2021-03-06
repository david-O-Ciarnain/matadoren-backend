package com.team7.matadorenbackend.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class RegistrationRequest {

    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String password;
    private final String roles;
}
