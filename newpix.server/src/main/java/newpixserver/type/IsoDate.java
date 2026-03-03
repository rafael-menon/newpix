package newpixserver.type;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class IsoDate {
    public static String getCurrentDate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        OffsetDateTime offsetDateTime = currentDateTime.atOffset(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        return offsetDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String getTokenDuration(int minutes) {
        LocalDateTime expirationTime= LocalDateTime.now(ZoneOffset.UTC).plusMinutes(minutes);
        DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return expirationTime.format(sqlFormatter);
    }

    public static String formatDate(String date) {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd'/'MM'/'uuuu 'às' HH:mm:ss").withLocale(new Locale("pt", "BR"));
        return offsetDateTime.format(dateFormatter);
    }
}
