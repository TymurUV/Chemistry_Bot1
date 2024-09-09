package com.example.repository;

import com.example.domain.User;
import com.example.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
Optional<User> getUserByChatId(Long chatId);

}
