package com.astaprime

import java.text.DateFormat
import java.text.SimpleDateFormat

class DataGenerator {

    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

   /* static String getCurrentDateWithTime() {
        return sdtf.format(new Date())
    }

    static String getCurrentDate() {
        return sdf.format(new Date())
    }

    static String stringDateByYearAndMonthAndDay(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        return sdf.format(dateByYearAndMonthAndDay(year, month, day, hour, minute))
    }
*/
       static Integer randomNumber(Integer min, Integer max) {
        return min + (int)(Math.random() * ((max - min) + min))
    }
/*
    static Date addParams(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        def currentDate = new Date()
        def calendar = Calendar.getInstance()
        calendar.setTime(currentDate)
        if (day)
            calendar.add(Calendar.DATE, day)
        if (month)
            calendar.add(Calendar.MONTH, month)
        if (year)
            calendar.add(Calendar.YEAR, year)
        if (hour)
            calendar.add(Calendar.HOUR_OF_DAY, hour)
        if (minute)
            calendar.add(Calendar.MINUTE, minute)
        return calendar.getTime()
    }

    static String stringDateWithTimeByYearAndMonthAndDay(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        return sdtf.format(dateByYearAndMonthAndDay(year, month, day, hour, minute))
    }

    static String stringDateByDate(Date date) {
        return sdf.format(date)
    }

    static String stringDateTimeByDate(Date date) {
        return sdtf.format(date)
    }

*/

}

