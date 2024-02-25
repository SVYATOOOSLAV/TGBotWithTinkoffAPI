package edu.java.bot.action;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String commandName();
    String description();
    SendMessage handle(Long chatID, String text);
    default BotCommand getBotCommand(){
        return new BotCommand(commandName(), description());
    }
}
