load data
infile 'D:\temp\usergroup1_Entity.txt'
badfile 'D:\temp\usergroup1_Entity_bad.txt'
append into table Entity
fields terminated by X'09'
(
ent_id,
ent_type,
ent_upd_date date "YYYY-MM-DD HH24:MI:SS",
ent_ste_uid,
ent_syn_ind
)
