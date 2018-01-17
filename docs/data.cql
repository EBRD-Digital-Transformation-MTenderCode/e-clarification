CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.enquiry_rules (
    country text,
    method text,
    parameter text,
    value text,
    primary key(country, method, parameter)
);

INSERT INTO ocds.enquiry_rules (country, method, parameter, value) VALUES ('UA', 'rt', 'offset', '3');
INSERT INTO ocds.enquiry_rules (country, method, parameter, value) VALUES ('UA', 'rt', 'interval', '3');


CREATE TABLE IF NOT EXISTS  ocds.enquiry_period (
    cp_id text,
    owner text,
    stage text,
    start_date timestamp,
    end_date timestamp,
    enquiry_end_date timestamp,
    primary key(cp_id,stage)
);

CREATE TABLE IF NOT EXISTS  ocds.enquiry (
    cp_id text,
    owner text,
    enquiry_id UUID,
    json_data text,
    is_answered boolean,
    primary key(cp_id,enquiry_id)
)WITH CLUSTERING ORDER BY (enquiry_id DESC);
