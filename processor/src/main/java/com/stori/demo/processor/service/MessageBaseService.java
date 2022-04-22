package com.stori.demo.processor.service;


import com.stori.demo.processor.model.MessageLifecycle;

public interface MessageBaseService {

    /**
     * excute message(parse/handle/response/send)
     *
     * @return
     */
    MessageLifecycle execute(MessageLifecycle messageLifecycle);
}
