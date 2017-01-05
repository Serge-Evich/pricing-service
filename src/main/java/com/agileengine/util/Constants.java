package com.agileengine.util;

import java.util.regex.Pattern;

public class Constants {
    public static final String DATETIME_Z_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final Pattern DATE_TIMEZONE_PATTERN = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})((\\+|-)\\d{4})");

    public static final String OBJECT_MUST_BE_NOT_NULL = "Object must be not null";
    public static final String OBJECT_CONTAINS_NULL_FIELDS = "Object contains null fields";
    public static final String DUPLICATE_PRODUCT_NAME = "Duplicate product name: %s";
    public static final String OBJECT_NOT_FOUND = "Object not found";
    public static final String INVALID_DATE_FORMAT = "Invalid date format";

}
