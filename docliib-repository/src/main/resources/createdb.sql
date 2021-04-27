CREATE TABLE IF NOT EXISTS catalog
(
    id     serial,
    name   varchar(20) not null unique,
    parent varchar(20),
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS types
(
    id   serial,
    type varchar(30) not null unique,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS clients
(
    id       serial,
    login    varchar(30) not null unique,
    password varchar(30) not null,
    email    varchar(50) not null unique,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS document
(
    id           serial          not null,
    name         varchar(20)     not null,
    type         varchar(20)     not null,
    version      int default (0) not null,
    description  varchar(200),
    catalog      int,
    is_moderated bool,
    PRIMARY KEY (id),
    FOREIGN KEY (type) references types (type) on update cascade on delete cascade,
    FOREIGN KEY (catalog) references catalog (id) on update cascade on delete cascade
);
CREATE TABLE IF NOT EXISTS file
(
    id          serial,
    name        varchar(20)  not null unique,
    document_id int          not null,
    path        varchar(300) not null,
    PRIMARY KEY (id),
    FOREIGN KEY (document_id) references document (id) on update cascade on delete cascade
);
create table if not exists catalog_client
(
    catalog_id int not null,
    client_id  int not null,
    permission int default 1,
    foreign key (catalog_id) references catalog (id) on update cascade on delete cascade,
    foreign key (client_id) references clients (id) on update cascade on delete cascade
);
create table if not exists document_client
(
    document_id int         not null,
    client_id   int not null,
    permission  int default 1,
    foreign key (document_id) references document (id) on update cascade on delete cascade,
    foreign key (client_id) references clients (id) on update cascade on delete cascade
);


INSERT INTO catalog(id, name)
    SELECT nextval('catalog_id_seq'), '/'
WHERE NOT EXISTS (
    SELECT 1 FROM catalog WHERE name='/'
);
