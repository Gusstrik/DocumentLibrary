package com.strelnikov.doclib.model.roles;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AuthorityType name;

    public Authority(String role) {
        switch (role) {
            case "ROLE_USER":
                this.id = 1;
                this.name = AuthorityType.ROLE_USER;
                break;
            case "ROLE_ADMIN":
                this.id = 2;
                this.name = AuthorityType.ROLE_ADMIN;
                break;
        }
    }
    @Override
    public boolean equals(Object object){
        if (object instanceof Authority){
            Authority check = (Authority)object;
            return this.id==check.id;
        }
        return false;
    }
}
