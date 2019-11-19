package com.siteparser.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "created_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name = "created_time")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    @Temporal(TemporalType.TIME)
    private Date createdTime;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Date lastModifiedDate;

    @Column(name = "last_modified_time")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    @Temporal(TemporalType.TIME)
    private Date lastModifiedTime;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "active")
    private int active = 1;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,  mappedBy = "user", orphanRemoval = true)
    private Set<Project> projects = new HashSet<>();

    public boolean hasRole(String roleName){
        for (Role role : roles){
            if (role.getRole().equals(roleName)) return true;
        }
        return false;
    }
}
