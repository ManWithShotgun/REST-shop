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
    private boolean online;
    private String token;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Account() {
    }

    public Account(String username, String password, boolean online, Role role) {
        this.username = username;
        this.password = password;
        this.online = online;
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

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
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
