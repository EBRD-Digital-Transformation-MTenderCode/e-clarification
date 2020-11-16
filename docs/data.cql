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

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval', '90');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '90');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval_before', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offsetExtended', '20');

INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval', '40');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offset', '40');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval_before', '20');
INSERT INTO ocds.clarification_rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offsetExtended', '20');

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

CREATE KEYSPACE IF NOT EXISTS clarification
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
        };

CREATE TABLE IF NOT EXISTS clarification.history
(
    command_id   TEXT,
    command_name TEXT,
    command_date TIMESTAMP,
    json_data    TEXT,
    PRIMARY KEY (command_id)
);

CREATE TABLE IF NOT EXISTS clarification.rules
(
    country        TEXT,
    pmd            TEXT,
    operation_type TEXT,
    parameter      TEXT,
    value          TEXT,
    PRIMARY KEY (country, pmd, operation_type, parameter)
);

CREATE TABLE IF NOT EXISTS clarification.periods
(
    cpid            TEXT,
    ocid            TEXT,
    owner           TEXT,
    start_date      TIMESTAMP,
    end_date        TIMESTAMP,
    tender_end_date TIMESTAMP,
    PRIMARY KEY (cpid, ocid)
);

CREATE TABLE IF NOT EXISTS  clarification.enquiries (
    cpid         TEXT,
    ocid         TEXT,
    token_entity TEXT,
    is_answered  BOOLEAN,
    json_data    TEXT,
    PRIMARY KEY(cpid, ocid, token_entity)
);

INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval', '777600');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offset', '777600');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'interval_before', '172800');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'OT', 'offsetExtended', '172800');

INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'interval', '345600');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'offset', '345600');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'interval_before', '172800');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'SV', 'offsetExtended', '172800');

INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval', '90');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offset', '90');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'interval_before', '20');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_OT', 'offsetExtended', '20');

INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval', '40');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offset', '40');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'interval_before', '20');
INSERT INTO clarification.rules (country, pmd, parameter, value) VALUES ('MD', 'TEST_SV', 'offsetExtended', '20');

DROP TABLE IF EXISTS clarification.history;

CREATE TABLE IF NOT EXISTS clarification.history
(
    command_id   TEXT,
    command_name TEXT,
    command_date TIMESTAMP,
    json_data    TEXT,
    PRIMARY KEY (command_id, command_name)
);

ALTER TABLE clarification.periods DROP tender_end_date;