package com.astaprime;

import com.astaprime.Protocol.*;
import groovy.sql.Sql

import java.sql.DriverManager;
import java.sql.SQLException
import com.astaprime.*;


class DataGen {

    private def sql
    String globalConnectionString
    String username
    String password

	String personalDataId

    DataGen(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
      sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }
/*
    def String getUniqueStudyCodename() {
        int randCodeName = 1 + (int) (Math.random() * ((5414 - 1) + 1))
        ​def codeName = new ProtocolNameSource().getMessage("name.$randCodeName")

        ​ def count = sql.firstRow("SELECT COUNT(ID) FROM CORE_PROTOCOL WHERE CODE_NAME = '$codeName'")[0]
        if (count == 0) {
            return codeName
        } else {
            randNumber = new Random(System.currentTimeMillis()).nextInt(5414 - 1) + 1
            codeName = new ProtocolNameSource().getMessage("name.$randCodeName") + " $randCodeName"
        }
        sql.close();


        return codeName
    }
*/
    def String getPatientCodename(String personalDataId){
        def pdId = this.personalDataId

        def firstname = sql.firstRow("SELECT FIRST_NAME FROM CORE_PERSONAL_DATA WHERE ID = $pdId")[0]
        def lastname =  sql.firstRow("SELECT LAST_NAME FROM CORE_PERSONAL_DATA WHERE ID = $pdId")[0]

        int randomNumber = 0 + (int) (Math.random() * ((9 - 0) + 0))

        char f_char = firstname.charAt(0);
        char l_char = lastname.charAt(0);


        return codeName = l_char + f_char + randomNumber
    }
}
