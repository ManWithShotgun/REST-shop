package ru.ilia.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import ru.ilia.rest.model.util.Config;
import ru.ilia.rest.model.util.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by ILIA on 27.01.2017.
 */
@Entity
@Table(name = "accounts")
@JsonRootName("account")
public class Account {
    @Id
    @Column(name="id_account")
    @GeneratedValue
    @JsonIgnore
    private long id;
    @Column(unique = true, nullable = false)
    @JsonProperty("login")
    private String username;
    @NotNull
    @JsonIgnore
    private String password;
    private String name;
    private String img;
    private String email;
    @JsonIgnore
    private String token;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Role role;

    public Account() {
    }

    public Account(String username, String password, String name, String email, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.img = Config.DEFAULT_ACCOUNT_IAMGE;
        this.token="";
    }

    @Override
    public String toString() {
        return String.format("Account: id: %d user: %s pass: %s token: %s", this.id, this.username, this.password, this.token);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
