package com.alipay.sofa.config;


import com.alipay.sofa.entrance.socket.WebSocketMessageHandler;
import com.alipay.sofa.entrance.socket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setWebSocketMessageHandler(WebSocketMessageHandler webSocketMessageHandler) {
        WebSocketServer.webSocketMessageHandler = webSocketMessageHandler;
    }

}
