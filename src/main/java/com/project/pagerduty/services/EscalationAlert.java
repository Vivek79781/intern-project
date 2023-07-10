package com.project.pagerduty.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class EscalationAlert implements ApplicationContextAware, ScheduledTask {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    @Override
    public Map<String, Object> run(Map<String, Object> args) {
        AlertService alertService = applicationContext.getBean(AlertService.class);
        if(!alertService.handleEscalationPolicyResponse((ArrayList<String>) args.get("escalationPolicyLevels"),
                (String) args.get("alertId"),
                (Integer) args.get("i"))){
            return null;
        }
        return new HashMap<>();
    }
}
