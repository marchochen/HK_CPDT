load data
infile 'D:\temp\course5_CourseCriteria.txt'
badfile 'D:\temp\course5_CourseCriteria_bad.txt'
append into table CourseCriteria
fields terminated by X'09'
(
ccr_pass_score,
ccr_duration,
ccr_pass_ind,
ccr_all_cond_ind,
ccr_create_timestamp date "YYYY-MM-DD HH24:MI:SS",
ccr_create_usr_id,
ccr_upd_timestamp date "YYYY-MM-DD HH24:MI:SS",
ccr_upd_usr_id,
ccr_type,
ccr_upd_method,
ccr_itm_id
)
