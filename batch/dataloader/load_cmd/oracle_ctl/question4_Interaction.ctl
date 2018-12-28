load data
infile 'D:\temp\question4_Interaction.txt'
badfile 'D:\temp\question4_Interaction_bad.txt'
append into table Interaction
fields terminated by X'09'
(
int_res_id,
int_order,
int_xml_outcome,
int_xml_explain
)
