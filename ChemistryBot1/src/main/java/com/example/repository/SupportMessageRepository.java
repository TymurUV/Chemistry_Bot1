package com.example.repository;

import com.example.domain.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
    List<SupportMessage> getSupportMsgByChatId(Long chatId);
}
