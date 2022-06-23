package com.team7.matadorenbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.matadorenbackend.appuser.AppUser;
import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.appuser.roles.RoleService;
import com.team7.matadorenbackend.websockets.web.WebSocketMethods;
import com.team7.matadorenbackend.websockets.model.ChatMessage;
import com.team7.matadorenbackend.websockets.model.MessageType;
import lombok.AllArgsConstructor;
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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class MatadorenBackendApplication extends WebSocketServer {

    private final static Logger logger = LogManager.getLogger(MatadorenBackendApplication.class);

    private final Set<WebSocket> connections;

    public MatadorenBackendApplication() throws UnknownHostException {

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
            WebSocketMethods.removeUser(connection);
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
                    WebSocketMethods.addUser(new AppUser(msg.getSender()), connection);
                    break;
                case DISCONNECT:
                    WebSocketMethods.removeUser(connection);
                    break;
                case CHAT:
                    WebSocketMethods.broadcastMessage(msg);
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
}
