package com.solutions.coyne.betathetapi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick Coyne on 8/24/2017.
 */

public class User {

    private String Name;
    private String key;
    private String School;
    private String email;
    private List<Group> groups;
    private List<String> groupsStrings;
    // 0 = nothing, 1 = pledge, 2 = Active, 3= Alumni
    private int Role;


    public User() {
        groups = new ArrayList<>();
        groups.add(new Group("Pledge"));
        groups.add(new Group("Intramural"));
        groupsStrings = new ArrayList<>();
        groupsStrings.add("Pledge");
        groupsStrings.add("Intramural");
        Role = 0;
    }

    public User(String name, String key, String school, String email) {
        Name = name;
        this.key = key;
        School = school;
        this.email = email;
        groups = new ArrayList<>();
        groups.add(new Group("Pledge"));
        groups.add(new Group("Intramural"));
        groupsStrings = new ArrayList<>();
        groupsStrings.add("Pledge");
        groupsStrings.add("Intramural");
        Role = 0;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addGroup(Group group){
        groups.add(group);
    }

    public List<String> getGroupsStrings() {
        return groupsStrings;
    }

    public void setGroupsStrings(List<String> groupsStrings) {
        this.groupsStrings = groupsStrings;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }
}
