create schema if not exists humble_skin;

create table if not exists humble_skin.account
(
    created_time  timestamptz not null,
    modified_time timestamptz not null,
    id            uuid primary key,
    email         text unique not null,
    password      text        not null
);


DO
'
BEGIN
CREATE TYPE model_type AS enum (''default'', ''slim'');
EXCEPTION
WHEN duplicate_object THEN null;
END
';

create table if not exists humble_skin.character
(
    created_time  timestamptz                                     not null,
    modified_time timestamptz                                     not null,
    id            uuid primary key,
    account_id    uuid unique references humble_skin.account (id) not null,
    name          text unique                                     not null,
    model         humble_skin.model_type                          not null
);

DO
'
BEGIN
CREATE TYPE texture_type AS enum (''skin'', ''cape'', ''elytra'');
EXCEPTION
WHEN duplicate_object THEN null;
END
';

create table if not exists humble_skin.texture
(
    created_time  timestamptz                                not null,
    modified_time timestamptz                                not null,
    id            uuid primary key,
    character_id  uuid references humble_skin.character (id) not null,
    type          humble_skin.texture_type                   not null,
    hash          text                                       not null,
    constraint unique_texture unique (character_id, type)
)