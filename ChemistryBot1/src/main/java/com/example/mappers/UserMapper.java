package com.example.mappers;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setNickname(user.getNickname());
        userDto.setChatId(user.getChatId());
        userDto.setTempChatIdForReply(user.getTempChatIdForReply());
        userDto.setRole(String.valueOf(user.getRole()));
        userDto.setStatus(user.getStatus());
        userDto.setAdminStatus(user.getAdminStatus());
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setNickname(userDto.getNickname());
        user.setChatId(userDto.getChatId());
        user.setTempChatIdForReply(userDto.getTempChatIdForReply());
        user.setRole(Role.valueOf(userDto.getRole().toUpperCase()));
        user.setStatus(userDto.getStatus());
        user.setAdminStatus(userDto.getAdminStatus());
        return user;
    }

}

