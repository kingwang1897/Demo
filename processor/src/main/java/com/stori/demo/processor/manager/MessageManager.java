package com.stori.demo.processor.manager;

import com.stori.demo.processor.constant.Constant;
import com.stori.demo.processor.constant.MessageStatus;
import com.stori.demo.processor.impl.MessageHandleServiceImpl;
import com.stori.demo.processor.impl.MessageParseServiceImpl;
import com.stori.demo.processor.impl.MessageResponseServiceImpl;
import com.stori.demo.processor.impl.MessageSendServiceImpl;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.thread.MessageProcessThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MessageManager {
    protected final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    private static final int CORE_POOL_SIZE = 3;
    private static final int MAX_POOL_SIZE = 5;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;

    private Map<String, MessageLifecycle> concurrentHashMap = new ConcurrentHashMap<String, MessageLifecycle>();

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy());;

    @Autowired
    private MessageParseServiceImpl messageParseService;

    @Autowired
    private MessageHandleServiceImpl messageHandleService;

    @Autowired
    private MessageResponseServiceImpl messageResponseService;

    @Autowired
    private MessageSendServiceImpl messageSendService;

    /**
     * accept request
     *
     * @param messageLifecycle
     */
    public void addMessage(MessageLifecycle messageLifecycle) {
        if (concurrentHashMap.containsKey(messageLifecycle.getMessageId())) {
            logger.warn("MessageManager addMessage warn, {} is exist.", messageLifecycle.getMessageId());
            return;
        }

        messageLifecycle.setStatus(MessageStatus.PREPARSE);
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        concurrentHashMap.put(messageLifecycle.getMessageId(), messageLifecycle);
    }

    /**
     * task status check and execute
     */
    public void taskManager() {
        if (concurrentHashMap.isEmpty()) {
            return;
        }

        for (Map.Entry<String, MessageLifecycle> entry : concurrentHashMap.entrySet()) {
            if (entry.getValue().getStatus().equals(MessageStatus.DONE)) {
                concurrentHashMap.remove(entry.getKey());
                continue;
            }

            MessageProcessThread messageProcessThread = new MessageProcessThread(messageParseService, messageHandleService, messageResponseService,
                    messageSendService, entry.getValue());
            executor.execute(messageProcessThread);
        }
    }
}
