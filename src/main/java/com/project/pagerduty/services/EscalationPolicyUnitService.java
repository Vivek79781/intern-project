package com.project.pagerduty.services;

import com.project.pagerduty.models.EscalationPolicyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EscalationPolicyUnitService {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<EscalationPolicyUnit> getAllEscalationPolicyUnits(){
        return mongoTemplate.findAll(EscalationPolicyUnit.class);
    }

    public EscalationPolicyUnit addEscalationPolicyUnit(EscalationPolicyUnit escalationPolicyUnit){
        return mongoTemplate.insert(escalationPolicyUnit);
    }

    public EscalationPolicyUnit getEscalationPolicyUnit(String id){
        return mongoTemplate.findById(id, EscalationPolicyUnit.class);
    }
}
