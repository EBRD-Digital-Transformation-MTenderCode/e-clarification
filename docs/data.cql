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
    start_date timestamp,
    end_date timestamp,
    owner text,
    tender_period_end_date timestamp,
    primary key(cp_id, stage)
);

Query; CQL [
INSERT INTO clarification_period (cp_id,stage,end_date,owner,start_date,tender_period_end_date)
VALUES ('ocds-t1s2t3-UA-1515589018079','dzo',1517932512831,'ps',1515589010194,1518191712831);];
Expected 8 or 0 byte long for date (2);
nested exception is com.datastax.driver.core.exceptions.InvalidQueryException:
Expected 8 or 0 byte long for date (2)","path":"/period/save"}



select * from ocds.clarification_period;

CREATE TABLE IF NOT EXISTS  ocds.clarification_enquiry (
    oc_id text,
    is_answered boolean,
    enquiry_id IIUD,
    json_data text,
    primary key(oc_id, is_answered, enquiry_id)
)WITH CLUSTERING ORDER BY (is_answered DESC, enquiry_id DESC);

INSERT INTO ocds.clarification_enquiry (oc_id, is_answered, enquiry_id, json_data) VALUES ('1235456', false, '1235456', 'json');

select * from ocds.clarification_enquiry where cp_id='1235456' and is_answered=false;