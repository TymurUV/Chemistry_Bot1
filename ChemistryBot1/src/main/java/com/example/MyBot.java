package com.example;

import com.example.dto.UserDto;
import com.example.service.ChemElementService;
import com.example.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            if (message.equals("/start")) {
                sendMsgToUser(chatId, "Welcome to the world of chemistry! \uD83E\uDDEA\uD83D\uDD2C \n " +
                        "Here you will find lots of fascinating facts, interesting experiments and useful knowledge about chemical elements. \n " +
                        "Welcome to our chemistry lab! \uD83D\uDCA1✨", List.of("Register"), 1);
            }
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();
            if (callBackData.equals("Register")) {
                registerUser(chatId, callbackQuery.getFrom().getFirstName(), callbackQuery.getFrom().getUserName());
            }
        }
    }


    private boolean isUserExist(Long chatId) {
        Optional<UserDto> user = userService.findUserByChatId(chatId);
        return user.isPresent();
    }

    private void sendMsgToUser(Long chatId, String message, List<String> textButton, int rows) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        if (textButton != null) {
            InlineKeyboardMarkup inlineKeyboardMarkup = createCustomKeyboard(textButton, rows);
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }
        try {
            execute(sendMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    private void registerUser(Long chatId, String name, String nickname) {
        UserDto userDto = new UserDto();
        userDto.setChatId(chatId);
        userDto.setName(name);
        userDto.setNickname(nickname);
        userDto.setRole("USER");

        if (!isUserExist(chatId)) {
            UserDto saveUser = userService.saveUser(userDto);
            sendMsgToUser(chatId, "You successfully registered", null, 0);

        } else {
            sendMsgToUser(chatId, "You've alredy registered in this Bot", null, 0);
        }
    }


    private InlineKeyboardMarkup createCustomKeyboard(List<String> buttonText, int rows) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        int buttonsPerRow = buttonText.size() / rows + buttonText.size() % rows;
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (String s : buttonText) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(s);
            button.setCallbackData(s);

            row.add(button);
            if (row.size() == buttonsPerRow || button.equals(buttonText.get(buttonText.size() - 1))) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
        }

        if (buttonText.size() % rows == 1) {
            List<InlineKeyboardButton> buttonRow = new ArrayList<>();
            for (int i = 0; i < buttonText.size() % rows; i++) {
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(buttonText.get(buttonText.size() - buttonText.size() % rows + i));
                inlineKeyboardButton.setCallbackData(buttonText.get(buttonText.size() - buttonText.size() % rows + i));
                buttonRow.add(inlineKeyboardButton);
            }
        }

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
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
