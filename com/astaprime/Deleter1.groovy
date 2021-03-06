package com.astaprime

import groovy.sql.Sql
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

class Deleter {
    private def sql
    String globalConnectionString
    String username
    String password

    Deleter(String globalConnectionString, String username, String password) {


        this.globalConnectionString = globalConnectionString
        this.username = username
        this.password = password

    }

    private void initSql(String globalConnectionString, String username, String password){
        sql = Sql.newInstance(globalConnectionString, username, password, "oracle.jdbc.driver.OracleDriver")
    }

    def deleteObjects(Map param) {
        initSql(globalConnectionString,username,password)
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
            }
        }
        sql.close()
    }

    def deleteProtocol(String study) {
        def delete_question_by_patient = sql.execute("DELETE FROM TT_QUESTION WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_question_by_study = sql.execute("DELETE FROM TT_QUESTION WHERE STUDY_ID = $study")

        def delete_signature_log_by_patient = sql.execute("DELETE FROM TT_SIGNATURE_LOG WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_signature_log_by_cycle = sql.execute("DELETE FROM TT_SIGNATURE_LOG WHERE CYCLE_ID IN (SELECT ID FROM TT_PROTOCOL_VISIT WHERE PROTOCOL_GROUP_ID IN (SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $study))")

        def delete_inscan_qr_code_by_patient = sql.execute("DELETE FROM INSCAN_QR_CODE_VISIT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_schedule_signature_by_patient = sql.execute("DELETE FROM TT_SCHEDULE_SIGNATURE WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_schedule_event_by_patient = sql.execute("DELETE FROM TT_SCHEDULED_EVENT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_schedule_event_log_by_patient = sql.execute("DELETE FROM TT_SCHEDULED_EVENT_LOG WHERE SCHEDULED_EVENT_ID IN  (SELECT ID FROM TT_SCHEDULED_EVENT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study))")

        def delete_cus_value_array_by_patient = sql.execute("DELETE FROM S_CUS_VALUE_ARRAY WHERE CUSTOM_FIELD_VALUE_ID IN (SELECT ID FROM S_CUSTOM_FIELD_VALUE WHERE PROTOCOL_Id = $study)")
        def delete_custom_field_value_by_patient = sql.execute("DELETE FROM S_CUSTOM_FIELD_VALUE WHERE PROTOCOL_Id = $study")

        def delete_query_by_patient = sql.execute("DELETE FROM TT_QUERY WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_sms_log_by_patient = sql.execute("DELETE FROM NOTIFICATION_SMS_LOG WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_sms_queue_by_patient = sql.execute("DELETE FROM NOTIFICATION_SMS_QUEUE WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")

        def delete_one_day_code_by_patient = sql.execute("DELETE FROM ONE_DAY_CODE WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")

        def delete_filter_field= sql.execute("DELETE FROM PATIENT_FILTER_FIELD WHERE FIELD = 'protocolId' AND VALUE = '$study'")
        def delete_patient_fiter = sql.execute("DELETE FROM PATIENT_FILTER WHERE ID NOT IN (SELECT FILTER_ID FROM PATIENT_FILTER_FIELD)")

        def delete_client_statistics_by_patient = sql.execute("DELETE FROM CLIENT_STATISTICS_BUFFER WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_doc_document_by_patient = sql.execute("DELETE FROM DOC_DOCUMENT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study) OR PROTOCOL_ID = $study")
        def delete_doc_inform_concept_by_patient = sql.execute("DELETE FROM DOC_INFORM_CONCEPT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_doctor_adverse_event_by_patient = sql.execute("DELETE FROM DOCTOR_ADVERSE_EVENT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_doctor_appointment = sql.execute("DELETE FROM DOCTOR_APPOINTMENT WHERE PROTOCOL_ID = $study")
        def delete_doctor_hospitalization = sql.execute("DELETE FROM DOCTOR_HOSPITALIZATIONS WHERE PROTOCOL_ID = $study")
        def delete_info_meeting = sql.execute("DELETE FROM INFO_MEETING WHERE PROTOCOL_ID = $study")
        def delete_monitor_visits = sql.execute("DELETE FROM INFO_MONITOR_VISIT WHERE PROTOCOL_ID = $study")
        def delete_protocol_website = sql.execute("DELETE FROM INFO_PROTOCOL_WEBSITE WHERE PROTOCOL_ID = $study")
        def delete_info_task = sql.execute("DELETE FROM INFO_TASK WHERE PROTOCOL_ID = $study")
        def delete_drug= sql.execute("DELETE FROM PH_DRUG WHERE PROTOCOL_ID = $study")
        def delete_drug_order = sql.execute("DELETE FROM PH_DRUG_ORDER WHERE PROTOCOL_ID = $study")
        def delete_pharmacy_log = sql.execute("DELETE FROM PH_PHARMACY_LOG WHERE PROTOCOL_ID = $study")


        def delete_personal_data_by_patient = sql.execute("DELETE FROM PERSONAL_DATA_PATIENT WHERE PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_patient_status_history = sql.execute("DELETE FROM CORE_PATIENT_STATUS_HISTORY WHERE GROUP_ID IN (select id from core_protocol_group where protocol_id = $study) OR PATIENT_ID IN (SELECT ID FROM CORE_PATIENT WHERE PROTOCOL_ID = $study)")
        def delete_patient = sql.execute("DELETE FROM CORE_PATIENT WHERE PROTOCOL_ID = $study")

        def delete_study_responsibility = sql.execute("DELETE FROM STUDY_RESPONSIBILITY WHERE ID IN (SELECT STUDY_RESPONSIBILITY_ID FROM S_RESP_LOG_RESP WHERE LOG_ID IN (SELECT ID FROM STUDY_RESPONSIBILITY_LOG WHERE STUDY_ID = $study))")
        def delete_s_resp_log_resp = sql.execute("DELETE FROM S_RESP_LOG_RESP WHERE LOG_ID IN (SELECT ID FROM STUDY_RESPONSIBILITY_LOG WHERE STUDY_ID = $study)")
        def delete_study_responsibility_signature = sql.execute("DELETE FROM STUDY_RESPONSIBILITY_SIGNATURE WHERE ID IN (SELECT SIGNATURE_ID FROM STUDY_RESPONSIBILITY_LOG WHERE STUDY_ID = $study)")
        def delete_study_responsibility_log = sql.execute("DELETE FROM STUDY_RESPONSIBILITY_LOG WHERE STUDY_ID = $study")


        def delete_events = sql.execute("DELETE FROM TT_EVENT WHERE DAY_OF_VISIT_ID IN (SELECT ID FROM TT_DAY_OF_VISIT WHERE VISIT_ID IN (SELECT ID FROM TT_PROTOCOL_VISIT WHERE PROTOCOL_GROUP_ID IN (SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $study)))")
        def delete_arm_acctivities = sql.execute("DELETE FROM TT_ARM_ACTIVITY WHERE GROUP_ID IN (SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $study)")
        def delete_days = sql.execute("DELETE FROM TT_DAY_OF_VISIT WHERE VISIT_ID IN (SELECT ID FROM TT_PROTOCOL_VISIT WHERE PROTOCOL_GROUP_ID IN (SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_Id = $study))")
        def delete_protocol_visits  = sql.execute("DELETE FROM TT_PROTOCOL_VISIT WHERE PROTOCOL_GROUP_ID IN (SELECT ID FROM CORE_PROTOCOL_GROUP WHERE PROTOCOL_ID = $study)")
        def delete_arms = sql.execute("DELETE FROM CORE_PROTOCOl_GROUP WHERE PROTOCOL_ID = $study")
        def delete_protocols_by_employees = sql.execute("DELETE FROM CORE_EMPLOYEE_CORE_PROTOCOL WHERE PROTOCOL_ID = $study")
        def delete_protocols = sql.execute("DELETE from core_protocol where id = $study")
        def delete_protocol_details = sql.execute("DELETE from core_protocol_details WHERE id not in (select details_id from core_protocol)")
    }

    def deleteUser(String user) {
        def delete_protocols = sql.execute("DELETE from core_protocol where id = $user")
    }

    def deletePatient(String patient) {
		def delete_question = sql.execute("DELETE FROM TT_QUESTION WHERE PATIENT_ID = $patient OR SCHEDULLED_EVENT_ID IN (SELECT ID FROM TT_SCHEDULED_EVENT WHERE PATIENT_ID = $patient)")

		def delete_signture_log = sql.execute("DELETE FROM TT_SIGNATURE_LOG WHERE PATIENT_ID = $patient")
		def delete_inscan_qr_code = sql.execute("DELETE FROM INSCAN_QR_CODE_VISIT WHERE PATIENT_ID = $patient")
		def delete_schedule_signature = sql.execute("DELETE FROM TT_SCHEDULE_SIGNATURE WHERE PATIENT_ID = $patient")
		def delete_scheduled_event = sql.execute("DELETE FROM TT_SCHEDULED_EVENT WHERE PATIENT_ID = $patient")
		def delete_scheduled_event_log = sql.execute("DELETE FROM TT_SCHEDULED_EVENT_LOG WHERE SCHEDULED_EVENT_ID IN  (SELECT ID FROM TT_SCHEDULED_EVENT WHERE PATIENT_ID = $patient)")


		def delete_query = sql.execute("DELETE FROM TT_QUERY WHERE PATIENT_ID = $patient")
		def delete_sms_log = sql.execute("DELETE FROM NOTIFICATION_SMS_LOG WHERE PATIENT_ID = $patient")
		def delete_sms_queue = sql.execute("DELETE FROM NOTIFICATION_SMS_QUEUE WHERE PATIENT_ID = $patient")
		def delete_one_day_code = sql.execute("DELETE FROM ONE_DAY_CODE WHERE PATIENT_ID = $patient")
		def delete_statistics = sql.execute("DELETE FROM CLIENT_STATISTICS_BUFFER WHERE PATIENT_ID = $patient")
		def delete_doc_document = sql.execute("DELETE FROM DOC_DOCUMENT WHERE PATIENT_ID = $patient")
		def delete_doc_inform_concept = sql.execute("DELETE FROM DOC_INFORM_CONCEPT WHERE PATIENT_ID = $patient")
		def delete_doctor_adverse_event = sql.execute("DELETE FROM DOCTOR_ADVERSE_EVENT WHERE PATIENT_ID = $patient")
		def delete_doctor_appointment = sql.execute("DELETE FROM DOCTOR_APPOINTMENT WHERE PATIENT_ID = $patient")
		def delete_doctor_hospitalizations = sql.execute("DELETE FROM DOCTOR_HOSPITALIZATIONS WHERE PATIENT_ID = $patient")
		def delete_drug_order = sql.execute("DELETE FROM PH_DRUG_ORDER WHERE PATIENT_ID = $patient")

		def delete_personal_data_patient = sql.execute("DELETE FROM PERSONAL_DATA_PATIENT WHERE PATIENT_ID = $patient")
		def delete_patient_status_history = sql.execute("DELETE FROM CORE_PATIENT_STATUS_HISTORY WHERE PATIENT_ID = $patient")
		def delete_patient = sql.execute("DELETE FROM CORE_PATIENT WHERE ID = $patient")
    }

    def deleteActivity(String activity) {
		def delete_perm_group_activity = sql.execute("DELETE from s_perm_group_activity where activity_id = $activity")
		def delete_user_activity = sql.execute("DELETE from s_user_activity where activity_id = $activity")
		def delete_event = sql.execute("DELETE from tt_event where ARM_ACTIVITY_ID in (select id from tt_arm_activity where activity_id = $activity)")
		def delete_arm_activity = sql.execute("DELETE from tt_arm_activity where activity_id = $activity")
		def delete_question = sql.execute("DELETE from TT_QUESTION where SCHEDULED_EVENT_ID in (select id from tt_scheduled_event where activity_id = $activity or event_id in (select id from tt_event where arm_activity_id in (select id from tt_arm_activity where activity_id = $activity)))")
		def delete_scheduled_event = sql.execute("DELETE from tt_scheduled_event where activity_id =$activity or event_id in (select id from tt_event where arm_activity_id in (select id from tt_arm_activity where activity_id = $activity))")
		def delete_activity = sql.execute("DELETE from TT_ACTIVITY where id = $activity")
		def delete_activity_group = sql.execute("DELETE from TT_ACTIVITY_GROUP WHERE ID NOT IN (SELECT ACTIVITY_GROUP_ID FROM TT_ACTIVITY)")
    }
}
