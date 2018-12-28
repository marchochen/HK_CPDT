load data
infile 'D:\temp\resfolder2_ObjectiveAccess.txt'
badfile 'D:\temp\resfolder2_ObjectiveAccess_bad.txt'
append into table ObjectiveAccess
fields terminated by X'09'
(
oac_obj_id,
oac_access_type,
oac_ent_id
)
