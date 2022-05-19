package com.company.executor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime implements Executor {
    public DateTime() {
    }

    @Override
    public String execute(String args) {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.DATE_TIME;
    }
}
