DELETE
FROM types
where type = 'test_type';

DELETE
FROM catalog
where name = 'test_parent'
  OR parent = 'test_parent';

