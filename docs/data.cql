CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.clarification_rules (
    country text,
    pmd text,
    parameter text,
    value text,
    primary key(country, pmd, parameter)
);

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

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval', '777600');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offset', '777600');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval_before', '172800');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offsetExtended', '172800');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'interval', '345600');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'offset', '345600');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'interval_before', '172800');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'offsetExtended', '172800');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval', '60');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval_before', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offsetExtended', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '60');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval', '60');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval_before', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offsetExtended', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offset', '60');

//86400sec(day) * 6 = 518400
//60 sec (min)

//86400sec(day) * 5 = 432000
//50 sec (min)

//86400sec(day) * 10 = 864000
//100 sec (min)

//86400sec(day) * 2 = 172800
//20 sec (min)

//86400sec(day) * 9 = 777600 (OT)
//86400sec(day) * 4 = 345600 (SV)