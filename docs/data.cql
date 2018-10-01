CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.clarification_rules (
    country text,
    pmd text,
    parameter text,
    value text,
    primary key(country, pmd, parameter)
);

86400sec(day)

interval: 6 days = 518400
test  = 60 sec

interval_before: 2 days = 172800
test 20 sec

offset: 2 days = 172800
test 20 sec


INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval', '518400');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval', '60');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval_before', '172800');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval_before', '20');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offset', '172800');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '20');
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