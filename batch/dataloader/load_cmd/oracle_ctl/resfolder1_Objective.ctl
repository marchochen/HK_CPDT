load data
infile 'D:\temp\resfolder1_Objective.txt'
badfile 'D:\temp\resfolder1_Objective_bad.txt'
append into table Objective
fields terminated by X'09'
(
obj_id,
obj_syl_id,
obj_type,
obj_desc,
obj_status,
obj_tcr_id
)
