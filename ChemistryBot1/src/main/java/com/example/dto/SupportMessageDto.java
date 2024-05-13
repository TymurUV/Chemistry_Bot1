package com.example.dto;

import lombok.Data;

import java.util.Date;


@Data
public class SupportMessageDto {
    private Long id;
    private String message;
    private String nickName;
    private Long chatId;
    private String status;
    private Date date;

}
