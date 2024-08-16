package com.example;

import com.example.dto.SupportMessageDto;
import com.example.dto.UserDto;
import com.example.mappers.SupportMessageMapper;
import com.example.repository.SupportMessageRepository;
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
    private final Map<Long, Long> responseCheck = new HashMap<>();
    private final Map<Long, String> helpRequests = new HashMap<>();

    @Autowired
    public MyBot(UserService userService, ChemElementService chemElementService, SupportMessageService supportMessageService, SupportMessageRepository supRep, SupportMessageMapper supportMessageMapper) {
        this.userService = userService;
        this.chemElementService = chemElementService;
        this.supportService = supportMessageService;

    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleIncomingMessage(update);
        }

        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleIncomingMessage(Update update) {
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();

        switch (message) {
            case "/start" -> sendWelcomeMessage(chatId);
            case "/help" -> getHelp(chatId);
            case "/sup" -> {
                if (isUserAdmin(chatId)) {
                    getSupportMsgToAdmin(chatId);
                }
            }
            default -> processUserMessage(chatId, message, update.getMessage().getFrom().getUserName());
        }
    }

    private void sendWelcomeMessage(Long chatId) {
        String welcomeText = "Welcome to the world of chemistry! \uD83E\uDDEA\uD83D\uDD2C\n" +
                "Here you will find lots of fascinating facts, interesting experiments and useful knowledge about chemical elements.\n" +
                "Welcome to our chemistry lab! \uD83D\uDCA1✨";
        sendMsgToUser(chatId, welcomeText);
        sendMsgToUser(chatId, "Select ", List.of("Register"), 1);
    }

    private void processUserMessage(Long chatId, String message, String username) {
        if (userCheck.containsKey(chatId) && userCheck.get(chatId)) {
            saveOrUpdateSupMsg(chatId, message, username);
            userCheck.put(chatId, false);
        } else if (responseCheck.containsKey(chatId) && !responseCheck.get(chatId).equals(0L)) {
            sendMsgToUser(responseCheck.get(chatId), message);
            sendMsgToUser(chatId, "Select ", List.of("✔️", "❌"), 1);
            responseCheck.put(chatId, 0L);
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callBackData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        switch (callBackData) {
            case "Register" ->
                    registerUser(chatId, callbackQuery.getFrom().getFirstName(), callbackQuery.getFrom().getUserName());
            case "Update Message" -> promptUserForSupportMessage(chatId);
            case "✔️" -> deleteSupportMessage(chatId);
            case "❌" -> notifyAdminAboutUser(chatId);
            default -> {
                if (callBackData.startsWith("Support")) {
                    handleSupportCallback(chatId);
                } else if (callBackData.startsWith("User")) {
                    handleUserResponseCallback(chatId, callBackData);
                }
            }
        }
    }

    private void handleSupportCallback(Long chatId) {
        if (!isUserWriteSupportMessage(chatId)) {
            promptUserForSupportMessage(chatId);
        } else {
            String existingMessage = supportService.findMessageByChatId(chatId).get().getMessage();
            sendMsgToUser(chatId, "You already have message sent to admin: " + existingMessage + "\nDo you want to update it?");
            sendMsgToUser(chatId, "Select ", List.of("Update Message", "Let it be"), 1);
        }
    }

    private void handleUserResponseCallback(Long chatId, String callBackData) {
        Long userChatIdForResponse = Long.valueOf(callBackData.replaceAll("\\D", ""));
        responseCheck.put(chatId, userChatIdForResponse);
        String userMessage = supportService.findMessageByChatId(userChatIdForResponse).get().getMessage();
        sendMsgToUser(chatId, "Write response to message: " + userMessage);
    }

    private void deleteSupportMessage(Long chatId) {
        supportService.deleteMessage(supportService.findMessageByChatId(chatId).get().getId());
    }


    private void notifyAdminAboutUser(Long chatId) {
        String nickname = userService.findUserByChatId(chatId).get().getNickname();
        String message = supportService.findMessageByChatId(chatId).get().getMessage();
        sendMsgToUser(1052235587L, "User with nickname: @" + nickname + "\nMessage: " + message);
    }

    private void promptUserForSupportMessage(Long chatId) {
        userCheck.put(chatId, true);
        sendMsgToUser(chatId, "Write down your message for admin");
    }


    private boolean isUserExist(Long chatId) {
        Optional<UserDto> user = userService.findUserByChatId(chatId);
        return user.isPresent();
    }


    private void sendMsgToUser(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMsgToUser(Long chatId, String message, List<String> textButton, int rows) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        InlineKeyboardMarkup inlineKeyboardMarkup = createCustomKeyboard(textButton, rows);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

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
            sendMsgToUser(chatId, "You successfully registered");

        } else {
            sendMsgToUser(chatId, "You've already registered in this Bot");
        }
    }

    private void getHelp(Long chatId) {
        sendMsgToUser(chatId, "Hi! Here's a list of available commands: \n \n" +
                "/about - Learn more about the bot. \n " +
                "/experiments - Get a list of interesting chemical experiments. \n " +
                "/elements - Learn information about chemical elements. \n " +
                "/reactions - Learn about different chemical reactions. \n " +
                "/search [term] - Find information on a specific chemical term. \n \n " +
                "\uD83D\uDD2C\uD83D\uDCA1\uD83C\uDF1F If you have any questions or queries, feel free to get in touch!");
        sendMsgToUser(chatId, "Select ", List.of("Support \uD83D\uDC68\uD83C\uDFFF\u200D\uD83D\uDCBB"), 1);

    }

    @Transactional
    private void saveOrUpdateSupMsg(Long chatId, String message, String nickname) {
        if (supportService.findMessageByChatId(chatId).isEmpty()) {
            SupportMessageDto supportMessageDto = new SupportMessageDto();
            supportMessageDto.setMessage(message);
            supportMessageDto.setDate(new Date());
            supportMessageDto.setStatus("WAIT_FOR_REPLY");
            supportMessageDto.setNickName(nickname);
            supportMessageDto.setChatId(chatId);
            supportService.saveSupportMessage(supportMessageDto);
        } else {
            SupportMessageDto supportMessageDto = supportService.findMessageByChatId(chatId).get();
            supportMessageDto.setMessage(message);
            supportMessageDto.setDate(new Date());
            supportService.updateMessage(supportMessageDto, supportMessageDto.getId());
        }
        sendMsgToUser(chatId, "your message successfully sent to Admin and you may proceed to further using our bot");
    }

    private boolean isUserWriteSupportMessage(Long chatId) {
        Optional<SupportMessageDto> messageByChatId = supportService.findMessageByChatId(chatId);
        return messageByChatId.isPresent();
    }


    private void getSupportMsgToAdmin(Long chatId) {
        List<SupportMessageDto> supportMessages = supportService.getAllMessages();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Support Messages: \n");
        for (SupportMessageDto supportMessageDto : supportMessages) {
            stringBuilder.append("≼----- Message Id: ").append(supportMessageDto.getId()).append(" -----≽").append("\n")
                    .append("Message Context: ").append(supportMessageDto.getMessage()).append("\n")
                    .append("Nickname: ").append(supportMessageDto.getNickName()).append("\n")
                    .append("Chat Id: ").append(supportMessageDto.getChatId()).append("\n")
                    .append("Status: ").append(supportMessageDto.getStatus()).append("\n")
                    .append("Date: ").append(supportMessageDto.getDate()).append("\n");

        }
        String builderString = stringBuilder.toString();
        sendMsgToUser(chatId, builderString);
        sendMsgToUser(chatId, "Select", supportMessages.stream().map(supMsg -> SupportMessageMapper.toUserChatInfo(supMsg).toString()).toList(), supportMessages.size());
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
