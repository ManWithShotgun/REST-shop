package ru.ilia.rest.model.entity;

import ru.ilia.rest.model.util.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ILIA on 27.01.2017.
 */
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(name="id_account")
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String username;
    @NotNull
    private String password;
    private String name;
    private String email;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Account() {
    }

    public Account(String username, String password, String name, String email, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("Account: id: %d user: %s pass: %s", this.id, this.username, this.password);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
