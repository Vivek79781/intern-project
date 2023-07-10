package com.project.pagerduty.controllers;

import com.project.pagerduty.models.EscalationPolicyUnit;
import com.project.pagerduty.services.EscalationPolicyUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/escalationpolicyunit")
public class EscalationPolicyUnitController {
    @Autowired
    EscalationPolicyUnitService escalationPolicyUnitService;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EscalationPolicyUnit> getAllEscalationPolicyUnits(){
        return escalationPolicyUnitService.getAllEscalationPolicyUnits();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public EscalationPolicyUnit addEscalationPolicyUnit(EscalationPolicyUnit escalationPolicyUnit){
        return escalationPolicyUnitService.addEscalationPolicyUnit(escalationPolicyUnit);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public EscalationPolicyUnit getEscalationPolicyUnit(@PathParam("id") String escalationPolicyUnitId){
        return escalationPolicyUnitService.getEscalationPolicyUnit(escalationPolicyUnitId);
    }

}
