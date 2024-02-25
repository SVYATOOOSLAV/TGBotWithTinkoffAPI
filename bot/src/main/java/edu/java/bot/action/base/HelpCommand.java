package edu.java.bot.action.base;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.action.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HelpCommand implements Command {

    private final List<Command> commands;
    @Autowired
    public HelpCommand(List<Command> commands){
        this.commands = commands;
    }
    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public String description() {
        return "List of commands";
    }

    @Override
    public SendMessage handle(Long chatID, String text) {
        StringBuilder res = new StringBuilder("List of existing commands:\n");

        commands.forEach(command -> {
            res.append(command.commandName())
                .append(": ")
                .append(command.description())
                .append(System.lineSeparator());
        });
        res.delete(res.length()-2, res.length());

        return new SendMessage(chatID, res.toString());
    }
}
