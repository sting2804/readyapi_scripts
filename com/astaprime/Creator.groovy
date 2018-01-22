package com.astaprime

import com.astaprime.protocols.*;
import groovy.sql.Sql;
import java.sql.SQLException


class Creator {
    private def sql
    String globalConnectionString
    String username
    String password

    Creator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password){
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

    def createObjects(Map param) {
        initSql(globalConnectionString,username,password)
        param.each { key, value ->
            switch (key) {
                case 'activityGroup':
                    createActivityGroup(value)
                    break
                case 'permissionGroup':
                    createPermissionGroup(value)
                    break
                case 'activity':
                    createActivity(value)
                    break
                case 'user':
                    createUser(value)
                    break
                case 'study':
                    createStudy(value)
                    break
                case 'personalData':
                    createPersonalData(value)
                    break
                case 'schedule':
                    createSchedule(value)
                    break
            }
        }
        sql.close()
    }

    def createActivityGroup(String randNumber) {

        def agName= new ProtocolNameSource().getMessage("name.$randNumber")
       /* def count = sql.firstRow("SELECT COUNT(ID) FROM CORE_PROTOCOL WHERE CODE_NAME = 'agName'")[0]
        if (count == 0) {return name}
        else {randNumber = new Random(System.currentTimeMillis()).nextInt(5414 - 1) + 1
            agName= new ProtocolNameSource().getMessage("agName.$randNumber")}
        sql.close();
        return agName*/
        def color = "#fffff0"


        def create_activity_group = sql.execute("INSERT INTO TT_ACTIVITY_GROUP (ID, VERSION, NAME, COLOR) VALUES (hibernate_Sequence.nextval, '0', $agName, $color)")
        def activity_group_id = sql.firstRow("SELECT ID FROM TT_ACTIVITY_GROUP WHERE NAME = $agName")
    }

}
