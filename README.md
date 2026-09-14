##Telegram Bot: User Directory & Tech Stack Explorer

Практический проект Telegram-бота для просмотра базы пользователей и их стека технологий с поддержкой пагинации и динамического обновления интерфейса.

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![TelegramBots](https://img.shields.io/badge/TelegramBots-6.x-blue.svg)](https://github.com/rubenlagus/TelegramBots)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-JDBC-blue.svg)](https://www.postgresql.org/)

## Технологический стек

* **Language:** Java 17+
* **Framework / API:** TelegramBots API (Long Polling)
* **Database:** PostgreSQL / MySQL (JDBC, PreparedStatements)
* **Infrastructure:** Embedded Sun HttpServer (для Keep-Alive на облачных хостингах вроде Render/Koyeb)

## Реализованный функционал

* **Динамическая пагинация:** Постраничный вывод списков пользователей через `InlineKeyboardMarkup` без перегрузки чата (изменение существующих сообщений через `EditMessageText`).
* **Безопасность SQL:** Использование `PreparedStatement` для защиты от SQL-инъекций.
* **Связи в БД (JOINs):** Запросы с `LEFT JOIN` к трем таблицам (пользователи, связи, языки) для сборки сводного профиля.
* **Облачный деплой:** Встроенный HTTP-сервер для прохождения Health Check на бесплатных хостинг-платформах.

## 🗄 Структура Базы Данных

Бот работает со следующей связью таблиц (Many-to-Many):

`app (users)` ───< `language_app` >─── `languages`

## 🚀 Инструкция по запуску

### 1. Переменные окружения (Environment Variables)

Создайте необходимые переменные окружения на вашем сервере или в IDE:

| Переменная | Описание | Пример |
| :--- | :--- | :--- |
| `BOT_USERNAME` | Имя пользователя бота | `goodkodBot` |
| `BOT_TOKEN` | Токен из @BotFather | `8622777209:AAE...` |
| `DB_URL` | URL подключения JDBC | `jdbc:postgresql://localhost:5432/mydb` |
| `DB_USER` | Пользователь БД | `postgres` |
| `DB_PASSWORD` | Пароль БД | `secret` |
| `PORT` | Порт HTTP-сервера | `8080` |

### 2. Сборка и запуск

```bash
# Клонировать репозиторий
git clone [https://github.com/ваш-логин/ваш-репозиторий.git](https://github.com/ваш-логин/ваш-репозиторий.git)

# Собрать и запустить проект
mvn clean package
java -jar target/your-bot-jar-with-dependencies.jar
