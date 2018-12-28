load data
infile 'D:\temp\user6_EntityRelation.txt'
badfile 'D:\temp\user6_EntityRelation_bad.txt'
append into table EntityRelation
fields terminated by X'09'
(
ern_child_ent_id,
ern_ancestor_ent_id,
ern_order,
ern_type,
ern_parent_ind,
ern_syn_timestamp date "YYYY-MM-DD HH24:MI:SS",
ern_remain_on_syn,
ern_create_timestamp date "YYYY-MM-DD HH24:MI:SS",
ern_create_usr_id
)
