
create table attachment (
                            id integer not null auto_increment,
                            created datetime(6),
                            category varchar(255),
                            extension varchar(255),
                            file LONGTEXT,
                            mime_type varchar(255),
                            name varchar(255),
                            note varchar(255),
                            primary key (id)
) engine=InnoDB;

create table category (
                          category_id integer not null auto_increment,
                          forward_to varchar(255),
                          label varchar(255),
                          subject varchar(255),
                          title varchar(255),
                          primary key (category_id)
) engine=InnoDB;

create table incident (
                          category_id integer,
                          created datetime(6),
                          updated datetime(6),
                          contact_method varchar(255),
                          coordinates varchar(255),
                          description varchar(255),
                          email varchar(255),
                          external_case_id varchar(255),
                          feedback varchar(255),
                          id varchar(255) not null,
                          person_id varchar(255),
                          phone_number varchar(255),
                          status enum ('ARKIVERAD','ERROR','INSKICKAT','KLART','KOMPLETTERAD','SPARAT','UNDER_BEHANDLING','VANTAR_KOMPLETTERING'),
                          primary key (id)
) engine=InnoDB;

create table incident_attachments (
                                      attachment_id integer not null,
                                      incident_id varchar(255) not null
) engine=InnoDB;

alter table if exists category
    add constraint UK_category_title unique (title);

alter table if exists category
    add constraint UK_category_label unique (label);

alter table if exists incident_attachments
    add constraint UK_qostmu0ye7fimfibt39agr5lj unique (attachment_id);

alter table if exists incident
    add constraint fk_incident_category
    foreign key (category_id)
    references category (category_id);

alter table if exists incident_attachments
    add constraint fk_attachment_incident
    foreign key (attachment_id)
    references attachment (id);

alter table if exists incident_attachments
    add constraint fk_incident_attachment
    foreign key (incident_id)
    references incident (id);
