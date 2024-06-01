package com.maa.belajar_websocket_realtime_chat.config;

import com.maa.belajar_websocket_realtime_chat.chat.ChatMessage;
import com.maa.belajar_websocket_realtime_chat.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageSendingOperations; // Untuk convert Object Java jadi Message

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event // SessionDisconnectEvent akan ketrigger setiap Koneksi dengan WebSocket berakhir
    ) {
        // Jika user saat ini koneksinya berakhir dengan WebSocket maka kirim pesan pemberitahuan
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username"); // megambil username User yang keluar

        if (username != null) {
            log.info("User disconnected: {}", username); // Lakukan info di console Server

            // Build message yang akan dikirim ke broadcast nantinya
            ChatMessage chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();

            // Menconvert Object Java menjadi bentuk Message pada WebSocket kemudian kirim ke Website pada path "/topic/public"
            messageSendingOperations.convertAndSend("/topic/public", chatMessage);

        }
    }
}
