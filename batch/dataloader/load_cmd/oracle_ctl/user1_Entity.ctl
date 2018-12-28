load data
infile 'D:\temp\user1_Entity.txt'
badfile 'D:\temp\user1_Entity_bad.txt'
append into table Entity
fields terminated by X'09'
(
ent_id,
ent_type,
ent_upd_date date "YYYY-MM-DD HH24:MI:SS",
ent_syn_ind
)
