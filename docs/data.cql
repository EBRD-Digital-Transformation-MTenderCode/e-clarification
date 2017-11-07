CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.enquiry_rules (
    iso text,
    offset int,
    primary key(iso)
);

INSERT INTO ocds.enquiry_rules (iso, offset) VALUES ('UA', 7);

CREATE TABLE IF NOT EXISTS  ocds.enquiry_period (
    oc_id text,
    start_date timestamp,
    end_date timestamp,
    primary key(oc_id)
);

CREATE TABLE IF NOT EXISTS  ocds.enquiry (
    oc_id text,
    enquiry_id UUID,
    json_data text,
    is_answered boolean,
    primary key(oc_id)
);