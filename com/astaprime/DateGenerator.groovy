package com.astaprime

import java.util.*
import groovy.sql.Sql;
import java.sql.SQLException

class DateGenerator {
    private def sql
    String globalConnectionString
    String username
    String password

    DateGenerator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password){
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

    def dates(Map param) {
        initSql(globalConnectionString,username,password)
        param.each { key, value ->
            switch (key) {
                case 'todayDate':
                    generateTodayDate(value)
                    break
                case 'todayDatetime':
                    generateTodayDatetime(value)
                    break
                case 'tomorrowDate':
                    generateTomorrowDate(value)
                    break
                case 'tomorrowDatetime':
                    generateTomorrowDatetime(value)
                    break
                case 'yesterdayDate':
                    generateYesterdayDate(value)
                    break
                case 'yesterdayDatetime':
                    generateYesterdayDatetime(value)
                    break
            }
        }
        sql.close()
    }

    def generateTodayDate() {
        def currentDate = new Date()
        def calendar = Calendar.getInstance()
        calendar.setTime(currentDate)
        calendar.add(Calendar.DATE)
        calendar.getTime()

        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime())
    }
}
