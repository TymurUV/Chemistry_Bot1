package com.example.mappers;

import com.example.domain.User;
import com.example.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setChatId(user.getChatId());
        return userDto;
    }
    public User toEntity(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setChatId(user.getChatId());
        return user;
    }

    }

