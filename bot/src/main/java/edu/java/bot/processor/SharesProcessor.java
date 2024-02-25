package edu.java.bot.processor;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.action.Command;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SharesProcessor implements InstrumentProcessor {

    private final String commandNotFound = "Command not found, send /help";
    @Getter
    private final List<Command> commands;
    @Autowired
    public SharesProcessor(List<Command> commands) {
        this.commands = commands;
    }

    public SendMessage process(Long userID, String text) {
        for(Command command : commands){
            if(text.startsWith(command.commandName())){
                return command.handle(userID, text);
            }
        }
        return new SendMessage(userID, commandNotFound);
    }
}
