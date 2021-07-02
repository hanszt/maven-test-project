package com.dnb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.System.*;

public class LocalDateTimeAndDate {

    public static void main(String[] args) {
        var localDate = LocalDate.now();
        out.println(localDate.format(DateTimeFormatter.ISO_DATE));
    }



}
