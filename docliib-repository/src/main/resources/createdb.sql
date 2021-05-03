CREATE TABLE IF NOT EXISTS catalogs
(
    id     serial,
    name   varchar(20) not null unique,
    parent varchar(20),
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
    type varchar(20) not null ,
    actual_version int default (0) not null,
    catalog_id int not null,
    PRIMARY KEY (id),
    FOREIGN KEY (type) references doc_types(name) on update  cascade on delete cascade,
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
    document_id int          not null,
    path        varchar(300) not null,
    PRIMARY KEY (id),
    FOREIGN KEY (document_id) references documents_versions (id) on update cascade on delete cascade
);
INSERT INTO catalogs(id, name)
    SELECT 1, '/'
WHERE NOT EXISTS (
    SELECT 1 FROM catalogs WHERE name='/'
);