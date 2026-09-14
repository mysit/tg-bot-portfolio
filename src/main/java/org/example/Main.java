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

            System.out.println("The bot has been successfully launched and is ready to operate!\n" +
                    "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}