package com.javatasks.entity;

public class User {

    private int id;
    private String name;
    private String surname;
    private String login;
    private String email;

    public User() {
        this.id = 0;
        this.name = "";
        this.surname = "";
        this.login = "";
        this.email = "";
    }


    public User(int id, String name, String surname, String login, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
    }

    public User(String name, String surname, String login, String email) {
        this.id = 0;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + "\nИмя: " + getName() +
                "\nФамилия: " + getSurname() + "\nЛогин: " +
                getLogin() + "\nEmail: " + getEmail() + "\n\n";
    }

}