package com.astaprime

import com.astaprime.personalData.Gender.*;
import com.astaprime.personalData.MenNames.*;
import com.astaprime.personalData.WomenNames.*;
import com.astaprime.personalData.MenLastNames.*;
import com.astaprime.personalData.WomenLastNames.*;
import com.astaprime.personalData.WomenMiddleNames.*;
import com.astaprime.personalData.MenMiddleNames.*;
import com.astaprime.personalData.CountryCode.*;
import com.astaprime.personalData.City.*;
import com.astaprime.personalData.Address.*;

import groovy.sql.Sql;
import java.sql.SQLException

class PatientCreator {

    private def sql
    String globalConnectionString
    String username
    String password

    PatientCreator(String globalConnectionString, String username, String password) {
        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password
    }

    private initSql(String globalConnectionString, String username, String password) {
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }


    def create(Map params) {
        initSql(globalConnectionString,username,password)
        def userId = params.userId
        def studyId = params.studyId
        def statusId = params.statusId
        def patientId = null




        switch (statusId) {
            case '62':
                createActivePatient() {

                    def personalDataId = createPersonalData()
                    return personalDataId
                   /*
                    def pdId = sql.execute("INSERT INTO CORE_PERSONAL_DATA (ID,VERSION,ADDRESS,CITY,COUNTRY_CODE,DATE_OF_BIRTH,FIRST_NAME,GENDER,LAST_NAME,MIDDLE_NAME,STATE) VALUES (hibernate_sequence.nextval, '0', 'Main str., 15','New York','US',sysdate-4023,'Julia','F','Johanson','Shirley','New York')")
                    def personalData = sql.firstRow("SELECT ID FROM CORE_PERSONAL_DATA WHERE FIRST_NAME = 'Julia' AND LAST_NAME = 'Johanson'")[0]

                    def status = sql.firstRow("SELECT ID FROM CORE_PATIENT_STATUS WHERE CODE = 'STATUS_FOLLOW_UP_CALLS'")[0]
                    def study  = context.expand('${DataGen#create protocol}')
                    def codeName = 'AAA'
                    def text = 'Some text'
                    def date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()-4)
                    def employee = sql.firstRow("SELECT ID FROM CORE_EMPLOYEE WHERE USERNAME = 'hospital.admin@mail.com'")[0]
                    def create_group = sql.execute("""INSERT INTO CORE_PROTOCOL_GROUP (ID,
    VERSION,
    IS_ACTIVE_FOLLOW_UP,
    IS_EDITABLE,
    NAME,
    PROTOCOL_ID,
    SCHEDULE_CLOSED_BY_ID,
    SCHEDULE_CLOSED_ON,
    STARTING_POINT_CODE) VALUES (hibernate_sequence.nextval, '0', '0', '0', 'rand', ?, ?, to_date(?, 'yyyy-MM-dd'), 'START_OF_TREATMENT')""", study, employee, date)
                    def group = sql.firstRow("SELECT ID FROM CORE_PROTOCOL_GROUP WHERE NAME = ? AND PROTOCOL_ID = ?", 'rand', study)[0]

                    def create_patient = sql.execute("""
INSERT INTO CORE_PATIENT (ID, VERSION, ARRIVING_ON, CODE_NAME,DIAGNOSIS_ON_ADMISSION, DOCTOR_ID, GROUP_ID, INFORM_CONCEPT_ON, PROTOCOL_ID, RANDOMIZATION_NUMBER, RANDOMIZATION_ON, SCREENING_NUMBER, START_OF_TREATMENT_ON, STATUS_ID)
VALUES (hibernate_sequence.nextval, '3', TO_DATE('1970-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?,
TO_DATE(?, 'YYYY-MM-DD'), ?, ?, TO_DATE(?, 'YYYY-MM-DD'),
?, TO_DATE(?, 'YYYY-MM-DD'), ?)
""", codeName, text, employee, group, date, study, text, date, text, date, status)
                    def patient = sql.firstRow("SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = ? AND GROUP_ID = ? AND STATUS_ID = ? AND CODE_NAME = ?", study, group, status, codeName)[0]
                    def link = sql.execute("INSERT INTO PERSONAL_DATA_PATIENT (PERSONAL_DATA_PATIENTS_ID, PATIENT_ID) VALUES(?,?)", personalData, patient)


                    sql.close()
                    return patient*/
                }
                break
            case '63':
                createFollowUpActivePatient()
                break
            case '64':
                createFollowUpCallsPatient()
                break
            case '65':
                createDroppedOutPatient()
                break
            case '66':
                createScreeningPatient()
                break
            case '67':
                createScreenFailurePatient()
                break
            case '68':
                createDroppedOutProtocolClosed()
                break
            case '69':
                createTreatmentProtocolClosed()
                break
        }


        def createActivity = sql.execute("INSERT INTO TT_ACTIVITY (ID, VERSION, NAME, ACTIVITY_GROUP_ID, IS_INTERNAL, IS_CONTINUOUS) VALUES (hibernate_sequence.nextval,'0',$activityName, $activityGroupId, $isInternal, $isContinuous)")
        activityId = sql.firstRow("SELECT ID FROM TT_ACTIVITY WHERE ACTIVITY_GROUP_ID = $activityGroupId AND NAME = $activityName").getProperty('ID')

        /*   def create_perm_group_activity = sql.execute("INSERT INTO S_PERM_GROUP_ACTIVITY (PERM_GROUP_ID,ACTIVITY_ID) VALUES ($permGroupId, $activityId)")

           def create_study_responsibility = sql.execute("INSERT INTO STUDY_RESPONSIBILITY (ID, VERSION, CREATED_ON, PERMISSION_GROUP_ID, START_DATE, USER_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $permGroupId, sysdate, $userId)")
           def study_responsibility_id = sql.firstRow("SELECT ID FROM STUDY_RESPONSIBILITY WHERE PERMISSION_GROUP_ID = $permGroupId AND USER_ID = $userId").getProperty('ID')

           def create_study_responsibility_log = sql.execute("INSERT INTO S_RESPONSIBILITY_LOG (ID, VERSION, CREATED_ON, SIGNED_BY_ID, SIGNED_ON, STUDY_ID) VALUES (hibernate_sequence.nextval, '0', sysdate, $userId, sysdate, $studyId)")
           def study_responsibility_log_id = sql.firstRow("SELECT ID FROM S_RESPONSIBILITY_LOG WHERE STUDY_ID = $studyId AND SIGNED_BY_ID = $userId")[0]

           def s_resp_log_resp = sql.execute("INSERT INTO S_RESP_LOG_RESP (LOG_ID, STUDY_RESPONSIBILITY_ID) VALUES ($study_responsibility_log_id, $study_responsibility_id)")
   */
        sql.close()
        return activityId

    }


