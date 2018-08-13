CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.clarification_rules (
    country text,
    pmd text,
    parameter text,
    value text,
    primary key(country, pmd, parameter)
);

//86400sec(day) * 6 = 518400
//60 sec (min)

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'RT', 'offset', '518400');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offset', '518400');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_RT', 'offset', '60');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '60');


CREATE TABLE IF NOT EXISTS  ocds.clarification_period (
    cp_id text,
    stage text,
    owner text,
    start_date timestamp,
    end_date timestamp,
    tender_end_date timestamp,
    primary key(cp_id, stage)
);

CREATE TABLE IF NOT EXISTS  ocds.clarification_enquiry (
    cp_id text,
    stage text,
    token_entity uuid,
    json_data text,
    is_answered boolean,
    primary key(cp_id, stage, token_entity)
);
