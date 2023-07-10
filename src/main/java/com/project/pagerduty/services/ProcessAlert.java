package com.project.pagerduty.services;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProcessAlert implements ApplicationContextAware, ScheduledTask {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ProcessAlert.applicationContext = applicationContext;
    }

    @Override
    public Map<String, Object> run(Map<String, Object> args) throws NoSuchMethodException {
        AlertService alertService = applicationContext.getBean(AlertService.class);
        alertService.processAlert((String) args.get("alertId"));
        return new HashMap<>();
    }
}
