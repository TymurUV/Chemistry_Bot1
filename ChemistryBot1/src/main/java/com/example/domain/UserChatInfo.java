package com.example.domain;

import lombok.Data;


@Data
public class UserChatInfo {
    private final String USER = "User";
    private Long chatId;
    private String nickname;

    @Override
    public String toString() {
        return USER + ": " + nickname + " " + chatId;
    }
}
