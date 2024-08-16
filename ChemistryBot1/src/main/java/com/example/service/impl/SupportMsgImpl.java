package com.example.service.impl;

import com.example.domain.MsgStatus;
import com.example.domain.SupportMessage;
import com.example.dto.SupportMessageDto;
import com.example.mappers.SupportMessageMapper;
import com.example.repository.SupportMessageRepository;
import com.example.service.SupportMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupportMsgImpl implements SupportMessageService {
    private SupportMessageRepository supRep;
    private SupportMessageMapper mapper;

    @Autowired
    public SupportMsgImpl(SupportMessageRepository supRep, SupportMessageMapper mapper) {
        this.supRep = supRep;
        this.mapper = mapper;
    }

    @Override
    public SupportMessageDto saveSupportMessage(SupportMessageDto supportMessageDto) {
        return mapper.toDto(supRep.save(mapper.toEntity(supportMessageDto)));
    }

    @Override
    public List<SupportMessageDto> getAllMessages() {
        List<SupportMessage> allMessages = supRep.findAll();

        return allMessages.stream()
                .map(mapper::toDto).toList();
    }

    @Override
    public void deleteMessage(Long id) {
        SupportMessage supportMessage = supRep.findById(id).get();
        supRep.delete(supportMessage);
    }

    @Override
    public SupportMessageDto updateMessage(SupportMessageDto supportMessageDto, Long id) {
        SupportMessage supportMessage = supRep.findById(id).get();
        supportMessage.setMessage(supportMessageDto.getMessage());
        supportMessage.setDate(supportMessageDto.getDate());
        supportMessage.setNickname(supportMessageDto.getNickName());
        supportMessage.setChatId(supportMessageDto.getChatId());
        supportMessage.setMsgStatus(MsgStatus.valueOf(supportMessageDto.getStatus()));
        return mapper.toDto(supRep.save(supportMessage));
    }

    @Override
    public Optional<SupportMessageDto> findMessageByChatId(Long chatId) {
        List<SupportMessage> msgChatId = supRep.getSupportMsgByChatId(chatId);
        return msgChatId.stream().map(mapper::toDto).findFirst();
    }
}



