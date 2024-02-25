package edu.java.bot.action.finders;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.action.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InstrumentsService;
import ru.tinkoff.piapi.core.MarketDataService;
import java.util.List;

@Component
public class FindByTicker implements Command {
    private final List<Share> tradableShares;
    private final MarketDataService marketDataService;
    private final InstrumentsService instrumentsService;
    private final String badRequest =
        """
        This is a bad request (you need write one company).
        Send me a message like this:
        /ticker SBER""";

    private final String companyNotFound = "Company not found or untradeable at this moment";
    @Autowired
    public FindByTicker(
        InstrumentsService instrumentsService,
        MarketDataService marketDataService,
        List<Share> tradableShares)
    {
        this.instrumentsService = instrumentsService;
        this.marketDataService = marketDataService;
        this.tradableShares = tradableShares;
    }

    @Override
    public String commandName() {
        return "/ticker";
    }

    @Override
    public String description() {
        return "find company with using ticker";
    }

    @Override
    public SendMessage handle(Long userID, String text) {
        String[] commandAndTicker = text.split("[\\s\\n]+");
        if(commandAndTicker.length == 1){
            return new SendMessage(userID, badRequest);
        }
        if(commandAndTicker.length > 2){
            return new SendMessage(userID, badRequest);
        }
        String figi = getFigiByTicker(commandAndTicker[1].toUpperCase());
        if(figi == null){
            return new SendMessage(userID, companyNotFound);
        }
        return new SendMessage(userID, getInfoAboutShare(figi));
    }

    private  String getFigiByTicker(String ticker){
        return tradableShares.stream()
            .filter(share -> share.getTicker().equals(ticker) && share.getCurrency().equals("rub"))
            .map(Share::getFigi)
            .findFirst().orElse(null);
    }

    private String getInfoAboutShare(String figi){
        StringBuilder res = new StringBuilder();
        var orderBook = marketDataService.getOrderBookSync(figi, 12);
        Share share = instrumentsService.getShareByFigiSync(figi);
        res.append("Тикер: ")
            .append(share.getTicker()).append(System.lineSeparator())
            .append("Isin: ")
            .append(share.getIsin()).append(System.lineSeparator())
            .append("Наименование: ")
            .append(share.getName()).append(System.lineSeparator())
            .append("Текущая цена: ")
            .append(Converter.convertUnitsAndNanos(orderBook.getLastPrice())).append(System.lineSeparator())
            .append(System.lineSeparator());
        return res.toString();
    }
}
