package com.team7.matadorenbackend.appuser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AppUser {


    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid-2")
    @GeneratedValue(generator = "uuid")
    private String id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;




}
