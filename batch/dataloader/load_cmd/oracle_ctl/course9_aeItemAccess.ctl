load data
infile 'D:\temp\course9_aeItemAccess.txt'
badfile 'D:\temp\course9_aeItemAccess_bad.txt'
append into table aeItemAccess
fields terminated by X'09'
(
iac_itm_id,
iac_ent_id,
iac_access_type,
iac_access_id
)
