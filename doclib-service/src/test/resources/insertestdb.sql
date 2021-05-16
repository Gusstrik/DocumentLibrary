INSERT INTO doc_types (id, name)
VALUES (1, 'test_type')
on conflict (name) do nothing;

INSERT INTO catalogs (id, name, catalog_id)
VALUES (2, 'test_catalog', 1)
on conflict do nothing;

INSERT INTO documents (id, name, type_id, actual_version, catalog_id)
VALUES (1, 'test_doc', 1, 0,1)
on conflict do nothing;

INSERT INTO documents_versions(id, document_id,version,description,importance,is_moderated)
VALUES (1,1,0,'test description','IMPORTANT',false)
on conflict do nothing;

INSERT INTO doc_files (id, name,path)
VALUES (1,'test_file','test_path')
on conflict do nothing;

INSERT INTO sec_object
VALUES(2,1,3)
ON CONFLICT DO NOTHING;

INSERT INTO sec_permission
VALUES(3,2,1,0)
ON CONFLICT DO NOTHING;

INSERT INTO sec_permission
VALUES(4,2,2,0)
ON CONFLICT DO NOTHING;


INSERT INTO files_versions
VALUES (1,1)
on conflict do nothing;