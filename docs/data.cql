CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.clarification_rules (
    country text,
    method text,
    parameter text,
    value text,
    primary key(country, method, parameter)
);

INSERT INTO ocds.clarification_rules (country, method, parameter, value) VALUES ('UA', 'micro', 'offset', '3');
INSERT INTO ocds.clarification_rules (country, method, parameter, value) VALUES ('UA', 'micro', 'interval', '3');


CREATE TABLE IF NOT EXISTS  ocds.clarification_period (
    cp_id text,
    stage text,
    owner text,
    start_date timestamp,
    end_date timestamp,
    primary key(cp_id, stage)
);

CREATE TABLE IF NOT EXISTS  ocds.clarification_enquiry (
    cp_id text,
    stage text,
    token_entity text,
    owner text,
    json_data text,
    is_answered boolean,
    primary key(cp_id, stage, token_entity)
);
