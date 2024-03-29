package com.siteparser.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "parsingStatus")
    private boolean parsingStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Page> pages = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
