package com.project.pagerduty.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Objects;

@Document(collection = "escalationPolicyLevel")
public class EscalationPolicyLevel {
    @Id
    private String id;
    @Field
    private String escalationPolicyUnit;
    @Field
    private String name;
    @Field
    private int stepNumber;
    @Field
    private String nextStep;
    @Field
    private String lastTriedEngineer;
    @Field
    private int timeToJump;
    @Field
    private int numberOfCycles;
    @Field
    ArrayList<String> engineers;

    public ArrayList<String> getEngineers() {
        return engineers;
    }

    public void setEngineers(ArrayList<String> engineers) {
        this.engineers = Objects.requireNonNullElseGet(engineers, ArrayList::new);
    }

    public EscalationPolicyLevel(String id, String escalationPolicyUnit, String name, int stepNumber, String nextStep, String lastTriedEngineer, int timeToJump, int numberOfCycles, ArrayList<String> engineers) {
        this.id = id;
        this.escalationPolicyUnit = escalationPolicyUnit;
        this.name = name;
        this.stepNumber = stepNumber;
        this.nextStep = nextStep;
        this.lastTriedEngineer = lastTriedEngineer;
        this.timeToJump = timeToJump;
        this.numberOfCycles = numberOfCycles;
        this.engineers = Objects.requireNonNullElseGet(engineers, ArrayList::new);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEscalationPolicyUnit() {
        return escalationPolicyUnit;
    }

    public void setEscalationPolicyUnit(String escalationPolicyUnit) {
        this.escalationPolicyUnit = escalationPolicyUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getLastTriedEngineer() {
        return lastTriedEngineer;
    }

    public void setLastTriedEngineer(String lastTriedEngineer) {
        this.lastTriedEngineer = lastTriedEngineer;
    }

    public int getTimeToJump() {
        return timeToJump;
    }

    public void setTimeToJump(int timeToJump) {
        this.timeToJump = timeToJump;
    }

    public int getNumberOfCycles() {
        return numberOfCycles;
    }

    public void setNumberOfCycles(int numberOfCycles) {
        this.numberOfCycles = numberOfCycles;
    }
}
