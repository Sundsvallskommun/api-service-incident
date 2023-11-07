DROP TABLE IF EXISTS Status;
DROP TABLE IF EXISTS Categories;

ALTER TABLE IF EXISTS Attachments_SEQ
    RENAME TO attachment_sequence;

ALTER TABLE IF EXISTS Attachments
    RENAME TO attachment;

ALTER TABLE IF EXISTS Errands
    RENAME TO incident;

ALTER TABLE IF EXISTS attachment
    RENAME COLUMN IF EXISTS ID TO id;

ALTER TABLE IF EXISTS attachment
    RENAME COLUMN IF EXISTS IncidentId TO incident_id;

ALTER TABLE IF EXISTS attachment
    RENAME COLUMN IF EXISTS Created TO created;

ALTER TABLE IF EXISTS attachment
    RENAME COLUMN IF EXISTS mimetype TO mime_type;

ALTER TABLE IF EXISTS attachment
    RENAME COLUMN IF EXISTS Created TO created;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS externalCaseId TO external_case_id;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS PersonId TO person_id;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Created TO created;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS PhoneNumber TO phone_number;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Email TO email;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS ContactMethod TO contact_method;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Updated TO updated;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Description TO description;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS MapCoordinates TO coordinates;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Feedback TO feedback;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Status TO status;

ALTER TABLE IF EXISTS incident
    MODIFY person_id VARCHAR(36) NOT NULL;

ALTER TABLE IF EXISTS incident
    MODIFY created DATETIME NOT NULL;

ALTER TABLE IF EXISTS incident
    MODIFY updated DATETIME NOT NULL;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS Category TO category_id;

ALTER TABLE IF EXISTS incident
    MODIFY status ENUM('INSKICKAT', 'KLART', 'KOMPLETTERAD', 'SPARAT', 'UNDER_BEHANDLING',
    'VANTAR_KOMPLETTERING', 'ARKIVERAD', 'ERROR');

ALTER TABLE IF EXISTS attachment
    MODIFY created DATETIME NOT NULL;

ALTER TABLE IF EXISTS attachment
    MODIFY incident_id VARCHAR(36) NOT NULL;

ALTER TABLE IF EXISTS attachment
    MODIFY file LONGTEXT NOT NULL;

ALTER TABLE IF EXISTS incident
    DROP COLUMN IF EXISTS ID;

ALTER TABLE IF EXISTS incident
    RENAME COLUMN IF EXISTS IncidentId TO id;

ALTER TABLE IF EXISTS incident
    ADD PRIMARY KEY (id);






