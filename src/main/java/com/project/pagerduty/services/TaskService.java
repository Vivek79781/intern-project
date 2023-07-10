package com.project.pagerduty.services;

import com.project.pagerduty.models.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    public void createTask(Task task){
        mongoTemplate.insert(task);
    }

    @Scheduled(fixedRate = 30_000)
    public void runningTasks() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info(String.valueOf(new Date().getTime()));
        List<Task> tasks = mongoTemplate.find(Query.query(Criteria.where("scheduledTime").lte(new Date()).and("scheduled").is(false)),Task.class);
        logger.info("Task Remaining:" + tasks.size());
        for (Task task:
                tasks) {
            mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(task.getId())),new Update().set("scheduled",true),Task.class);
            Class<?> c = Class.forName(task.getClassName());
            Method method = c.getDeclaredMethod(task.getMethodName(),Map.class);
            Map<String, Object> argument = task.getArguments();
            Object obj = c.getConstructor().newInstance();
            taskExecutor.execute(() -> {
                try {
                    if(method.invoke(obj,argument) == null){
                        mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(task.getId())),new Update().set("scheduled",false),Task.class);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    mongoTemplate.findAndModify(Query.query(Criteria.where("id").is(task.getId())),new Update().set("scheduled",false),Task.class);
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
