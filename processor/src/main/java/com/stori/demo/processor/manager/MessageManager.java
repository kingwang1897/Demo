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
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageManager {
    protected final Logger logger = LoggerFactory.getLogger(MessageManager.class);

    private Map<String, MessageLifecycle> concurrentHashMap = new ConcurrentHashMap<String, MessageLifecycle>();

    private MessageProcessThread messageProcessThread;

    @Autowired
    private MessageParseServiceImpl messageParseService;

    @Autowired
    private MessageHandleServiceImpl messageHandleService;

    @Autowired
    private MessageResponseServiceImpl messageResponseService;

    @Autowired
    private MessageSendServiceImpl messageSendService;

    public void init() {
        messageProcessThread = new MessageProcessThread(messageParseService,
                messageHandleService, messageResponseService, messageSendService, concurrentHashMap);
        messageProcessThread.start();
    }

    public void addMessage(MessageLifecycle messageLifecycle) {
        if (concurrentHashMap.containsKey(messageLifecycle.getMessageId())) {
            logger.warn("MessageManager addMessage warn, {} is exist.", messageLifecycle.getMessageId());
            return;
        }

        messageLifecycle.setStatus(MessageStatus.PREPARSE);
        messageLifecycle.setCallCount(Constant.MESSAGE_CALL_INIT);
        concurrentHashMap.put(messageLifecycle.getMessageId(), messageLifecycle);
        if (messageProcessThread == null) {
            init();
        }
    }
}
