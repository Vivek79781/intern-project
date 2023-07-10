package com.project.pagerduty.services;


import com.project.pagerduty.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class AlertService implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);
    private static ApplicationContext applicationContext;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    TaskService taskService;
    @Autowired
    MailService mailService;
    @Autowired
    ModService modService;
    @Autowired
    ThreadPoolTaskExecutor modExecutor;

    public List<Alert> getAllAlerts(){
        return mongoTemplate.findAll(Alert.class);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Alert addAlert(Map<String,Object> payload) throws NoSuchMethodException {
        Set<String> allEscalationPolicyUnits = new HashSet<>();
        ArrayList<CompletableFuture<List<String>>> futures = new ArrayList<>();
        if (modService.isCompiled()){
            Set<String> compiledModsId = modService.getCompiledModIds();
            for (String modId: compiledModsId
            ) {
                CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> modService.executeMod(modId, payload), modExecutor);
                futures.add(future);
            }
        } else {
            List<Mod> mods = modService.find();
            for (Mod mod : mods
            ) {
                CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> modService.compileAndExecuteMod(mod, payload), modExecutor);
                futures.add(future);
            }
            modService.setCompiled(true);
        }

        for (CompletableFuture<List<String>> future : futures
        ) {
            try {
                List<String> escalationPolicyUnit = future.get();
                if (escalationPolicyUnit != null && escalationPolicyUnit.size() != 0)
                    allEscalationPolicyUnits.addAll(escalationPolicyUnit);
            } catch (Exception ignored) {
            }
        }
        Alert alert = new Alert(null,payload.get("title").toString(),payload.get("description").toString(),new Date(),null,new ArrayList<>(allEscalationPolicyUnits),new ArrayList<>(allEscalationPolicyUnits),false, 0);
        ArrayList<String> escalationPolicyUnitsId = alert.getEscalationPolicyUnits();
        logger.info(escalationPolicyUnitsId.toString());
        List<EscalationPolicyUnit> escalationPolicyUnits = mongoTemplate.find(Query.query(Criteria.where("_id").in(escalationPolicyUnitsId)),EscalationPolicyUnit.class);
        ArrayList<String> escalationPolicies = new ArrayList<>();
        for(EscalationPolicyUnit escalationPolicyUnit: escalationPolicyUnits) {
            escalationPolicies.add(escalationPolicyUnit.getId());
            logger.info(escalationPolicyUnit.getId());
        }
        alert.setEscalationPolicyUnits(escalationPolicies);
        alert.setCurrentAssignedEngineers(escalationPolicies);

        Alert savedAlert = mongoTemplate.insert(alert);
        Class<ProcessAlert> c = ProcessAlert.class;
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("alertId",savedAlert.getId());
        Task task = new Task(null,c.getName(),c.getDeclaredMethod("run", Map.class).getName(),arguments,new Date(),false);
        taskService.createTask(task);
        return savedAlert;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void processAlert(String savedAlertId) throws NoSuchMethodException {
        logger.info("Alert ID:"+savedAlertId);
        List<Alert> alerts = mongoTemplate.find(Query.query(Criteria.where("_id").is(savedAlertId).and("status").is(false).and("scheduled").is(0)),Alert.class);
        logger.info("Processing Alert");
        if(alerts.size() == 0)
            return;

        for (int i = 0; i < Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).getEscalationPolicyUnits().size(); i++) {
            EscalationPolicyUnit escalationPolicyUnit = mongoTemplate.findById(Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).getEscalationPolicyUnits().get(i), EscalationPolicyUnit.class);
            assert escalationPolicyUnit != null;
            ArrayList<String> escalationPolicyLevels = escalationPolicyUnit.getEscalationPolicyLevels();
            mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(savedAlertId)),new Update().inc("scheduled",1),Alert.class);
            escalationPolicyTask(i, escalationPolicyLevels, savedAlertId);
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void escalationPolicyTask(int i, ArrayList<String> escalationPolicyLevels, String alertId) throws NoSuchMethodException {
        Class<EscalationAlert> cls = EscalationAlert.class;
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("escalationPolicyLevels",escalationPolicyLevels);
        arguments.put("alertId",alertId);
        arguments.put("i",i);
        Task task = new Task(null,cls.getName(),cls.getDeclaredMethod("run", Map.class).getName(),arguments,new Date(),false);
        taskService.createTask(task);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean handleEscalationPolicyResponse(ArrayList<String> escalationPolicyLevels, String savedAlertId, int i){
        try {
            for (String escalationPolicyLevelId : escalationPolicyLevels) {
                // Called Database
                EscalationPolicyLevel escalationPolicyLevel = mongoTemplate.findById(escalationPolicyLevelId, EscalationPolicyLevel.class);
                assert escalationPolicyLevel != null;
                ArrayList<String> engineers = escalationPolicyLevel.getEngineers();
                for (int j = 0; j < escalationPolicyLevel.getNumberOfCycles(); j++) {
                    for (String engineer : engineers) {
                        if(Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).isStatus()){
                            return true;
                        }
                        // Assigning New Engineer
                        assignNewEngineer(savedAlertId, i, engineer);
                        // Get Current Assigned Engineer
                        final String[] currentAssignedEngineer = {engineer};
                        if (Objects.requireNonNull(mongoTemplate.findById(currentAssignedEngineer[0], Engineer.class)).isAvailable()) {
                            try {
                                // Calling the engineer
                                callEngineer(savedAlertId, engineer);
                                // Schedule a timeout in thread
                                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                                executor.schedule(() -> {
                                    if (!Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).isStatus()) {
                                        logger.info(currentAssignedEngineer[0] + " is not responded");
                                        currentAssignedEngineer[0] = null;
                                        freeEngineer(engineer);
                                    }
                                }, 30, TimeUnit.SECONDS);


                                while (currentAssignedEngineer[0] != null && !Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).isStatus()) {
                                    try {
                                        Thread.sleep(5000);
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                executor.shutdownNow();
                                freeEngineer(engineer);
                                if (Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).isStatus()) {
                                    resolvedAlert(i, savedAlertId);
                                    return true;
                                }
                            } catch (Exception e){
                                freeEngineer(engineer);
                            }
                        } else {
                            logger.info(engineer+" is not available right now");
                        }
                    }
                }
            }
        } catch (Exception e){
            logger.error(Arrays.toString(e.getStackTrace()));
        }
        if (!Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)).isStatus()) {
            Alert savedAlert = mongoTemplate.findById(savedAlertId, Alert.class);
            assert savedAlert != null;
            ArrayList<String> currentAssignedEngineers = savedAlert.getCurrentAssignedEngineers();
            currentAssignedEngineers.set(i, savedAlert.getEscalationPolicyUnits().get(i));
            mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(savedAlertId)),new Update().set("currentAssignedEngineers",currentAssignedEngineers),Alert.class);
            return false;
        } else {
            return true;
        }
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void freeEngineer(String engineerId) {
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(engineerId)),new Update().set("available",true),Engineer.class);
    }

    private void resolvedAlert(int i, String savedAlertId) {
        logger.info(savedAlertId+" is resolved");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void callEngineer(String savedAlertId, String engineerId) throws MessagingException {
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(engineerId)),new Update().set("available",false),Engineer.class);
        Engineer engineer = mongoTemplate.findById(engineerId,Engineer.class);
        assert engineer != null;

        mailService.sendMail(engineer.getEmail(),engineerId, Objects.requireNonNull(mongoTemplate.findById(savedAlertId, Alert.class)));
        logger.info("Requested EngineerID: " + engineerId + " Please Click On http://localhost:9090/engineer/"+ engineerId +"/alert/"+savedAlertId + " to respond.");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void assignNewEngineer(String savedAlertId, int i, String engineer) {
        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(savedAlertId)), new Update().set("currentAssignedEngineers."+i, engineer), Alert.class);
    }

    public Alert getAlert(String alertId){
        return mongoTemplate.findById(alertId,Alert.class);
    }

    public void updateAlert(Alert alert){
        mongoTemplate.save(alert);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AlertService.applicationContext = applicationContext;
    }
}
