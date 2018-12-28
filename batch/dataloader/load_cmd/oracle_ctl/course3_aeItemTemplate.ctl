load data
infile 'D:\temp\course3_aeItemTemplate.txt'
badfile 'D:\temp\course3_aeItemTemplate_bad.txt'
append into table aeItemTemplate
fields terminated by X'09'
(
itp_itm_id,
itp_ttp_id,
itp_tpl_id,
itp_create_timestamp date "YYYY-MM-DD HH24:MI:SS",
itp_create_usr_id
)
