package com.project.pagerduty.services;

import com.project.pagerduty.models.Engineer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class EngineerService {

    @Autowired
    MongoTemplate mongoTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EngineerService.class);

    public List<Engineer> getAllEngineers(){
        return mongoTemplate.findAll(Engineer.class);
    }

    public Engineer addEngineer(Engineer engineer){
        engineer.setAvailable(true);
        return mongoTemplate.insert(engineer);
    }

    public Engineer getEngineer(String engineerId){
        return mongoTemplate.findById(engineerId, Engineer.class);
    }

    public Engineer updateEngineer(Engineer engineer) { return mongoTemplate.save(engineer); }

    @PostConstruct
    public void init(){
        mongoTemplate.findAndModify(Query.query(Criteria.where("available").is(false)),new Update().set("available",true),Engineer.class);
    }
}
