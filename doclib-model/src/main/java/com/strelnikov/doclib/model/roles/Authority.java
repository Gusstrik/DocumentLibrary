package com.strelnikov.doclib.model.roles;

import lombok.Getter;

import javax.persistence.*;

@Getter

@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AuthorityType name;
}
