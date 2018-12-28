load data
infile 'D:\temp\question3_Question.txt'
badfile 'D:\temp\question3_Question_bad.txt'
append into table Question
fields terminated by X'09'
(
que_res_id ,
que_xml,
que_score,
que_type,
que_int_count,
que_media_ind,
que_submit_file_ind
)
