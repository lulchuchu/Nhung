package com.example.demo.Service;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Quy định tất cả dữ liệu được gửi từ client xuống server đều phải bắt đầu với tiền tố là /app

        registry.enableSimpleBroker("/topic", "/user");
        // quy định rằng tiền tố /topic dùng để máy chủ gửi du lieu đến tất cả máy khách đang sub vào chủ đề này
        // còn /user để máy chủ gửi riêng đến 1 user nào đó
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
//                .withSockJS();
    }
}
