CREATE TABLE IF NOT EXISTS catalogs
(
    id     serial,
    name   varchar(20) not null unique,
    catalog_id int,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS doc_types
(
    id   serial,
    name varchar(30) not null unique,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS documents(
    id serial not null ,
    name varchar(20) not null,
    type_id int not null ,
    actual_version int default (0) not null,
    catalog_id int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (type_id) references doc_types(id) on update  cascade on delete cascade,
    FOREIGN KEY (catalog_id) references catalogs(id) on update cascade on  delete cascade
);
CREATE TABLE IF NOT EXISTS  documents_versions(
    id serial not null,
    document_id int not null,
    version int not null,
    description varchar(300),
    importance varchar(10) not null,
    is_moderated bool default false not null,
    PRIMARY KEY (id),
    FOREIGN KEY  (document_id) references documents(id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS doc_files
(
    id          serial,
    name        varchar(20)  not null,
    path        varchar(300) not null,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS files_versions
(
    file_id int not null,
    version_id int not null,
    PRIMARY KEY (file_id, version_id),
    FOREIGN KEY (file_id) references doc_files(id) on update cascade on delete cascade,
    FOREIGN KEY (version_id) references documents_versions(id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS clients
(
    id serial,
    login varchar,
    password varchar,
    PRIMARY KEY (id)
);

INSERT INTO catalogs(id, name, catalog_id)
    SELECT 1, '/',0
WHERE NOT EXISTS (
    SELECT 1 FROM catalogs WHERE name='/'
);
CREATE TABLE IF NOT EXISTS authority
(
    id serial,
    name varchar not null,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS client_authority
(
    client_id int not null ,
    authority_id int not null,
    primary key (client_id,authority_id),
    FOREIGN KEY (client_id) REFERENCES clients(id) on delete cascade on update cascade,
    FOREIGN KEY (authority_id) REFERENCES authority(id) on delete  cascade  on UPDATE  cascade
);

INSERT INTO authority (id, name) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN') ON CONFLICT DO NOTHING ;

INSERT INTO clients VALUES (1,'root', '$2a$10$McsGXLvYFziE0VXSbDsp7.ITYb61GxuMVRZKT3m7kjieto5yaI5GG')
ON CONFLICT DO NOTHING ;

INSERT INTO clients VALUES (2,'user', '$$2a$10$WCE1xwJawWw.y2rkR0F1HutQi9cfS5zeCMt4CBiyWtqGmqHhZD9FK')
ON CONFLICT DO NOTHING ;

INSERT INTO client_authority(client_id, authority_id)
VALUES (1,2) ON CONFLICT DO NOTHING;

INSERT INTO client_authority(client_id, authority_id)
VALUES (2,1) ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS sec_object_classes
(
    id   serial,
    name varchar(255) not null,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sec_object
(
    id              serial,
    object_table_id int not null,
    class_id        int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (class_id) REFERENCES sec_object_classes (id) on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS sec_permission
(
    id         serial,
    object_id  int not null,
    client_id  int not null,
    permission int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients (id) on update cascade on delete cascade,
    FOREIGN KEY (object_id) REFERENCES sec_object (id) on update cascade on delete cascade
);

INSERT INTO sec_object_classes
VALUES (1, 'Catalog')
ON CONFLICT DO NOTHING;

INSERT INTO sec_object_classes
VALUES (2, 'Document')
ON CONFLICT DO NOTHING;

INSERT INTO sec_object_classes
VALUES (3, 'DocumentFile')
ON CONFLICT DO NOTHING;

INSERT INTO sec_object
VALUES (1, 1, 1)
ON CONFLICT DO NOTHING;

INSERT INTO sec_permission
VALUES (1, 1, 1, 7)
ON CONFLICT DO NOTHING;

INSERT INTO sec_permission
VALUES (2, 1, 2, 1)
ON CONFLICT DO NOTHING;


