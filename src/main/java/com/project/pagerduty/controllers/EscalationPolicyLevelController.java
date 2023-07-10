package com.project.pagerduty.controllers;

import com.project.pagerduty.models.EscalationPolicyLevel;
import com.project.pagerduty.services.EscalationPolicyLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/escalationpolicylevel")
public class EscalationPolicyLevelController {
    @Autowired
    EscalationPolicyLevelService escalationPolicyLevelService;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EscalationPolicyLevel> getAllEscalationPolicyLevels(){
        return escalationPolicyLevelService.getAllEscalationPolicyLevels();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EscalationPolicyLevel addEscalationPolicyLevel(EscalationPolicyLevel escalationPolicyLevel){
        return escalationPolicyLevelService.addEscalationPolicyLevel(escalationPolicyLevel);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EscalationPolicyLevel getEscalationPolicyLevel(@PathParam("id") String escalationPolicyLevelId){
        return escalationPolicyLevelService.getEscalationPolicyLevel(escalationPolicyLevelId);
    }
}
