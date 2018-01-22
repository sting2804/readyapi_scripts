package com.astaprime

import groovy.sql.Sql;
import java.sql.SQLException


/*
def globalConnectionString = context.expand( '${urlDB}' )
def username = context.expand( '${unameDB}' )
def password = context.expand('${passwordDB}')

def deleter = new Deleter(globalConnectionString, username, password)
deleter.deleteObjects([protocolId:1])
deleter.deleteObjects([protocolId:1, userId:1, activityId:1, patientId:1])
deleter.deleteObjects([protocolId:1, userId:1])
 */
class Checker {
    private def sql
    String globalConnectionString
    String username
    String password

    Checker(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

   /* def checkObjects(Map param) {
        initSql(globalConnectionString, username, password)
        param.each { key, value ->
            switch (key) {
                case 'protocolId':
                    deleteProtocol(value)
                    break
                case 'userId':
                    deleteUser(value)
                    break
                case 'activityId':
                    deleteActivity(value)
                    break
                case 'patientId':
                    deletePatient(value)
                    break
                case 'protocolIdWithoutStudy':
                    deleteProtocolsDaysCycleArms(value)
                case 'protocolIdWithoutStudyAndArms':
                    deleteProtocolsDaysCycle(value)
                case 'protocolIdWithoutWithoutStudyArmsAndCycles':
                    deleteProtocolsDays(value)
            }
        }
        sql.close()
    }
*/
    def CheckObjects() {
        def count_core_employees = sql.firstRow("SELECT COUNT(ID) FROM CORE_EMPLOYEE ORDER BY ID ASC")[0]
        log.info(count_core_employees)
        def core_employees = sql.firstRow("SELECT LISTAGG(id, ', ') WITHIN GROUP (ORDER BY id) FROM CORE_EMPLOYEE ORDER BY ID ASC")[0]
        log.info(core_employees)
    }
}
