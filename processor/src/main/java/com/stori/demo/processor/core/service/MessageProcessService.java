package com.stori.demo.processor.core.service;

import com.stori.demo.processor.mode.StoriMessage;

/**
 * 交易报文处理服务
 */
public interface MessageProcessService {

    /**
     * 解析、校验请求报文并生成响应报文
     *
     * @param request 请求报文
     * @return {@link  StoriMessage} 响应报文
     */
    StoriMessage createResponse(StoriMessage request);
}
