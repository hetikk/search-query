CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

--
-- Define tables
--

CREATE TYPE document_type AS ENUM ('WORD', 'EXCEL');

CREATE TABLE document
(
    document_id   uuid                        default gen_random_uuid() PRIMARY KEY,
    title         varchar(255)                              NOT NULL,
    created_at    timestamp without time zone default now() NOT NULL,
    document_type document_type                             NOT NULL,
    is_deleted    boolean                                   NOT NULL,
    deleted_at    timestamp without time zone default null
);

CREATE UNIQUE INDEX title_idx ON document (title);

CREATE TABLE document_content
(
    content_id  uuid default gen_random_uuid() PRIMARY KEY,
    document_id uuid REFERENCES document (document_id),
    content     varchar(255) NOT NULL
);

CREATE UNIQUE INDEX document_idx ON document_content (document_id);

--
-- Fill tables
--

INSERT INTO document (document_id, title, created_at, document_type, is_deleted, deleted_at)
VALUES ('aeb1adf5-2644-482c-a5d3-29848205ad0c', 'Document1', '2024-09-10 23:00:00'::timestamp without time zone, 'WORD', true, '2024-09-15 00:00:00'::timestamp without time zone),
       ('16b82ef3-4050-4fe7-9553-194ee97e371b', 'Document2', '2024-09-10 23:30:00'::timestamp without time zone, 'EXCEL', false, null),
       ('62794b77-83d3-40d8-adad-2542cbd79670', 'Document3', '2024-09-10 23:30:30'::timestamp without time zone, 'WORD', true, '2024-09-16 00:00:00'::timestamp without time zone),
       ('e30e35c6-1c50-461c-a2f7-14ce19bb3c6b', 'Document4', '2024-09-11 00:00:00'::timestamp without time zone, 'EXCEL', false, null),
       ('aecce6c9-09e3-439b-8497-82d5f67f6241', 'Document5', '2024-09-11 01:00:00'::timestamp without time zone, 'WORD', true, '2024-09-17 00:00:00'::timestamp without time zone);

INSERT INTO document_content (document_id, content)
VALUES ('aeb1adf5-2644-482c-a5d3-29848205ad0c', 'Content1'),
       ('16b82ef3-4050-4fe7-9553-194ee97e371b', 'Content2'),
       ('62794b77-83d3-40d8-adad-2542cbd79670', 'Content3'),
       ('e30e35c6-1c50-461c-a2f7-14ce19bb3c6b', 'Content4'),
       ('aecce6c9-09e3-439b-8497-82d5f67f6241', 'Content5');
