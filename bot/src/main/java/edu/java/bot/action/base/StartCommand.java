package edu.java.bot.action.base;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.action.Command;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
    String welcomeText = "I try to help you if want to find more info about company";
    @Override
    public String commandName() {
        return "/start";
    }

    @Override
    public String description() {
        return "what you need to do";
    }

    @Override
    public SendMessage handle(Long chatID, String text) {
        return new SendMessage(chatID, welcomeText);
    }
}
