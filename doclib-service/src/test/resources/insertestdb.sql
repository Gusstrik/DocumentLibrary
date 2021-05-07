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

INSERT INTO doc_files (id, name, document_id,path)
VALUES (nextval('doc_files_id_seq'),'test_file',1,'test_path')
on conflict do nothing;