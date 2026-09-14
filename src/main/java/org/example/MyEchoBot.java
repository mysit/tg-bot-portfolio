package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyEchoBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "goodkodBot";
    }

    @Override
    public String getBotToken() {
        return System.getenv("DB_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("Select a user:");

                InlineKeyboardMarkup markupInline = buildKeyboardForPage(0);
                message.setReplyMarkup(markupInline);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } 
        else if (update.hasCallbackQuery()) {
            String callbackQueryId = update.getCallbackQuery().getId();
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();

            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackQueryId);
            try {
                execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            if (callData.startsWith("page_")) {
                int page = Integer.parseInt(callData.replace("page_", ""));
                InlineKeyboardMarkup markupInline = buildKeyboardForPage(page);

                EditMessageText editMessage = new EditMessageText();
                editMessage.setChatId(String.valueOf(chatId));
                editMessage.setMessageId(messageId);
                editMessage.setText("Select a user:");
                editMessage.setReplyMarkup(markupInline);

                try {
                    execute(editMessage);
                } catch (TelegramApiException e) {
                    if (e.getMessage() == null || !e.getMessage().contains("message is not modified")) {
                        e.printStackTrace();
                    }
                }
            } 
            else {
                String dbResponse = DatabaseManager.getUserWithLangs(callData);

                InlineKeyboardMarkup backMarkup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                List<InlineKeyboardButton> rowInline = new ArrayList<>();

                InlineKeyboardButton back = new InlineKeyboardButton();
                back.setText("Return to search");
                back.setCallbackData("page_0");

                rowInline.add(back);
                rowsInline.add(rowInline);
                backMarkup.setKeyboard(rowsInline);

                EditMessageText editMessage = new EditMessageText();
                editMessage.setChatId(String.valueOf(chatId));
                editMessage.setMessageId(messageId);
                editMessage.setText(dbResponse);
                editMessage.setReplyMarkup(backMarkup);

                try {
                    execute(editMessage);
                } catch (TelegramApiException e) {
                    if (e.getMessage() == null || !e.getMessage().contains("message is not modified")) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private InlineKeyboardMarkup buildKeyboardForPage(int page) {
        Map<String, String> usersMap = DatabaseManager.getAllUsers();
        List<Map.Entry<String, String>> userList = new ArrayList<>(usersMap.entrySet());

        int pageSize = 5;
        int start = page * pageSize;
        int end = Math.min(start + pageSize, userList.size());

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (int i = start; i < end; i++) {
            Map.Entry<String, String> entry = userList.get(i);
            String login = entry.getKey();
            String name = entry.getValue();

            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton btn = new InlineKeyboardButton();
            btn.setText(name);
            btn.setCallbackData(login);

            rowInline.add(btn);
            rowsInline.add(rowInline);
        }

        List<InlineKeyboardButton> navRow = new ArrayList<>();
        if (page > 0) {
            InlineKeyboardButton prev = new InlineKeyboardButton();
            prev.setText("<");
            prev.setCallbackData("page_" + (page - 1));
            navRow.add(prev);
        }
        if (end < userList.size()) {
            InlineKeyboardButton next = new InlineKeyboardButton();
            next.setText(">");
            next.setCallbackData("page_" + (page + 1));
            navRow.add(next);
        }
        if (!navRow.isEmpty()) {
            rowsInline.add(navRow);
        }

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
