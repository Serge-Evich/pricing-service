package com.agileengine.dto;


import com.agileengine.util.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TimestampDTO {
    @JsonFormat(pattern = Constants.DATETIME_FORMAT)
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
