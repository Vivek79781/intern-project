package com.project.pagerduty.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "escalationPolicyUnits")
public class EscalationPolicyUnit {
    @Id
    String id;
    @Field
    String name;
    @Field
    ArrayList<String> escalationPolicyLevels;

    public EscalationPolicyUnit(String id, String name, ArrayList<String> escalationPolicyLevels) {
        this.id = id;
        this.name = name;
        this.escalationPolicyLevels = Objects.requireNonNullElseGet(escalationPolicyLevels, ArrayList::new);
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

    public ArrayList<String> getEscalationPolicyLevels() {
        return escalationPolicyLevels;
    }

    public void setEscalationPolicyLevels(ArrayList<String> escalationPolicyLevels) {
        this.escalationPolicyLevels = Objects.requireNonNullElseGet(escalationPolicyLevels, ArrayList::new);
    }
}
