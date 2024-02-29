package edu.java.bot.action.finders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.tinkoff.piapi.contract.v1.Quotation;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Converter {
    public static BigDecimal convertUnitsAndNanos(Quotation value){
        if (value == null) {
            return null;
        }
       return mapUnitsAndNanos(value.getUnits(), value.getNano());
    }
    private static  BigDecimal mapUnitsAndNanos(long units, int nanos) {
        if (units == 0 && nanos == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(units + nanos/ 1e9);
    }
}
