package com.team7.matadorenbackend.websockets.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.matadorenbackend.MatadorenBackendApplication;
import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.websockets.model.ChatMessage;
import com.team7.matadorenbackend.websockets.model.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Set;

public class WebSocketMethods {

    private final static Logger logger = LogManager.getLogger(MatadorenBackendApplication.class);

    private AppUser users = new HashMap<>()

    private final Set<WebSocket> connections;

    public WebSocketMethods(Set<WebSocket> connections) {
        this.connections = connections;
    }


    public static void broadcastMessage(ChatMessage msg) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String messageJson = mapper.writeValueAsString(msg);
            for (WebSocket sock : connections) {
                sock.send(messageJson);
            }
        } catch (JsonProcessingException e) {
            logger.error("Cannot convert message to json.");
        }
    }

    public static void addUser(AppUser user, WebSocket conn) throws JsonProcessingException {
        users.put(conn, user);
        acknowledgeUserJoined(user, conn);
        broadcastUserActivityMessage(MessageType.CONNECT);
    }

    public static void removeUser(WebSocket conn) throws JsonProcessingException {
        users.remove(conn);
        broadcastUserActivityMessage(MessageType.DISCONNECT);
    }

    public static void acknowledgeUserJoined(AppUser user, WebSocket conn) throws JsonProcessingException {
        ChatMessage message = new ChatMessage();
        message.setType(MessageType.CONNECT_ACK);
        message.setSender(user.getUsername());
        conn.send(new ObjectMapper().writeValueAsString(message));
    }

    public static void broadcastUserActivityMessage(MessageType messageType) throws JsonProcessingException {

        ChatMessage newMessage = new ChatMessage();

        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(users.values());
        newMessage.setContent(data);
        newMessage.setType(messageType);
        broadcastMessage(newMessage);
    }
}
