package com.example.dto;

import com.example.domain.AdminStatus;
import com.example.domain.UserStatus;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String nickname;
    private Long chatId;
    private String role;
    private Long tempChatIdForReply;
    private UserStatus status;
    private AdminStatus adminStatus;
}
