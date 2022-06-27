package com.team7.matadorenbackend.websockets.endpoints;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/chat/{username}")
public class ChatEndpoint {

    @OnOpen
    public void onOpen(Session session) throws IOException {

    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
        // Handle new messages
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
