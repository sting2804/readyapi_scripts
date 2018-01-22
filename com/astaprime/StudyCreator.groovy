package com.astaprime;

import com.astaprime.Protocol.*;

import groovy.sql.Sql

import java.sql.DriverManager;
import java.sql.SQLException
import com.astaprime.*;


class StudyCreator {

    private def sql
    String globalConnectionString
    String username
    String password

    StudyCreator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

        def String create(Map params) {
        initSql(globalConnectionString,username,password)

           def codeName = params.codeName
           def statusId = params.statusId
           def i_date = params.i_date
           def s_date = params.s_date
           def e_date = params.e_date
           def c_date = params.c_date
           def treatmentDescription = params.treatmentDescription
           def inclusionCriteria = params.inclusionCriteria
           def title = params.title
           def userId = params.userId
           def protocol = null;


           userId = sql.firstRow("SELECT ID FROM CORE_EMPLOYEE WHERE USERNAME = $userId")[0]

           def details = sql.execute("INSERT INTO CORE_PROTOCOL_DETAILS (ID,VERSION,ECRF,END_OF_RECRUITMENT_ON,INCLUSION_CRITERIA,INITIATION_VISIT_ON,START_OF_RECRUITMENT_ON,TREATMENT_DESCRIPTION, EXCLUSION_CRITERIA) VALUES (hibernate_sequence.nextval, '0', '1', to_date($e_date, 'yyyy-MM-dd'), $inclusionCriteria, to_date($i_date, 'yyyy-MM-dd'), to_date($s_date, 'yyyy-MM-dd'), $treatmentDescription, $inclusionCriteria)")
           details = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_DETAILS WHERE inclusion_criteria = $inclusionCriteria and treatment_description = $treatmentDescription").getProperty('ID')
println details
           protocol = sql.execute("INSERT INTO CORE_PROTOCOL (ID,VERSION,CODE_NAME,DATE_CREATED,DETAILS_ID,STATUS_ID,TITLE,LAST_UPDATED) VALUES (HIBERNATE_SEQUENCE.nextval, '0', $codeName, sysdate, $details, $statusId, $title, sysdate)")
           protocol = sql.firstRow("SELECT ID FROM CORE_PROTOCOL WHERE CODE_NAME = $codeName").getProperty('ID')
println protocol

           def screeningArm = sql.execute("INSERT INTO CORE_PROTOCOL_GROUP (ID, VERSION, NAME, IS_EDITABLE, IS_ACTIVE_FOLLOW_UP, STARTING_POINT_CODE, PROTOCOL_ID) VALUES (hibernate_sequence.nextval, '0', 'Screening', '0', '0', 'SCREENING', $protocol)")
           screeningArm = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $protocol AND NAME = 'Screening'").getProperty('ID')
println screeningArm
           def screeningCycle = sql.execute("INSERT INTO TT_PROTOCOL_VISIT (ID, VERSION, NAME, ORDER_NUMBER, PROTOCOL_GROUP_ID) VALUES (hibernate_sequence.nextval, '0', 'Screening', '1',$screeningArm)")
           screeningCycle = sql.firstRow("SELECT ID FROM TT_PROTOCOL_VISIT WHERE NAME = 'Screening' AND PROTOCOL_GROUP_ID = $screeningArm").getProperty('ID')
println screeningCycle
           def firstDayByScreeningCycle = sql.execute("INSERT INTO TT_DAY_OF_VISIT (ID,VERSION,DAY_IN_CYCLE,VARIATION,VISIT_ID) VALUES (hibernate_sequence.nextval, '0','1','1',$screeningCycle)")
           firstDayByScreeningCycle = sql.firstRow("SELECT ID FROM TT_DAY_OF_VISIT WHERE DAY_IN_CYCLE = 1 AND VISIT_ID = $screeningCycle").getProperty('ID')
println firstDayByScreeningCycle

           def piPgId = sql.firstRow("SELECT ID FROM S_PERMISSION_GROUP WHERE lower(NAME) = lower('PI') ")[0]
           def scPgId = sql.firstRow("SELECT ID FROM S_PERMISSION_GROUP WHERE lower(NAME) = lower('study coordinator') ")[0]

           def pi_responsibility = sql.execute("INSERT INTO S_RESPONSIBILITY (ID,VERSION,CREATED_ON,PERMISSION_GROUP_ID,START_DATE,USER_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $piPgId, sysdate, $userId)")
           def sc_responsibility = sql.execute("INSERT INTO S_RESPONSIBILITY (ID,VERSION,CREATED_ON,PERMISSION_GROUP_ID,START_DATE,USER_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $scPgId, sysdate, $userId)")


           pi_responsibility = sql.firstRow("SELECT ID FROM S_RESPONSIBILITY WHERE PERMISSION_GROUP_ID = $piPgId AND USER_ID = $userId")[0]
           sc_responsibility = sql.firstRow("SELECT ID FROM S_RESPONSIBILITY WHERE PERMISSION_GROUP_ID = $scPgId AND USER_ID = $userId")[0]

           def s_resp_log = sql.execute("INSERT INTO S_RESP_LOG (ID, VERSION, CREATED_ON, SIGNED_BY_ID, SIGNED_ON, STUDY_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $userId, sysdate, $protocol)")
           s_resp_log = sql.firstRow("SELECT ID FROM S_RESP_LOG WHERE SIGNED_BY_ID = $userId AND STUDY_ID = $protocol")[0]

           def s_pi_resp_log_resp = sql.execute("INSERT INTO S_RESP_LOG_RESP (LOG_ID, RESP_ID) VALUES ($s_resp_log, $pi_responsibility)")
           def s_sc_resp_log_resp = sql.execute("INSERT INTO S_RESP_LOG_RESP (LOG_ID, RESP_ID) VALUES ($s_resp_log, $sc_responsibility)")

           sql.close();
           return protocol
    }

