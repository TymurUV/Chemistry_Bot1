package com.example;

import com.example.service.ChemElementService;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class MyBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final ChemElementService chemElementService;

    @Autowired
    public MyBot(UserService userService, ChemElementService chemElementService) {
        this.userService = userService;
        this.chemElementService = chemElementService;
    }


    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (message.equals("/start")) {
                sendMsgToUser(chatId, "Hello, how can I help you with chemistry today");
            }
        }
    }

    private void sendMsgToUser(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (Exception e) {
        }
    }

    @Override
    public String getBotUsername() {
        return "chem1234bot";
    }

    @Override
    public String getBotToken() {
        return "6733348678:AAE8dEHPJhWGBvO3Jut1iuozme4zWqdy4Hw";
    }
}
