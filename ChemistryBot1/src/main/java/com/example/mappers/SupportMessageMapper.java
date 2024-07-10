package com.example.mappers;

import com.example.domain.Status;
import com.example.domain.SupportMessage;
import com.example.domain.UserChatInfo;
import com.example.dto.SupportMessageDto;
import org.springframework.stereotype.Component;

@Component
public class SupportMessageMapper {
    public SupportMessageDto toDto(SupportMessage supportMessage) {
        SupportMessageDto supportMessageDto = new SupportMessageDto();
        supportMessageDto.setId(supportMessage.getId());
        supportMessageDto.setMessage(supportMessage.getMessage());
        supportMessageDto.setNickName(supportMessage.getNickname());
        supportMessageDto.setChatId(supportMessage.getChatId());
        supportMessageDto.setDate(supportMessage.getDate());
        supportMessageDto.setStatus(String.valueOf(supportMessage.getStatus()));
        return supportMessageDto;
    }

    public SupportMessage toEntity(SupportMessageDto supportMessageDto) {
        SupportMessage supportMessage = new SupportMessage();
        supportMessage.setId(supportMessageDto.getId());
        supportMessage.setNickname(supportMessageDto.getNickName());
        supportMessage.setMessage(supportMessageDto.getMessage());
        supportMessage.setChatId(supportMessageDto.getChatId());
        supportMessage.setDate(supportMessageDto.getDate());
        supportMessage.setStatus(Status.valueOf(supportMessageDto.getStatus()));
        return supportMessage;
    }

    public static UserChatInfo toUserChatInfo(SupportMessageDto supportMessageDto) {
        UserChatInfo userChatInfo = new UserChatInfo();
        userChatInfo.setChatId(supportMessageDto.getChatId());
        userChatInfo.setNickname(supportMessageDto.getNickName());
        return userChatInfo;

    }
//    supportMessageDtos.stream().map(a -> new UserChatInfo(a.getChatId(), a.getNickName())).collect(Collectors.toList());
}
