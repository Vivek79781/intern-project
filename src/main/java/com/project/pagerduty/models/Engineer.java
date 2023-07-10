package com.project.pagerduty.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "engineers")
public class Engineer {
    @Id
    String id;
    @Field
    String name;
    @Field
    String email;
    @Field
    String role;
    @Field
    boolean available;

    public Engineer(String id, String name, String email, String role, boolean available) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
