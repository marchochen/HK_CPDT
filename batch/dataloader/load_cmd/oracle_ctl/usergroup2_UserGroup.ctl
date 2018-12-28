load data
infile 'D:\temp\usergroup2_UserGroup.txt'
badfile 'D:\temp\usergroup2_UserGroup_bad.txt'
append into table UserGroup
fields terminated by X'09'
(
usg_ent_id,
usg_display_bil,
usg_ent_id_root
)
