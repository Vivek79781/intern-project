package com.project.pagerduty.controllers;

import com.project.pagerduty.models.Alert;
import com.project.pagerduty.services.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Component
@Path("/alert")
public class AlertController {
    @Autowired
    AlertService alertService;

    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alert> getAllAlerts(){
        return alertService.getAllAlerts();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Alert addAlert(Map<String ,Object> payload) throws NoSuchMethodException {
        return alertService.addAlert(payload);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Alert getAlert(@PathParam("id") String alertId){
        return alertService.getAlert(alertId);
    }
}
