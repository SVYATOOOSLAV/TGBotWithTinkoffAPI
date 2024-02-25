package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.action.Command;
import edu.java.bot.processor.InstrumentProcessor;
import edu.java.bot.processor.SharesProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Bot implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final InstrumentProcessor processor;
    @Autowired
    public Bot(TelegramBot telegramBot, InstrumentProcessor processor){
        this.telegramBot = telegramBot;
        this.processor = processor;
        telegramBot.execute(creteCommandMenu());
        telegramBot.setUpdatesListener(this);
    }

    private SetMyCommands creteCommandMenu(){
        return new SetMyCommands(
            processor.getCommands().stream().map(Command::getBotCommand).toArray(BotCommand[]::new)
        );
    }
    @Override
    public int process(List<Update> list) {
        for(Update update : list){
            Message message = update.message();
            if(message != null){
                SendMessage response = processor.process(message.chat().id(), message.text());
                telegramBot.execute(response);
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
