/**
 *
 *  @author Pluciński Tomasz S27477
 *
 */

package zad1;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Time {

    public static String passed(String s, String s1) {
        try {
            String result = "Od ";

            if (s.contains("T") && s1.contains("T")) {
                LocalDateTime date1 = LocalDateTime.parse(s);
                LocalDateTime date2 = LocalDateTime.parse(s1);
                result = getBaseInfoFromLDT(result, date1);
                result += " do ";
                result = getBaseInfoFromLDT(result, date2);
                result +=  "\n";

                long numberOfDaysBetweenDates = ChronoUnit.DAYS.between(date1.toLocalDate(), date2.toLocalDate());
                double numberOfWeeksBetweenDates = numberOfDaysBetweenDates/7.0;

                int nowbdi = numberOfWeeksBetweenDates % 7 == 0 ? (int) numberOfWeeksBetweenDates : -1;
                String numberOfWeeksResult = nowbdi == -1 ? String.format("%.2f", numberOfWeeksBetweenDates) : Integer.toString(nowbdi);
                String formatDay = getFormattedDay((int) numberOfDaysBetweenDates);
                result += " - mija: " + numberOfDaysBetweenDates + " " + formatDay + ", tygodni " + numberOfWeeksResult + "\n";

                ZonedDateTime zdt1 = ZonedDateTime.of(date1, ZoneId.of("Europe/Warsaw"));
                ZonedDateTime zdt2 = ZonedDateTime.of(date2, ZoneId.of("Europe/Warsaw"));
                long betweenHours = ChronoUnit.HOURS.between(zdt1, zdt2);
                long betweenMinutes = ChronoUnit.MINUTES.between(zdt1, zdt2);
                result += " - godzin: " + betweenHours + ", minut: " + betweenMinutes + "\n";

                int years = Period.between(date1.toLocalDate(), date2.toLocalDate()).getYears();
                int months = Period.between(date1.toLocalDate(),date2.toLocalDate()).getMonths();
                int days = Period.between(date1.toLocalDate(), date2.toLocalDate()).getDays();
                return getStringFromDate(result, years, months, days);

            } else {
                LocalDate date1 = LocalDate.parse(s);
                LocalDate date2 = LocalDate.parse(s1);
                result += date1.getDayOfMonth() + " ";
                result += date1.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + " ";
                result += date1.getYear() + " (";
                result += date1.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + ") do ";
                result += date2.getDayOfMonth() + " ";
                result += date2.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + " ";
                result += date2.getYear() + " (";
                result += date2.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + ")\n";

                long numberOfDaysBetweenDates = ChronoUnit.DAYS.between(date1, date2);
                double numberOfWeeksBetweenDates = numberOfDaysBetweenDates/7.0;

                int nowbdi = numberOfWeeksBetweenDates % 7 == 0 ? (int) numberOfWeeksBetweenDates : -1;
                String numberOfWeeksResult = nowbdi == -1 ? String.format("%.2f", numberOfWeeksBetweenDates) : Integer.toString(nowbdi);

                result += " - mija: " + numberOfDaysBetweenDates + " dni" + ", tygodni " + numberOfWeeksResult + "\n";

                int years = Period.between(date1, date2).getYears();
                int months = Period.between(date1,date2).getMonths();
                int days = Period.between(date1, date2).getDays();
                return getStringFromDate(result, years, months, days);
            }
        } catch (Exception e) {
            return "*** " + e;
        }
    }

    private static String getStringFromDate(String result, int years, int months, int days) {
        String correctlyNameYears = getFormattedYear(years);
        String correctlyNameMonths = getFormattedMonth(months);
        String correctlyNameDays = getFormattedDay(days);

        result += " - kalendarzowo: ";
        if(years > 0){
            result += years + " " + correctlyNameYears;
            if(months > 0){
                result += ", " + months + " " + correctlyNameMonths;
                if(days > 0){
                    result += ", " + days + " " + correctlyNameDays;
                }
            }

        }else{
            if(months > 0){
                result += ", " + months + " " + correctlyNameMonths;
                if(days > 0){
                    result += ", " + days + " " + correctlyNameDays;
                }
            }else{
                if(days > 0){
                    result += days + " " + correctlyNameDays;
                }
            }
        }
        return result;
    }

    private static String getBaseInfoFromLDT(String result, LocalDateTime date1) {
        result += date1.getDayOfMonth() + " ";
        result += date1.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + " ";
        result += date1.getYear() + " (";
        result += date1.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl")) + ") godz. ";
        result += date1.getHour() + ":" + String.format("%02d", date1.getMinute());
        return result;
    }

    private static String getFormattedYear(int year){
        if(year == 1) return "rok";
        else if (year >= 2 && year < 5) return "lata";
        else return "lat";
    }
    private static String getFormattedMonth(int month){
        if(month == 1) return "miesiąc";
        else if (month >= 2 && month < 5) return "miesiące";
        else return "miesięcy";
    }
    private static String getFormattedDay(int day){
        if(day == 1) return "dzień";
        else return "dni";
    }
}
