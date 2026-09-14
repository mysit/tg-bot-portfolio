package org.example;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        com.sun.net.httpserver.HttpServer server = com.sun.net.httpserver.HttpServer.create(
                new java.net.InetSocketAddress(Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"))), 0
        );
        server.start();
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