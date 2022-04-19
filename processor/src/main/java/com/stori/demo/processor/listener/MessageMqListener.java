package com.stori.demo.processor.listener;

import com.stori.demo.processor.manager.MessageManager;
import com.stori.demo.processor.model.MessageLifecycle;
import com.stori.demo.processor.core.service.MessageProcessService;
import com.stori.demo.processor.model.StoriMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageMqListener implements MessageProcessService {

    @Autowired
    private MessageManager messageManager;

    /**
     * 解析、校验请求报文并生成响应报文
     *
     * @param request 请求报文
     * @return {@link  StoriMessage} 响应报文
     */
    @Override
    public StoriMessage createResponse(StoriMessage request) {
        // step 2: put message to processor
        MessageLifecycle messageLifecycle = new MessageLifecycle(request.getSocketId(), request.getMessageChannel(), request.getOriginMessage());
        messageManager.addMessage(messageLifecycle);

        StoriMessage storiMessage = new StoriMessage();
        storiMessage.setSocketId(request.getSocketId());
        storiMessage.setMessageChannel(request.getMessageChannel());
        return storiMessage;
    }
}
