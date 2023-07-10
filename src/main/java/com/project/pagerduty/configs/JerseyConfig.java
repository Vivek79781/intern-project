package com.project.pagerduty.configs;

import com.project.pagerduty.controllers.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig(){
        register(HelloWorldController.class);
        register(EngineerController.class);
        register(EscalationPolicyUnitController.class);
        register(EscalationPolicyLevelController.class);
        register(AlertController.class);
        register(ModController.class);
    }
}
