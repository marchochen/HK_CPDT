load data
infile 'D:\temp\question2_ResourceObjective.txt'
badfile 'D:\temp\question2_ResourceObjective_bad.txt'
append into table ResourceObjective
fields terminated by X'09'
(
rob_res_id,
rob_obj_id
)
