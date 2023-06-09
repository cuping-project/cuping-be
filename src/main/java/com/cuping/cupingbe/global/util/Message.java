package com.cuping.cupingbe.global.util;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Message {
    private String message;
    private Object data;


    public static Message setSuccess(String message, Object data) {
        return new Message(message, data);
    }

    public Message(String message) {
        this.message = message;
    }


}
