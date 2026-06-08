package home.local.cable_control.model;

import jakarta.persistence.*;



public class User {

/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private Long id;
    private String name;
    private String mail;
    //@Enumerated(EnumType.STRING)
    private Role role;
    private String login;
    private String password;


}
