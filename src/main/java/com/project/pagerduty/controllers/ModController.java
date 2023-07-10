package com.project.pagerduty.controllers;

import com.project.pagerduty.models.Mod;
import com.project.pagerduty.services.ModService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Path("/mod")
public class ModController {
    @Autowired
    ModService modService;

    private static final Logger logger = LoggerFactory.getLogger(ModController.class);


    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Mod> getAllMods(){
        return modService.find();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mod addMod(@NotNull Mod mod){
        return modService.addMod(mod);
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Mod getMod(@PathParam("id") String modId){
        return modService.findById(modId);
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Mod deleteMod(@PathParam("id") String modId){
        return modService.deleteMod(modId);
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Mod updateMod(@PathParam("id") String modId, Mod mod){
        mod.setId(modId);
        return modService.updateMod(mod);
    }
    @Path("/{id}/exec")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> execMod(@PathParam("id") String modId){

        Mod mod = modService.findById(modId);
        Map<String,Object> payload = new HashMap<>();
        payload.put("title","care");
        return modService.compileAndExecuteMod(mod,payload);
    }
}
