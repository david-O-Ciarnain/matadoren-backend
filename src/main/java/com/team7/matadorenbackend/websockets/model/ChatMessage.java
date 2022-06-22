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
    private MessageType type;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private String sender;

    @Getter
    private String time;
}
