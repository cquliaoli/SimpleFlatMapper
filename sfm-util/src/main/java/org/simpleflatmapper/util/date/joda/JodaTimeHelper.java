package org.simpleflatmapper.util.date.joda;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleflatmapper.util.TypeHelper;
import org.simpleflatmapper.util.date.DateFormatSupplier;
import org.simpleflatmapper.util.date.DefaultDateFormatSupplier;
import org.simpleflatmapper.util.date.TimeZoneSupplier;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class JodaTimeHelper {

    public static DateTimeFormatter[] getDateTimeFormatters(Object... properties) {
        final DateTimeZone dateTimeZone = getDateTimeZone(properties);

        List<DateTimeFormatter> dtf = new ArrayList<DateTimeFormatter>();

        DefaultDateFormatSupplier defaultDateFormatSupplier = null;
        for(Object prop : properties) {
            if (prop instanceof DateTimeFormatterSupplier) {
                dtf.add(withZone(((DateTimeFormatterSupplier) prop).get(), dateTimeZone));
            } else if (prop instanceof DateFormatSupplier) {
                dtf.add(withZone(((DateFormatSupplier)prop).get(), dateTimeZone));
            } else if (prop instanceof DefaultDateFormatSupplier) {
                defaultDateFormatSupplier = (DefaultDateFormatSupplier) prop;
            }
        }

        if (dtf.isEmpty()) {
            if (defaultDateFormatSupplier == null) {
                throw new IllegalStateException("No date format specified");
            }
            dtf.add(withZone(DateTimeFormat.forPattern(defaultDateFormatSupplier.get()), dateTimeZone));
        }

        return dtf.toArray(new DateTimeFormatter[0]);
    }
    private static DateTimeFormatter withZone(String format, DateTimeZone zoneId) {
        return withZone(DateTimeFormat.forPattern(format), zoneId);
    }
    private static DateTimeFormatter withZone(DateTimeFormatter dateTimeFormatter, DateTimeZone zoneId) {
        if (zoneId != null) {
            return dateTimeFormatter.withZone(zoneId);
        } else if (dateTimeFormatter.getZone() == null) {
            return dateTimeFormatter.withZone(DateTimeZone.getDefault());
        }
        return dateTimeFormatter;
    }


    public static DateTimeZone getDateTimeZoneOrDefault(Object... params) {
        DateTimeZone p = getDateTimeZone(params);
        if (p != null) return p;
        return DateTimeZone.getDefault();
    }

    private static DateTimeZone getDateTimeZone(Object[] params) {
        if (params != null) {
            for(Object p : params) {
                if (p instanceof DateTimeZoneSupplier) {
                    return ((DateTimeZoneSupplier) p).get();
                } else if (p instanceof DateTimeZone) {
                    return (DateTimeZone) p;
                } else if (p instanceof TimeZone) {
                    return DateTimeZone.forTimeZone((TimeZone)p);
                } else if (p instanceof TimeZoneSupplier) {
                    return DateTimeZone.forTimeZone(((TimeZoneSupplier) p).get());
                }
            }
        }
        return null;
    }

    public static final String ORG_JODA_TIME_DATE_TIME = "org.joda.time.DateTime";

    public static final String ORG_JODA_TIME_LOCAL_DATE = "org.joda.time.LocalDate";

    public static final String ORG_JODA_TIME_LOCAL_DATE_TIME = "org.joda.time.LocalDateTime";

    public static final String ORG_JODA_TIME_LOCAL_TIME = "org.joda.time.LocalTime";

    public static final String ORG_JODA_INSTANT = "org.joda.time.Instant";

    public static boolean isJoda(Type target) {
        return getTypeName(target).startsWith("org.joda.time");
    }

    private static String getTypeName(Type target) {
        return TypeHelper.toClass(target).getName();
    }

    public static boolean isJodaDateTime(Type target) {
        return ORG_JODA_TIME_DATE_TIME.equals(getTypeName(target));
    }
    public static boolean isJodaInstant(Type target) {
        return ORG_JODA_INSTANT.equals(getTypeName(target));
    }
    public static boolean isJodaLocalDateTime(Type target) {
        return ORG_JODA_TIME_LOCAL_DATE_TIME.equals(getTypeName(target));
    }
    public static boolean isJodaLocalDate(Type target) {
        return ORG_JODA_TIME_LOCAL_DATE.equals(getTypeName(target));
    }
    public static boolean isJodaLocalTime(Type target) {
        return ORG_JODA_TIME_LOCAL_TIME.equals(getTypeName(target));
    }
}
