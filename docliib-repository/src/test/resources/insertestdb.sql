INSERT INTO types (id, type)
VALUES (nextval('types_id_seq'), 'test_type')
on conflict (type) do nothing;

INSERT INTO catalog (id, name, parent)
VALUES (2, 'test_parent', '/')
on conflict do nothing;

INSERT INTO catalog (id, name, parent)
VALUES (nextval('catalog_id_seq'), 'test_catalog',
        'test_parent')
on conflict do nothing;

INSERT INTO document (id, name, type, version, description, catalog, is_moderated)
VALUES (1, 'test_doc', 'test_type', 1, 'test_description',2, false)
on conflict do nothing;

INSERT INTO file (id, name, document_id,path)
VALUES (nextval('file_id_seq'),'test_file',1,'test_path')
on conflict do nothing;