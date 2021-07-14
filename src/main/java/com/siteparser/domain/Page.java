package com.siteparser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(exclude = {"title", "content", "description"})
@Setter
@Getter
@ToString
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "URL")
    private String url;

    @Column(name = "title")
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "content")
    private String content;

    @JsonIgnore
    @JoinColumn(name = "project_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;
}
