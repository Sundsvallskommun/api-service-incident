ALTER TABLE IF EXISTS incident
    ADD COLUMN municipality_id VARCHAR(255);
CREATE INDEX idx_incident_municipality_id ON incident (municipality_id);

ALTER TABLE IF EXISTS category
    ADD COLUMN municipality_id VARCHAR(255);
CREATE INDEX idx_category_municipality_id ON category (municipality_id);