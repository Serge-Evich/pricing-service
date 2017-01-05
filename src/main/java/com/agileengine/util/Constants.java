package com.agileengine.util;

import java.util.regex.Pattern;

public class Constants {
    public static final String DATETIME_Z_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final Pattern DATE_TIMEZONE_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})((\\+|-)\\d{4})");
}
