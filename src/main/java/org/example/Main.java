package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            // Инициализируем API
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Регистрируем нашего бота
            botsApi.registerBot(new MyEchoBot());

            System.out.println("Бот успешно запущен и готов к работе!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}