package com.project.pagerduty.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "alerts")
public class Alert {
    @Id
    String id;
    @Field
    String title;
    @Field
    String description;
    @Field
    Date createdAt;
    @Field
    Date resolvedAt;
    @Field
    ArrayList<String> escalationPolicyUnits;

    @Field
    boolean status;

    @Field
    int scheduled;

    @Field
    ArrayList<String> currentAssignedEngineers;

    public Alert(String id, String title, String description, Date createdAt, Date resolvedAt, ArrayList<String> escalationPolicyUnits, ArrayList<String> currentAssignedEngineers, boolean status, int scheduled) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
        assert escalationPolicyUnits != null && escalationPolicyUnits.size() != 0;
        this.escalationPolicyUnits = escalationPolicyUnits;
        this.currentAssignedEngineers = currentAssignedEngineers;
        this.status = status;
        this.scheduled = scheduled;
    }

    public ArrayList<String> getCurrentAssignedEngineers() {
        return currentAssignedEngineers;
    }

    public void setCurrentAssignedEngineers(ArrayList<String> currentAssignedEngineers) {
        this.currentAssignedEngineers = currentAssignedEngineers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public ArrayList<String> getEscalationPolicyUnits() {
        return escalationPolicyUnits;
    }

    public void setEscalationPolicyUnits(ArrayList<String> escalationPolicyUnits) {
        assert escalationPolicyUnits != null && escalationPolicyUnits.size() != 0;
        this.escalationPolicyUnits = escalationPolicyUnits;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getScheduled() {
        return scheduled;
    }

    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }
}
