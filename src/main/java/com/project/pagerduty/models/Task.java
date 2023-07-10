package com.project.pagerduty.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    @Field
    String className;
    @Field
    String methodName;
    @Field
    Map<String, Object> arguments;
    @Field
    Date scheduledTime;
    @Field
    boolean scheduled;

    public Task(String id, String className, String methodName, Map<String, Object> arguments, Date scheduledTime, boolean scheduled) {
        this.id = id;
        this.className = className;
        this.methodName = methodName;
        this.arguments = arguments;
        this.scheduledTime = scheduledTime;
        this.scheduled = scheduled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }
}
