package com.example.domain;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChatInfo {
    private final String buttonPressed = "the button has been pressed";
    private Long chatId;
    private String nickname;

}
