package com.example.service.impl;

import com.example.domain.User;
import com.example.domain.UserStatus;
import com.example.dto.UserDto;
import com.example.mappers.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new RuntimeException("User is not with " + id);
        }
        user.setName(userDto.getName());
        user.setChatId(userDto.getChatId());
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Not found by id " + id)
                )
        );
    }

    @Override
    public UserDto updateStatusByChatId(Long chatId, UserStatus status) {
        Optional<UserDto> userByChatId = findUserByChatId(chatId);
        if (userByChatId.isPresent()) {
            UserDto userDto = userByChatId.get();
            userDto.setStatus(status);
            return userDto;
        }
        return null;
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUserById(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> findUserByChatId(Long chatId) {
        Optional<User> userByChatId = userRepository.getUserByChatId(chatId);
        return userByChatId.stream()
                .map(userMapper::toDto)
                .findFirst();
    }
}
