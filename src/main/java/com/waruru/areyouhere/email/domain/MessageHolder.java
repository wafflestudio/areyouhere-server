package com.waruru.areyouhere.email.domain;

import lombok.Getter;

@Getter
public class MessageHolder {
    private String title;

    private String contents;

    public MessageHolder(String title, MessageTemplate messageTemplate, Object... values){
        this.title = title;
        this.contents = String.format(messageTemplate.getTemplate(), (Object) values);
    }
}
