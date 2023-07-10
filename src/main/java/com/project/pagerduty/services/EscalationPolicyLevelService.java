package com.project.pagerduty.services;

import com.project.pagerduty.models.EscalationPolicyLevel;
import com.project.pagerduty.models.EscalationPolicyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EscalationPolicyLevelService {
    @Autowired
    MongoTemplate mongoTemplate;
    public List<EscalationPolicyLevel> getAllEscalationPolicyLevels(){
        return mongoTemplate.findAll(EscalationPolicyLevel.class);
    }

    public EscalationPolicyLevel addEscalationPolicyLevel(EscalationPolicyLevel escalationPolicyLevel){
        EscalationPolicyLevel savedEscalationPolicyLevel = mongoTemplate.insert(escalationPolicyLevel);
        EscalationPolicyUnit currentLevelUnit = mongoTemplate.findById(savedEscalationPolicyLevel.getEscalationPolicyUnit(), EscalationPolicyUnit.class);
        assert currentLevelUnit != null;
        ArrayList<String> newEscalationPolicyLevels = currentLevelUnit.getEscalationPolicyLevels();
        newEscalationPolicyLevels.add(savedEscalationPolicyLevel.getId());
        mongoTemplate.save(currentLevelUnit);
        return savedEscalationPolicyLevel;
    }

    public EscalationPolicyLevel getEscalationPolicyLevel(String id){
        return mongoTemplate.findById(id, EscalationPolicyLevel.class);
    }
}
