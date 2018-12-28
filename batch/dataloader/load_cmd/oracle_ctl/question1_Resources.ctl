load data
infile 'D:\temp\question1_Resources.txt'
badfile 'D:\temp\question1_Resources_bad.txt'
append into table Resources
fields terminated by X'09'
(
res_id,
res_title,
res_type,
res_subtype,
res_difficulty,
res_privilege,
res_usr_id_owner,
res_status,
res_create_date date "YYYY-MM-DD HH24:MI:SS",
res_upd_user,
res_upd_date date "YYYY-MM-DD HH24:MI:SS"
)
