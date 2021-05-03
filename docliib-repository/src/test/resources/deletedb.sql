DELETE
FROM doc_types
where name = 'test_type';

DELETE
FROM catalogs
where name = 'test_parent'
  OR parent = 'test_parent';

