package com.maa.belajar_websocket_realtime_chat.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * @IMPORTANT
 * pada konsep HTTP kita menggunakan @RestController dan metode Request -- Response
 * tapi pada konsep WebSocket kita menggunakan @Controller dan metode Message
 *
 * @MESSAGE?
 * Message adalah data yang dikirim dari user -> client dan sebaliknya dalam konsep WebSocket
 * seperti Request dan Response, Message juga punya Header
 *
 * @NOTE
 * Header pada Message dia akan terus ada dan tidak berubah-rubah meski kita mengirim pesan berbeda-beda, kecuali jika kita ubah sendiri
 * Header pada Message disimpan disisi Server yang artinya akan hilang jika koneksi WebSocket berakhir
 * Header pada Message seperti Cookies tapi disimpan di Server
 * Header pada Message dapat digunakan untuk menyimpan data-data yang sering digunakan tapi jarang berubah
 */

@Controller // Ini kayak @RestController tapi default outputnya bukan JSON
public class ChatController {

    @MessageMapping("/chat.sendMessage") // Ini path kalo kita mau make method sendMessage()
    @SendTo("/topic/public") // Ini path kemana messagenya akan dikirim
    public ChatMessage sendMessage(
            /**
             * @Payload mirip kayak @ResponseBody tapi khusus Messaging
             * @Payload berfungsi untuk menjaga Struktur pesan dalam bentuk Object Java
             */
            @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser") // Ini path kalo kita mau make method sendMessage()
    @SendTo("/topic/public") // Ini path kemana messagenya akan dikirim
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor // Ini adalah class untuk mengakses Header suatu Message
    ) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender()); // Simpan username kedalam Header Message
        return chatMessage;
    }
}
