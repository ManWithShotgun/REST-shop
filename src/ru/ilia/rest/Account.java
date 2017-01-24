package ru.ilia.rest;

/**
 * Created by ILIA on 24.01.2017.
 */
public class Account {
    int id;
    String login;
    String password;
    int group;

    public Account(int id, String login, String password, int group) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId_account(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
