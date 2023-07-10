package com.project.pagerduty.services;

import com.project.pagerduty.models.Mod;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class ModService {
    private static final Logger logger = LoggerFactory.getLogger(ModService.class);
    private final Map<String,Class> compiledMods;
    private boolean compiled = false;
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    public ModService(Map<String, Class> compiledMods) {
        this.compiledMods = compiledMods;
    }

    public Mod addMod(Mod mod){
        Mod savedMod = mongoTemplate.save(mod);
        Class<?> compiledMod = new GroovyClassLoader().parseClass(savedMod.getContent());
        logger.info(mod.getId());
        logger.info(String.valueOf(getCompiledModIds().contains(mod.getId())));
        compiledMods.put(savedMod.getId(),compiledMod);
        return savedMod;
    }

    public List<Mod> find(){
        return mongoTemplate.findAll(Mod.class);
    }

    public Mod findById(String modId){
        return mongoTemplate.findById(modId,Mod.class);
    }

    public Mod updateMod(Mod mod){
        Mod savedMod = mongoTemplate.save(mod);
        Class<?> compiledMod = new GroovyClassLoader().parseClass(savedMod.getContent());
        compiledMods.put(savedMod.getId(),compiledMod);
        return savedMod;
    }

    public Mod deleteMod(String modId){
        Mod deletedMod = mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(modId)),Mod.class);
        compiledMods.remove(modId);
        return deletedMod;
    }

    public List<String> compileAndExecuteMod(Mod mod, Map<String,Object> alert){
        try {
            if(!compiledMods.containsKey(mod.getId())){
                Class<?> compiledMod = new GroovyClassLoader().parseClass(mod.getContent());
                compiledMods.put(mod.getId(),compiledMod);
            }
            Class compiledMod = compiledMods.get(mod.getId());
            GroovyObject scriptInstance = (GroovyObject) compiledMod.getDeclaredConstructor().newInstance();
            scriptInstance.setProperty("alert",alert);
            return (List<String>) scriptInstance.invokeMethod("run",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> executeMod(String modId, Map<String,Object> alert){
        try {
            if(Objects.equals(modId, "64a66918966b3454c60ff211")){
                Class compiledMod = compiledMods.get(modId);
                GroovyObject scriptInstance = (GroovyObject) compiledMod.getDeclaredConstructor().newInstance();
                scriptInstance.setProperty("alert",alert);
                List<String> list = (List<String>) scriptInstance.invokeMethod("run",null);
                logger.info(list.toString());
                return list;
            }
            Class compiledMod = compiledMods.get(modId);
            GroovyObject scriptInstance = (GroovyObject) compiledMod.getDeclaredConstructor().newInstance();
            scriptInstance.setProperty("alert",alert);
            return (List<String>) scriptInstance.invokeMethod("run",null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isCompiled(){
        return compiled;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public Set<String> getCompiledModIds() {
        return compiledMods.keySet();
    }

    @PostConstruct
    public void compileMods(){
        List<Mod> mods = mongoTemplate.findAll(Mod.class);
        for (Mod mod: mods
             ) {
            Class<?> compiledMod = new GroovyClassLoader().parseClass(mod.getContent());

            compiledMods.put(mod.getId(),compiledMod);
        }
        setCompiled(true);
    }
}
