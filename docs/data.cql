CREATE KEYSPACE IF NOT EXISTS ocds WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

CREATE TABLE IF NOT EXISTS  ocds.clarification_rules (
    country text,
    method text,
    parameter text,
    value text,
    primary key(country, method, parameter)
);

INSERT INTO ocds.enquiry_rules (country, method, parameter, value) VALUES ('UA', 'micro', 'offset', '3');
INSERT INTO ocds.enquiry_rules (country, method, parameter, value) VALUES ('UA', 'micro', 'interval', '3');


CREATE TABLE IF NOT EXISTS  ocds.clarification_period (
    oc_id text,
    start_date timestamp,
    end_date timestamp,
    primary key(oc_id)
);

CREATE TABLE IF NOT EXISTS  ocds.clarification_enquiry (
    oc_id text,
    is_answered boolean,
    enquiry_id IIUD,
    json_data text,
    primary key(oc_id, is_answered, enquiry_id)
)WITH CLUSTERING ORDER BY (is_answered DESC, enquiry_id DESC);

INSERT INTO ocds.clarification_enquiry (oc_id, is_answered, enquiry_id, json_data) VALUES ('1235456', false, '1235456', 'json');

select * from ocds.clarification_enquiry where cp_id='1235456' and is_answered=false;