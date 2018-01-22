package com.astaprime

import groovy.sql.Sql;
import java.sql.SQLException

class ActivityCreator {

    private def sql
    String globalConnectionString
    String username
    String password

    ActivityCreator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }


    def createActivity(Map params) {
        initSql(globalConnectionString,username,password)
        def activityGroupId = params.activityGroupId
        def activityName = params.activityName
        def activityId = null


        def permGroupId = sql.firstRow("SELECT ID FROM S_PERMISSION_GROUP WHERE lower(NAME) = lower('PI')")[0]
        def isContinuous = '1'
        def isInternal = '0'


        def createActivity = sql.execute("INSERT INTO TT_ACTIVITY (ID, VERSION, NAME, ACTIVITY_GROUP_ID, IS_INTERNAL, IS_CONTINUOUS) VALUES (hibernate_sequence.nextval,'0',$activityName, $activityGroupId, $isInternal, $isContinuous)")
        activityId = sql.firstRow("SELECT ID FROM TT_ACTIVITY WHERE ACTIVITY_GROUP_ID = $activityGroupId AND NAME = $activityName").getProperty('ID')

        def create_perm_group_activity_e = sql.execute("INSERT INTO S_PERM_GROUP_ACT_E (PERM_GROUP_ID, ACTIVITIES_FOR_EDIT) VALUES ($permGroupId, $activityId)")
        def create_perm_group_activity_v = sql.execute("INSERT INTO S_PERM_GROUP_ACT_V (PERM_GROUP_ID, ACTIVITIES_FOR_VIEW) VALUES ($permGroupId, $activityId)")

        sql.close()
        return activityId

    }
}
