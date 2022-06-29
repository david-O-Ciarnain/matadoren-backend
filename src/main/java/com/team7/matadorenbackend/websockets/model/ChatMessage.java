package com.team7.matadorenbackend.websockets.model;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Status status;
}
