package com.example.service;

import com.example.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    UserDto updateUserById(Long id, UserDto userDto);

    UserDto getUserById(Long id);

    Long getUserChatId(Long chatId);

    List<UserDto> getAllUsers();

    void deleteUserById(Long id);

    Optional<UserDto> findUserByChatId(Long chatId);
}
