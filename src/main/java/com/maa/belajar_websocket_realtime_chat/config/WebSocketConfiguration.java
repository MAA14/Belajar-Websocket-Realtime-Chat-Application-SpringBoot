package com.maa.belajar_websocket_realtime_chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // Ini untuk menentukan endpoint Websocketnya mau diopen di path apa
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket spring make SockJS | pathnya bisa "ws" atau "wss" kalo make https
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // Ini api aplikasi kita
        registry.enableSimpleBroker("/topic"); // Ini path di dalam api yang ingin dijadikan real-time
    }
}

/**
 * Alur Code WebSocket diatas
 *
 * 1. addEndPoint("/ws") -> ini adalah letak path untuk membuat Handshake dengan WebSocket
 * 2. setApplicationDestinationPrefixes("/app") -> ini adalah path awal api yang akan dihubungkan dengan WebSocket kita
 * 3. enableSimpleBroker("/topic") -> ini adalah path yang diizinkan untuk menerima dan mengirim data menggunakan metode WebSocket
 * 4. Jadi path akhir yang berhubungan dengan WebSocket adalah "/api/topic"
 */
