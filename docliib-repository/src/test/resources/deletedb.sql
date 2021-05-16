DELETE
FROM doc_types
where name = 'test_type';

DELETE
FROM catalogs
where name = 'test_catalog';

DELETE
FROM doc_files
WHERE name = 'test_file';

DELETE
FROM sec_object;

INSERT INTO sec_object
VALUES (1,1,1);

INSERT INTO sec_permission
VALUES(1,1,1,7),
(2,1,2,1);
