package com.example;

import com.example.dto.SupportMessageDto;
import com.example.dto.UserDto;
import com.example.service.ChemElementService;
import com.example.service.SupportMessageService;
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

import java.util.*;

@Component
@Slf4j
public class MyBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final ChemElementService chemElementService;
    private final SupportMessageService supportService;
    private final Map<Long, Boolean> userCheck = new HashMap<>();


    @Autowired
    public MyBot(UserService userService, ChemElementService chemElementService, SupportMessageService supportMessageService) {
        this.userService = userService;
        this.chemElementService = chemElementService;
        this.supportService = supportMessageService;
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
            } else if (message.equals("/help")) {
                getHelp(chatId);
            } else if (message.equals("/sup")) {
                getSupportMsgToAdmin(chatId);
            } else if (!message.isEmpty() && userCheck.containsKey(chatId)) {
                if (userCheck.get(chatId)) {
                    sendMsgToAdmin(chatId, message, update.getMessage().getFrom().getUserName());
                    userCheck.put(chatId, false);
                }
            }
        }
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callBackData = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();
            if (callBackData.equals("Register")) {
                registerUser(chatId, callbackQuery.getFrom().getFirstName(), callbackQuery.getFrom().getUserName());
            } else if (callBackData.startsWith("Support")) {
                userCheck.put(chatId, true);
                sendMsgToUser(chatId, "Write down your message for admin", null, 0);
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
            userService.saveUser(userDto);
            sendMsgToUser(chatId, "You successfully registered", null, 0);

        } else {
            sendMsgToUser(chatId, "You've already registered in this Bot", null, 0);
        }
    }

    private void getHelp(Long chatId) {
        sendMsgToUser(chatId, "Hi! Here's a list of available commands: \n \n" +
                        "/about - Learn more about the bot. \n " +
                        "/experiments - Get a list of interesting chemical experiments. \n " +
                        "/elements - Learn information about chemical elements. \n " +
                        "/reactions - Learn about different chemical reactions. \n " +
                        "/search [term] - Find information on a specific chemical term. \n \n " +
                        "\uD83D\uDD2C\uD83D\uDCA1\uD83C\uDF1F If you have any questions or queries, feel free to get in touch!",
                List.of("Support \uD83D\uDC68\uD83C\uDFFF\u200D\uD83D\uDCBB"), 1);

    }

    @Transactional
    private void sendMsgToAdmin(Long chatId, String message, String nickname) {
        SupportMessageDto supportMessageDto = new SupportMessageDto();
        supportMessageDto.setMessage(message);
        supportMessageDto.setDate(new Date());
        supportMessageDto.setStatus("WAIT_FOR_REPLY");
        supportMessageDto.setNickName(nickname);
        supportMessageDto.setChatId(chatId);

        supportService.saveSupportMessage(supportMessageDto);
        sendMsgToUser(chatId, "your message successfully sent to Admin and you may proceed to further using our bot", null, 0);
    }

    private void getSupportMsgToAdmin(Long chatId) {
        List<SupportMessageDto> supportMessages = supportService.getAllMessages();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Support Messages: \n");
        for (SupportMessageDto supportMessageDto : supportMessages) {
            stringBuilder.append("Message Id: ").append(supportMessageDto.getId()).append("\n")
                    .append("Message Context: ").append(supportMessageDto.getMessage()).append("\n")
                    .append("Nickname: ").append(supportMessageDto.getNickName()).append("\n")
                    .append("Chat Id: ").append(supportMessageDto.getChatId()).append("\n")
                    .append("Status: ").append(supportMessageDto.getStatus()).append("\n")
                    .append("Date: ").append(supportMessageDto.getDate()).append("\n")
                    .append("----------------------");

        }
        String builderString = stringBuilder.toString();
        sendMsgToUser(chatId, supportMessages.toString(), null, 0);


    }

    private boolean isUserAdmin(Long chatId) {
        Optional<UserDto> userByChatId = userService.findUserByChatId(chatId);
        return userByChatId.map(userDto -> userDto.getRole().equals("ADMIN")).orElse(false);
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
