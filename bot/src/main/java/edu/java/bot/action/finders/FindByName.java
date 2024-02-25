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
public class FindByName implements Command {
    private final List<Share> tradableShares;
    private final MarketDataService marketDataService;
    private final InstrumentsService instrumentsService;
    private final String badRequest =
        """
        This is a bad request (you need write one company).
        Send me a message like this:
        /name Сбербанк""";

    private final String companyNotFound = "Company not found or untradeable at this moment";
    @Autowired
    public FindByName(
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
        return "/name";
    }

    @Override
    public String description() {
        return "find company with using name";
    }

    @Override
    public SendMessage handle(Long userID, String text) {
        String[] commandAndName = text.split("[\\s\\n]+");
        if(commandAndName.length == 1){
            return new SendMessage(userID, badRequest);
        }
        if(commandAndName.length > 2){
            return new SendMessage(userID, badRequest);
        }
        List<String> figis = getFigisByNameCompany(commandAndName[1].toLowerCase());
        if(figis.isEmpty()){
            return new SendMessage(userID, companyNotFound);
        }
        StringBuilder res = new StringBuilder();
        for (String figi : figis) {
            res.append(getInfoAboutShare(figi));
        }
        return new SendMessage(userID, res.toString());
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
    private  List<String> getFigisByNameCompany(String companyName){
        return tradableShares.stream()
            .filter(share -> share.getName().toLowerCase().contains(companyName) && share.getCurrency().equals("rub"))
            .map(Share::getFigi)
            .toList();
    }
}
