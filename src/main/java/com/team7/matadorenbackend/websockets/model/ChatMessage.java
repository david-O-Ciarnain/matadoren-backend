package com.team7.matadorenbackend.websockets.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage implements Serializable {

    @Getter
    @Setter
    public MessageType type;

    @Getter
    @Setter
    public String content;

    @Getter
    @Setter
    public String sender;

    @Getter
    public String time;

    public ChatMessage(MessageType type, String content, String sender, String time) {
        this.type = type;
        this.content = content;
        this.sender = sender;
        this.time = time;
    }

    public ChatMessage() {
    }
}


