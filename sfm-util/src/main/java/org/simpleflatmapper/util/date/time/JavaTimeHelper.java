package org.simpleflatmapper.util.date.time;

import org.simpleflatmapper.util.date.DateFormatSupplier;
import org.simpleflatmapper.util.date.DefaultDateFormatSupplier;
import org.simpleflatmapper.util.date.TimeZoneSupplier;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class JavaTimeHelper {

    public static DateTimeFormatter[] getDateTimeFormatters(Object... properties) {
        List<DateTimeFormatter> dtf = new ArrayList<DateTimeFormatter>();

        ZoneId zoneId = getZoneId(properties);

        DefaultDateFormatSupplier defaultDateFormatSupplier = null;
        for(Object prop : properties) {
            if (prop instanceof DateTimeFormatterSupplier) {
                dtf.add(withZone(((DateTimeFormatterSupplier) prop).get(), zoneId));
            } else if (prop instanceof DateFormatSupplier) {
                dtf.add(withZone(((DateFormatSupplier) prop).get(), zoneId));
            } else if (prop instanceof DefaultDateFormatSupplier) {
                defaultDateFormatSupplier = (DefaultDateFormatSupplier) prop;
            }
        }

        if (dtf.isEmpty()) {
            if (defaultDateFormatSupplier == null) {
                throw new IllegalStateException("No date format specified");
            }
            dtf.add(withZone(defaultDateFormatSupplier.get(), zoneId));
        }

        return dtf.toArray(new DateTimeFormatter[0]);
    }

    private static DateTimeFormatter withZone(String format, ZoneId zoneId) {
        return withZone(DateTimeFormatter.ofPattern(format), zoneId);
    }

    private static DateTimeFormatter withZone(DateTimeFormatter dateTimeFormatter, ZoneId zoneId) {
        if (zoneId != null) {
            return dateTimeFormatter.withZone(zoneId);
        } else if (dateTimeFormatter.getZone() == null) {
            return dateTimeFormatter.withZone(ZoneId.systemDefault());
        }
        return dateTimeFormatter;
    }

    public static ZoneId getZoneIdOrDefault(Object... params) {
        ZoneId p = getZoneId(params);
        if (p != null) return p;
        return ZoneId.systemDefault();
    }

    public static ZoneId getZoneId(Object[] params) {
        if (params != null) {
            for(Object p : params) {
                if (p instanceof ZoneId) {
                    return (ZoneId) p;
                } else if (p instanceof TimeZone) {
                    return ((TimeZone)p).toZoneId();
                } else if (p instanceof ZoneIdSupplier) {
                    return ((ZoneIdSupplier)p).get();
                } else if (p instanceof TimeZoneSupplier) {
                    return (((TimeZoneSupplier)p).get()).toZoneId();
                }
            }
        }
        return null;
    }

}
