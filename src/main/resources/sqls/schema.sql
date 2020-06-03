CREATE TABLE country (
    country VARCHAR2(20 CHAR)
        NOT NULL ENABLE,
    PRIMARY KEY ( "COUNTRY" )
)


CREATE TABLE state2 (
    id                NUMBER(10, 0)
        NOT NULL ENABLE,
    area              NUMBER(10, 0),
    is_deleted        VARCHAR2(10 CHAR),
    last_updated_ts   DATE,
    no_of_employed    NUMBER(19, 0),
    population        NUMBER(19, 0),
    state_name        VARCHAR2(50 CHAR),
    country           VARCHAR2(50 CHAR)
        NOT NULL ENABLE,
    PRIMARY KEY ( "ID" ),
    CONSTRAINT "country_state" FOREIGN KEY ( "COUNTRY" )
        REFERENCES "COUNTRY" ( "COUNTRY" )
    ENABLE
)



 CREATE TABLE country_state_master (
     id             NUMBER(10, 0) NOT NULL,
     country_name   VARCHAR2(20 CHAR),
     state_name     VARCHAR2(20 CHAR),
     PRIMARY KEY ( "ID" ),
     CONSTRAINT "UK_STATE" UNIQUE ( "STATE_NAME" )
 )

 CREATE SEQUENCE state_seq
   MINVALUE 1
   MAXVALUE 999999999999999999999999999
   START WITH 1
   INCREMENT BY 1
   CACHE 20;

 CREATE SEQUENCE country_state_map_seq
    MINVALUE 1
    MAXVALUE 999999999999999999999999999
    START WITH 1
    INCREMENT BY 1
    CACHE 20;


insert into country_state_master VALUES(1,'India','Andhra Pradesh');
insert into country_state_master VALUES(2,'India','Arunachal Pradesh');
insert into country_state_master VALUES(3,'India','Assam');
insert into country_state_master VALUES(4,'India','Bihar');
insert into country_state_master VALUES(5,'India','Chhattisgarh');
insert into country_state_master VALUES(6,'India','Goa');
insert into country_state_master VALUES(7,'India','Gujarat');
insert into country_state_master VALUES(8,'India','Haryana');
insert into country_state_master VALUES(9,'India','Himachal Pradesh');
insert into country_state_master VALUES(10,'India','Jammu Kashmir');
insert into country_state_master VALUES(11,'India','Jharkhand');
insert into country_state_master VALUES(12,'India','Karnataka');
insert into country_state_master VALUES(13,'India','Kerala');
insert into country_state_master VALUES(14,'India','Madhya Pradesh');
insert into country_state_master VALUES(15,'India','Maharashtra');
insert into country_state_master VALUES(16,'India','Manipur');
insert into country_state_master VALUES(17,'India','Meghalaya');
insert into country_state_master VALUES(18,'India','Mizoram');
insert into country_state_master VALUES(19,'India','Nagaland');
insert into country_state_master VALUES(20,'India','Odisha');
insert into country_state_master VALUES(21,'India','Punjab');
insert into country_state_master VALUES(22,'India','Rajasthan');
insert into country_state_master VALUES(23,'India','Sikkim');
insert into country_state_master VALUES(24,'India','Tamil Nadu');
insert into country_state_master VALUES(25,'India','Tripura');
insert into country_state_master VALUES(26,'India','Uttar Pradesh');
insert into country_state_master VALUES(27,'India','Uttarakhand');
insert into country_state_master VALUES(28,'India','West Bengal');
insert into country_state_master VALUES(29,'India','Andman Nicobar');
insert into country_state_master VALUES(30,'India','Chandigarh');
insert into country_state_master VALUES(32,'India','Dadar Nagar Haveli');
insert into country_state_master VALUES(32,'India','Daman  Diu');
insert into country_state_master VALUES(33,'India','Delhi');
insert into country_state_master VALUES(35,'India','Lakshadweep');
insert into country_state_master VALUES(36,'India','Puducherry');
insert into country_state_master VALUES(37,'Nepal','Koshi Pradesh');
insert into country_state_master VALUES(38,'Nepal','Madesh');
insert into country_state_master VALUES(39,'Nepal','Bagmati Pradesh');
insert into country_state_master VALUES(40,'Nepal','Gandaki Pradesh');
insert into country_state_master VALUES(41'Nepal','Lumbini Pradesh');
insert into country_state_master VALUES(42,'Nepal','Karnali Pradesh');
insert into country_state_master VALUES(43,'Nepal','Sudurpashchim Pradesh');
insert into country_state_master VALUES(44,'Pakistan','Karachi');
insert into country_state_master VALUES(45,'Pakistan','Lohore');
insert into country_state_master VALUES(46'Pakistna','Rawalpindi');
insert into country_state_master VALUES(47,'Pakistan','State-A');
insert into country_state_master VALUES(48'Pakisthan','State-B');
