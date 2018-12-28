load data
infile 'D:\temp\user3_RegUserExtension.txt'
badfile 'D:\temp\user3_RegUserExtension_bad.txt'
append into table RegUserExtension
fields terminated by X'09'
(
urx_usr_ent_id
)
