package com.example.domain;

public class UserChatInfo {
    private static final String buttonPressed = "button_pressed";
    private Long chatId;
    private String nickname;

    public UserChatInfo(Long chatId, String nickname) {
        this.chatId = chatId;
        this.nickname = nickname;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserChatInfo{" +
                "chatId=" + chatId +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
