load data
infile 'D:\temp\user2_RegUser.txt'
badfile 'D:\temp\user2_RegUser_bad.txt'
append into table RegUser
fields terminated by X'09'
(
usr_id,
usr_ent_id,
usr_pwd,
usr_display_bil,
usr_signup_date date "YYYY-MM-DD HH24:MI:SS",
usr_last_login_date date "YYYY-MM-DD HH24:MI:SS",
usr_status,
usr_upd_date date "YYYY-MM-DD HH24:MI:SS",
usr_ste_ent_id,
usr_ste_usr_id,
usr_approve_timestamp date "YYYY-MM-DD HH24:MI:SS",
usr_pwd_upd_timestamp date "YYYY-MM-DD HH24:MI:SS",
usr_syn_rol_ind,
usr_source
)
