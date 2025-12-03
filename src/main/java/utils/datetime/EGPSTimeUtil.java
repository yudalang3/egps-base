package utils.datetime;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Utility class for converting between Date and LocalDate objects.
 */
public class  EGPSTimeUtil {

	static ZoneId systemDefault = ZoneId.systemDefault();

	public static LocalDate date2LocalDate(Date date) {
		if (null == date) {
			return null;
		}

		return date.toInstant().atZone(systemDefault).toLocalDate();
	}

	public static Date localDate2Date(LocalDate localDate) {
		if (null == localDate) {
			return null;
		}
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(systemDefault);
		return Date.from(zonedDateTime.toInstant());
	}

	public static List<Date> localDateList2dateList(List<LocalDate> input) {
		List<Date> ret = new ArrayList<>();
		for (LocalDate date : input) {
			ret.add(localDate2Date(date));
		}

		return ret;
	}

	public static List<LocalDate> dateList2localDateList(List<Date> input) {
		List<LocalDate> ret = new ArrayList<>();
		for (Date date : input) {
			ret.add(date2LocalDate(date));
		}

		return ret;
	}

}