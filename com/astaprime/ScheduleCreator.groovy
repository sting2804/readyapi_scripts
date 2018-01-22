package com.astaprime

import groovy.sql.Sql;
import java.sql.SQLException

class ScheduleCreator {
    private def sql
    String globalConnectionString
    String username
    String password


    ScheduleCreator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

    def create(Map params) {
        initSql(globalConnectionString,username,password)
        def studyId = params.studyId
        def armName = params.armName
        def startingPointCode = params.startingPointCode
        def isActiveFollowUp = params.isActiveFollowUp
        def isEditable = params.isEditable
        def arm = null

        def createArm = sql.execute("INSERT INTO CORE_PROTOCOL_GROUP (ID,VERSION,IS_ACTIVE_FOLLOW_UP,IS_EDITABLE,NAME,PROTOCOL_ID,STARTING_POINT_CODE) VALUES (hibernate_sequence.nextval, '0', $isActiveFollowUp, $isEditable,$armName,$studyId,$startingPointCode)")
        arm = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE NAME = $armName AND POTOCOL_ID = $studyId AND $startingPointCode").getProperty('ID')

        sql.close()
        return arm
    }

    /*static String stringDateByYearAndMonthAndDay(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        return sdf.format(dateByYearAndMonthAndDay(year, month, day, hour, minute))
    }

    static Date dateByYearAndMonthAndDay(Integer year, Integer month, Integer day, Integer hour, Integer minute) {
        def currentDate = new Date()
        def calendar = Calendar.getInstance()
        calendar.setTime(currentDate)
        if (day)
            calendar.set(Calendar.DATE, day)
        if (month)
            calendar.set(Calendar.MONTH, month)
        if (year)
            calendar.set(Calendar.YEAR, year)
        if (hour)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
        if (minute)
            calendar.set(Calendar.MINUTE, minute)
        return calendar.getTime()
    }

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
