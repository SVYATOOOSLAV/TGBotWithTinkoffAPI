package edu.java.bot.processor;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.action.Command;
import java.util.List;

public interface InstrumentProcessor {
    List<Command> getCommands();
    SendMessage process(Long userID, String text);
}
