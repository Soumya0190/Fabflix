DELIMITER $$
CREATE PROCEDURE `ShowDBMetaData`(IN databaseName varchar(20))
BEGIN
select table_name, column_name, column_type,  is_nullable, 
REPLACE(column_key,'PRI','Primary Key') colkey, extra
 from information_schema.columns where table_schema =databaseName
 order by table_name,  ordinal_position;

END$$
DELIMITER ;
