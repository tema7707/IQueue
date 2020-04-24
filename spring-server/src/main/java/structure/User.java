package structure;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public static Map<String, User> users = new HashMap<>();

    private String id;
    private String name, surname;
    @JsonIgnore
    private List<Note> notes = new ArrayList<>();
    private String login, password;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public User(String login, String password, String name, String surename){
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surename;
    }
}
