package com.team7.matadorenbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleService;
import com.team7.matadorenbackend.websockets.model.ChatMessage;
import com.team7.matadorenbackend.websockets.model.MessageType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class MatadorenBackendApplication extends WebSocketServer {

    private final static Logger logger = LogManager.getLogger(MatadorenBackendApplication.class);

    private HashMap<WebSocket, AppUser> users;

    private Set<WebSocket> connections;

    private MatadorenBackendApplication(int port) {
        super(new InetSocketAddress(port));
        connections = new HashSet<>();
        users = new HashMap<>();
    }


    public MatadorenBackendApplication(InetSocketAddress address) {
        super(address);
    }

    public static void main(String[] args) {
        SpringApplication.run(MatadorenBackendApplication.class, args);
        int port;
        try {
            port = Integer.parseInt(System.getenv("PORT"));
        } catch (NumberFormatException nfe) {
            port = 9000;
        }
        new MatadorenBackendApplication(port).start();

    }

    @Bean
    CommandLineRunner run(AppUserService appUserService, RoleService roleService){

        return args -> {
            roleService.saveRoles();
            appUserService.signUpAppUser(new AppUser("demo","demo","admin","demo@live.se","123",new ArrayList<>()));
            appUserService.signUpAppUser(new AppUser("user","user","user","demo951@live.se","321",new ArrayList<>()));

            appUserService.addRoleToAppUSer("user","USER");
            appUserService.addRoleToAppUSer("admin","ADMIN");
        };
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    connections.add(webSocket);

    logger.info("Connetion established from: " + webSocket.getRemoteSocketAddress().getHostString());
    System.out.println("New connection from " + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
    connections.remove(connection);
    //When connection is closed, remove the user.
        try {
            removeUser(connection);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        logger.info("Connection closed to: " + connection.getRemoteSocketAddress().getHostString());
        System.out.println("Closed connection to " + connection.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onMessage(WebSocket connection, String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ChatMessage msg = mapper.readValue(message, ChatMessage.class);

            switch (msg.getType()) {
                case CONNECT:
                    addUser(new AppUser("", "", msg.getSender(), "", "",  ), connection);
                    break;
                case DISCONNECT:
                    removeUser(connection);
                    break;
                case CHAT:
                    broadcastMessage(msg);
            }

            System.out.println("Message from user: " + msg.getSender() + ", text: " + msg.getContent() + ", type:" + msg.getType());
            logger.info("Message from user: " + msg.getSender() + ", text: " + msg.getContent());
        } catch (IOException e) {
            logger.error("Wrong message format.");
            // return error message to user
        }
    }

    @Override
    public void onError(WebSocket connection, Exception ex) {

        if (connection != null) {
            connections.remove(connection);
        }
        assert connection != null;
        System.out.println("ERROR from " + connection.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    private void broadcastMessage(ChatMessage msg) {
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

    private void addUser(AppUser user, WebSocket conn) throws JsonProcessingException {
        users.put(conn, user);
        acknowledgeUserJoined(user, conn);
        broadcastUserActivityMessage(MessageType.CONNECT);
    }

    private void removeUser(WebSocket conn) throws JsonProcessingException {
        users.remove(conn);
        broadcastUserActivityMessage(MessageType.DISCONNECT);
    }

    private void acknowledgeUserJoined(AppUser user, WebSocket conn) throws JsonProcessingException {
        ChatMessage message = new ChatMessage();
        message.setType(MessageType.CONNECT_ACK);
        message.setSender(user.getUsername());
        conn.send(new ObjectMapper().writeValueAsString(message));
    }

    private void broadcastUserActivityMessage(MessageType messageType) throws JsonProcessingException {

        ChatMessage newMessage = new ChatMessage();

        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(users.values());
        newMessage.setContent(data);
        newMessage.setType(messageType);
        broadcastMessage(newMessage);
    }
}
