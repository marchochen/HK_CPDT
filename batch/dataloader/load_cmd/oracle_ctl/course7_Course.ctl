load data
infile 'D:\temp\course7_Course.txt'
badfile 'D:\temp\course7_Course_bad.txt'
append into table Course
fields terminated by X'09'
(
cos_res_id,
cos_itm_id
)
