package com.example.service;


import com.example.dto.SupportMessageDto;

import java.util.List;
import java.util.Optional;

public interface SupportMessageService {
    SupportMessageDto saveSupportMessage(SupportMessageDto supportMessageDto);

    List<SupportMessageDto> getAllMessages();

    void deleteMessage(Long id);

    SupportMessageDto updateMessage(SupportMessageDto supportMessageDto, Long id);
    Optional<SupportMessageDto> findMessageByChatId(Long chatId);

}
