load data
infile 'D:\temp\course8_ResourcePermission.txt'
badfile 'D:\temp\course8_ResourcePermission_bad.txt'
append into table ResourcePermission
fields terminated by X'09'
(
rpm_res_id,
rpm_ent_id,
rpm_rol_ext_id,
rpm_read,
rpm_write,
rpm_execute
)
