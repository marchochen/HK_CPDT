load data
infile 'D:\temp\course1_aeItem.txt'
badfile 'D:\temp\course1_aeItem_bad.txt'
append into table aeItem
fields terminated by X'09'
(
itm_id,
itm_title,
itm_code,
itm_type,
itm_appn_start_datetime date "YYYY-MM-DD HH24:MI:SS",
itm_appn_end_datetime date "YYYY-MM-DD HH24:MI:SS",
itm_xml CHAR(2000),
itm_status,
itm_owner_ent_id,
itm_create_timestamp date "YYYY-MM-DD HH24:MI:SS",
itm_create_usr_id,
itm_upd_timestamp date "YYYY-MM-DD HH24:MI:SS",
itm_upd_usr_id,
itm_create_run_ind,
itm_run_ind,
itm_apply_ind,
itm_deprecated_ind,
itm_qdb_ind,
itm_auto_enrol_qdb_ind,
itm_create_session_ind,
itm_session_ind,
itm_has_attendance_ind,
itm_ji_ind,
itm_completion_criteria_ind,
itm_retake_ind,
itm_tcr_id,
itm_access_type
)
