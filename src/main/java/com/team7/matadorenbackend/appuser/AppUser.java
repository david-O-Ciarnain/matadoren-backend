package com.team7.matadorenbackend.appuser;

import com.team7.matadorenbackend.appuser.roles.Roles;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class AppUser {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String username;
    private String firstName;

    private String lastName;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Roles>roles = new ArrayList<>();


    public AppUser(String username, String firstName, String lastName, String password, Collection<Roles> roles) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.roles = roles;
    }
}