        def String createWithAllArms (Map params) {
        initSql(globalConnectionString,username,password)

        def codeName = params.codeName
        def statusId = params.statusId
        def i_date = params.i_date
        def s_date = params.s_date
        def e_date = params.e_date
        def c_date = params.c_date
        def treatmentDescription = params.treatmentDescription
        def inclusionCriteria = params.inclusionCriteria
        def title = params.title
        def userId = params.userId
        def protocol = null;


        userId = sql.firstRow("SELECT ID FROM CORE_EMPLOYEE WHERE USERNAME = $userId")[0]

        def details = sql.execute("INSERT INTO CORE_PROTOCOL_DETAILS (ID,VERSION,ECRF,END_OF_RECRUITMENT_ON,INCLUSION_CRITERIA,INITIATION_VISIT_ON,START_OF_RECRUITMENT_ON,TREATMENT_DESCRIPTION, EXCLUSION_CRITERIA) VALUES (hibernate_sequence.nextval, '0', '1', to_date($e_date, 'yyyy-MM-dd'), $inclusionCriteria, to_date($i_date, 'yyyy-MM-dd'), to_date($s_date, 'yyyy-MM-dd'), $treatmentDescription, $inclusionCriteria)")
        details = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_DETAILS WHERE inclusion_criteria = $inclusionCriteria and treatment_description = $treatmentDescription").getProperty('ID')
        println details
        protocol = sql.execute("INSERT INTO CORE_PROTOCOL (ID,VERSION,CODE_NAME,DATE_CREATED,DETAILS_ID,STATUS_ID,TITLE,LAST_UPDATED) VALUES (HIBERNATE_SEQUENCE.nextval, '0', $codeName, sysdate, $details, $statusId, $title, sysdate)")
        protocol = sql.firstRow("SELECT ID FROM CORE_PROTOCOL WHERE CODE_NAME = $codeName").getProperty('ID')
        println protocol

            def screeningArm = sql.execute("INSERT INTO CORE_PROTOCOL_GROUP (ID, VERSION, NAME, IS_EDITABLE, IS_ACTIVE_FOLLOW_UP, STARTING_POINT_CODE, PROTOCOL_ID) VALUES (hibernate_sequence.nextval, '0', 'Screening', '0', '0', 'SCREENING', $protocol)")
            screeningArm = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $protocol AND NAME = 'Screening'").getProperty('ID')
            println screeningArm
            def screeningCycle = sql.execute("INSERT INTO TT_PROTOCOL_VISIT (ID, VERSION, NAME, ORDER_NUMBER, PROTOCOL_GROUP_ID) VALUES (hibernate_sequence.nextval, '0', 'Screening', '1',$screeningArm)")
            screeningCycle = sql.firstRow("SELECT ID FROM TT_PROTOCOL_VISIT WHERE NAME = 'Screening' AND PROTOCOL_GROUP_ID = $screeningArm").getProperty('ID')
            println screeningCycle
            def firstDayByScreeningCycle = sql.execute("INSERT INTO TT_DAY_OF_VISIT (ID,VERSION,DAY_IN_CYCLE,VARIATION,VISIT_ID) VALUES (hibernate_sequence.nextval, '0','1','1',$screeningCycle)")
            firstDayByScreeningCycle = sql.firstRow("SELECT ID FROM TT_DAY_OF_VISIT WHERE DAY_IN_CYCLE = 1 AND VISIT_ID = $screeningCycle").getProperty('ID')
            println firstDayByScreeningCycle


            def treatmentArm = sql.execute("INSERT INTO CORE_PROTOCOL_GROUP (ID, VERSION, NAME, IS_EDITABLE, IS_ACTIVE_FOLLOW_UP, STARTING_POINT_CODE, PROTOCOL_ID) VALUES (hibernate_sequence.nextval, '0', 'Treatment arm', '0', '0', 'RANDOMIZATION', $protocol)")
            treatmentArm = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $protocol AND NAME = 'Treatment arm'").getProperty('ID')
            println treatmentArm
            def eotCycle = sql.execute("INSERT INTO TT_PROTOCOL_VISIT (ID, VERSION, NAME, ORDER_NUMBER, PROTOCOL_GROUP_ID) VALUES (hibernate_sequence.nextval, '0', 'End of treatment', '2',$treatmentArm)")
            eotCycle = sql.firstRow("SELECT ID FROM TT_PROTOCOL_VISIT WHERE NAME = 'End of treatment' AND PROTOCOL_GROUP_ID = $treatmentArm").getProperty('ID')
            println eotCycle
            def firstDayByEotCycle = sql.execute("INSERT INTO TT_DAY_OF_VISIT (ID,VERSION,DAY_IN_CYCLE,VARIATION,VISIT_ID) VALUES (hibernate_sequence.nextval, '0','1','1',$eotCycle)")
            firstDayByEotCycle = sql.firstRow("SELECT ID FROM TT_DAY_OF_VISIT WHERE DAY_IN_CYCLE = 1 AND VISIT_ID = $eotCycle").getProperty('ID')
            println firstDayByEotCycle
            def firstCycle = sql.execute("INSERT INTO TT_PROTOCOL_VISIT (ID, VERSION, NAME, ORDER_NUMBER, PROTOCOL_GROUP_ID) VALUES (hibernate_sequence.nextval, '0', 'Cycle 1', '1',$treatmentArm)")
            firstCycle = sql.firstRow("SELECT ID FROM TT_PROTOCOL_VISIT WHERE NAME = 'Cycle 1' AND PROTOCOL_GROUP_ID = $treatmentArm").getProperty('ID')
            println eotCycle
            def firstDayByFirstCycle = sql.execute("INSERT INTO TT_DAY_OF_VISIT (ID,VERSION,DAY_IN_CYCLE,VARIATION,VISIT_ID) VALUES (hibernate_sequence.nextval, '0','1','1',$firstCycle)")
            firstDayByFirstCycle = sql.firstRow("SELECT ID FROM TT_DAY_OF_VISIT WHERE DAY_IN_CYCLE = 1 AND VISIT_ID = $firstCycle").getProperty('ID')
            println firstDayByEotCycle




            def followUpArm = sql.execute("INSERT INTO CORE_PROTOCOL_GROUP (ID, VERSION, NAME, IS_EDITABLE, IS_ACTIVE_FOLLOW_UP, STARTING_POINT_CODE, PROTOCOL_ID) VALUES (hibernate_sequence.nextval, '0', 'Follow up', '0', '1', 'END_OF_TREATMENT', $protocol)")
            followUpArm = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $protocol AND NAME = 'Follow up'").getProperty('ID')
            println followUpArm
            def followUpCycle = sql.execute("INSERT INTO TT_PROTOCOL_VISIT (ID, VERSION, NAME, ORDER_NUMBER, PROTOCOL_GROUP_ID) VALUES (hibernate_sequence.nextval, '0', 'Follow up', '1',$followUpArm)")
            followUpCycle = sql.firstRow("SELECT ID FROM TT_PROTOCOL_VISIT WHERE NAME = 'Follow up' AND PROTOCOL_GROUP_ID = $followUpArm").getProperty('ID')
            println eotCycle
            def firstDayByFollowUpCycle = sql.execute("INSERT INTO TT_DAY_OF_VISIT (ID,VERSION,DAY_IN_CYCLE,VARIATION,VISIT_ID) VALUES (hibernate_sequence.nextval, '0','1','1',$followUpCycle)")
            firstDayByFollowUpCycle = sql.firstRow("SELECT ID FROM TT_DAY_OF_VISIT WHERE DAY_IN_CYCLE = 1 AND VISIT_ID = $followUpCycle").getProperty('ID')
            println firstDayByEotCycle



        def piPgId = sql.firstRow("SELECT ID FROM S_PERMISSION_GROUP WHERE lower(NAME) = lower('PI') ")[0]
        def scPgId = sql.firstRow("SELECT ID FROM S_PERMISSION_GROUP WHERE lower(NAME) = lower('study coordinator') ")[0]

        def pi_responsibility = sql.execute("INSERT INTO S_RESPONSIBILITY (ID,VERSION,CREATED_ON,PERMISSION_GROUP_ID,START_DATE,USER_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $piPgId, sysdate, $userId)")
        def sc_responsibility = sql.execute("INSERT INTO S_RESPONSIBILITY (ID,VERSION,CREATED_ON,PERMISSION_GROUP_ID,START_DATE,USER_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $scPgId, sysdate, $userId)")


        pi_responsibility = sql.firstRow("SELECT ID FROM S_RESPONSIBILITY WHERE PERMISSION_GROUP_ID = $piPgId AND USER_ID = $userId")[0]
        sc_responsibility = sql.firstRow("SELECT ID FROM S_RESPONSIBILITY WHERE PERMISSION_GROUP_ID = $scPgId AND USER_ID = $userId")[0]

        def s_resp_log = sql.execute("INSERT INTO S_RESP_LOG (ID, VERSION, CREATED_ON, SIGNED_BY_ID, SIGNED_ON, STUDY_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $userId, sysdate, $protocol)")
        s_resp_log = sql.firstRow("SELECT ID FROM S_RESP_LOG WHERE SIGNED_BY_ID = $userId AND STUDY_ID = $protocol")[0]

        def s_pi_resp_log_resp = sql.execute("INSERT INTO S_RESP_LOG_RESP (LOG_ID, RESP_ID) VALUES ($s_resp_log, $pi_responsibility)")
        def s_sc_resp_log_resp = sql.execute("INSERT INTO S_RESP_LOG_RESP (LOG_ID, RESP_ID) VALUES ($s_resp_log, $sc_responsibility)")

        sql.close();
        return protocol
    }
}

