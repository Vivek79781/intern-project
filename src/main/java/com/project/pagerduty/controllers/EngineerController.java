package com.project.pagerduty.controllers;

import com.project.pagerduty.models.Alert;
import com.project.pagerduty.models.Engineer;
import com.project.pagerduty.services.AlertService;
import com.project.pagerduty.services.EngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Component
@Path("/engineer")
public class EngineerController {
    @Autowired
    EngineerService engineerService;

    @Autowired
    AlertService alertService;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Engineer> getAllEngineers(){
        return engineerService.getAllEngineers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Engineer addEngineer(Engineer engineer){
        return engineerService.addEngineer(engineer);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Engineer getEngineer(@PathParam("id") String engineerId){
        return engineerService.getEngineer(engineerId);
    }

    @Path("/{id}/alert/{alertId}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String alertResponse(@PathParam("id") String engineerId, @PathParam("alertId") String alertId) {
        Alert alert = alertService.getAlert(alertId);
        if (alert.isStatus()) {
            return "Alert is already resolved";
        } else {
            if (alert.getCurrentAssignedEngineers().contains(engineerId)) {
                alert.setStatus(true);
                alert.setResolvedAt(new Date());
                alertService.updateAlert(alert);
                return "Successful Acknowledgement";
            } else {
                return "It is not assigned to you";
            }
        }
    }
}
