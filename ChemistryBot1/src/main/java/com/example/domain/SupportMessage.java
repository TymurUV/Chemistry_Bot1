package com.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "support_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "message")
    private String message;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "chatId")
    private Long chatId;
    @Column(name = "date")
    private Date date;
    @Enumerated(EnumType.STRING)
    private MsgStatus msgStatus;
}
