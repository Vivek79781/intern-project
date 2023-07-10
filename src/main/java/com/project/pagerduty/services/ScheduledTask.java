package com.project.pagerduty.services;

import java.util.Map;

public interface ScheduledTask {
    Map<String,Object> run(Map<String,Object> args) throws NoSuchMethodException;
}
