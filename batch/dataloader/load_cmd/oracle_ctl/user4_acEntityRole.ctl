load data
infile 'D:\temp\user4_acEntityRole.txt'
badfile 'D:\temp\user4_acEntityRole_bad.txt'
append into table acEntityRole
fields terminated by X'09'
(
erl_ent_id,
erl_rol_id,
erl_creation_timestamp date "YYYY-MM-DD HH24:MI:SS",
erl_eff_start_datetime date "YYYY-MM-DD HH24:MI:SS",
erl_eff_end_datetime date "YYYY-MM-DD HH24:MI:SS"
)
