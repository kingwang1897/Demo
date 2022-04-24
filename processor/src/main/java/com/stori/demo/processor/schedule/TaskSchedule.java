package com.stori.demo.processor.schedule;

import com.stori.demo.processor.manager.MessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TaskSchedule
 */
@Component
public class TaskSchedule {

    @Autowired
    private MessageManager messageManager;

    /**
     * task manager
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void taskManager() {
        messageManager.taskManager();
    }
}
