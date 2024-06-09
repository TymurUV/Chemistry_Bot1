package com.example.mappers;

import com.example.domain.UserChatInfo;
import com.example.dto.SupportMessageDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserChatInfoMapper {

    public static List<UserChatInfo> transform(List<SupportMessageDto> supportMessageDtos) {
        return supportMessageDtos.stream().map(a -> new UserChatInfo(a.getChatId(), a.getNickName())).collect(Collectors.toList());
    }
}
