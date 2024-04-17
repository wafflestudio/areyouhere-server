package com.waruru.areyouhere.manager.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity (name = "manager")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @Builder
    public Manager(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Manager update(String name, String password){
        this.password = password;
        this.name = name;
        return this;
    }
}
