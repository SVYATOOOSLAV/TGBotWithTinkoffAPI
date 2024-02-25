package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.action.Command;
import edu.java.bot.action.base.HelpCommand;
import edu.java.bot.action.base.StartCommand;
import edu.java.bot.action.finders.FindByName;
import edu.java.bot.action.finders.FindByTicker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.MarketDataService;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotConfig {
    @Bean
    TelegramBot telegramBot(ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }

    @Bean
    InvestApi investApi(ApplicationConfig applicationConfig) {
        return InvestApi.createReadonly(applicationConfig.tinkoffToken());
    }

    @Bean
    InstrumentsService instrumentsService(InvestApi investApi) {
        return investApi.getInstrumentsService();
    }

    @Bean
    MarketDataService marketDataService(InvestApi investApi) {
        return investApi.getMarketDataService();
    }

    @Bean
    List<Command> commands(
        StartCommand startCommand,
        HelpCommand helpCommand,
        FindByName commandName,
        FindByTicker commandTicker
    ) {
        return List.of(startCommand, helpCommand, commandName, commandTicker);
    }

    @Bean
    List<Share> tradableShares(InstrumentsService instrumentsService) {
        return instrumentsService.getTradableSharesSync();
    }
}
