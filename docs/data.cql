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

//86400sec(day) * 10 = 864000
//100 sec (min)

//86400sec(day) * 2 = 172800
//20 sec (min)


INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval', '864000');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval', '100');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval_before', '518400');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval_before', '60');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offset', '518400');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '60');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offsetExtended', '172800');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offsetExtended', '20');


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

CREATE TABLE IF NOT EXISTS  ocds.clarification_history (
    operation_id text,
    command text,
    operation_date timestamp,
    json_data text,
    primary key(operation_id, command)
);