    def createPersonalData(){

        int randGender = 1 + (int)(Math.random() * ((2 - 1) + 1))
        int randFName = 1 + (int)(Math.random() * ((20 - 1) + 1))
        int randLName = 1 + (int)(Math.random() * ((10 - 1) + 1))
        int randMName = 1 + (int)(Math.random() * ((20 - 1) + 1))
        int randCCode = 1 + (int)(Math.random() * ((155 - 1) + 1))
        int randAddress = 1 + (int)(Math.random() * ((124 - 1) + 1))

        def gender = new GenderSource().getMessage("errors.$randGender")
        def firstname, lastname, middlename
        def country, city, address

        if (gender == 'M') {
            firstname = new MenNamesSource().getMessage("errors.$randFName")
            lastname = new MenLastNamesSource().getMessage("errors.$randLName")
            middlename = new MenMiddleNamesSource().getMessage("errors.$randMName")
        }
        else {
            firstname = new WomenNamesSource().getMessage("errors.$randMName")
            lastname = new WomenLastNamesSource().getMessage("errors.$randLName")
            middlename = new WomenMiddleNamesSource().getMessage("errors.$randMName")
        }

        country = new CountryCodeSource().getMessage("errors.$randCCode")
        city = new CitySource().getMessage("errors.$randCCode")
        address = new AddressSource().getMessage("errors.$randAddress") + ", $randCCode/$randMName"

        def personalDataId = sql.execute("INSERT INTO CORE_PERSONAL_DATA (ID,VERSION,ADDRESS,CITY,COUNTRY_CODE,DATE_OF_BIRTH,FIRST_NAME,GENDER,LAST_NAME,MIDDLE_NAME,STATE) VALUES (hibernate_sequence.nextval,'0',$address,$city,$country,sysdate-9855,$firstname,$gender,$lastname,$middlename,$city)")
        personalDataId = sql.firstRow("SELECT ID FROM CORE_PERSONAL_DATA WHERE FIRST_NAME = $firstname AND LAST_NAME = $lastname AND MIDDLE_NAME = $middlename AND ADDRESS = $address")[0]
        sql.close()
        return personalDataId
    }


}
