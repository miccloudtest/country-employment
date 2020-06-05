CREATE TABLE country_master (
    country_id     NUMBER(10, 0) PRIMARY KEY,
    country_name   VARCHAR(20)
)

CREATE TABLE state_master (
    state_id     NUMBER(10, 0),
    state_name   VARCHAR2(20),
    country_id NOT NULL,
    PRIMARY KEY ( state_id ),
    FOREIGN KEY ( country_id )
        REFERENCES country_master ( country_id )
)

SELECT
    c.country_id,
    c.country_name,
    s.state_id,
    s.state_name
FROM
    country_master   c
    INNER JOIN state_master     s ON c.country_id = s.country_id

CREATE TABLE Country_State_Mapping_Data (
    state_id                   NUMBER(10, 0),
    country_id                   NUMBER(10, 0),
    state_area              NUMBER(10, 0),
    state_num_employed    NUMBER(19, 0),
    state_population        NUMBER(19, 0),
    created_by varchar2(50),
    modified_by varchar2(50),
    created_ts DATE,
    modified_ts DATE,
    is_deleted        VARCHAR2(10 CHAR),
    constraint PK_COUNTRY_STATE_ID primary key (country_id, state_id)
)
ALTER TABLE Country_State_Mapping_Data
ADD employment_rate FLOAT NOT NULL;


insert into country_master VALUES(1,'India');
insert into country_master VALUES(2,'Nepal');
insert into country_master VALUES(3,'Pakistan');

insert into state_master VALUES(1,'Andhra Pradesh',1);
insert into state_master VALUES(2,'Arunachal Pradesh',1);
insert into state_master VALUES(3,'Assam',1);
insert into state_master VALUES(4,'Bihar',1);
insert into state_master VALUES(5,'Chhattisgarh',1);
insert into state_master VALUES(6,'Goa',1);
insert into state_master VALUES(7,'Gujarat',1);
insert into state_master VALUES(8,'Haryana',1);
insert into state_master VALUES(9,'Himachal Pradesh',1);
insert into state_master VALUES(10,'Jammu Kashmir',1);
insert into state_master VALUES(11,'Jharkhand',1);
insert into state_master VALUES(12,'Karnataka',1);
insert into state_master VALUES(13,'Kerala',1);
insert into state_master VALUES(14,'Madhya Pradesh',1);
insert into state_master VALUES(15,'Maharashtra',1);
insert into state_master VALUES(16,'Manipur',1);
insert into state_master VALUES(17,'Meghalaya',1);
insert into state_master VALUES(18,'Mizoram',1);
insert into state_master VALUES(19,'Nagaland',1);
insert into state_master VALUES(20,'Odisha',1);
insert into state_master VALUES(21,'Punjab',1);
insert into state_master VALUES(22,'Rajasthan',1);
insert into state_master VALUES(23,'Sikkim',1);
insert into state_master VALUES(24,'Tamil Nadu',1);
insert into state_master VALUES(25,'Tripura',1);
insert into state_master VALUES(26,'Uttar Pradesh',1);
insert into state_master VALUES(27,'Uttarakhand',1);
insert into state_master VALUES(28,'West Bengal',1);
insert into state_master VALUES(29,'Andman Nicobar',1);
insert into state_master VALUES(30,'Chandigarh',1);
insert into state_master VALUES(32,'Dadar Nagar Haveli',1);
insert into state_master VALUES(49,'Daman Diu',1);
insert into state_master VALUES(33,'Delhi ',1);
insert into state_master VALUES(35,'Lakshadweep',1);
insert into state_master VALUES(36,'Puducherry',1);
insert into state_master VALUES(37,'Koshi Pradesh',2);
insert into state_master VALUES(38,'Madesh',2);
insert into state_master VALUES(39,'Bagmati Pradesh',2);
insert into state_master VALUES(40,'Gandaki Pradesh',2);
insert into state_master VALUES(41,'Lumbini Pradesh',2);
insert into state_master VALUES(42,'Karnali Pradesh',2);
insert into state_master VALUES(43,'Sudurpashchim',2);
insert into state_master VALUES(44,'Karachi',3);
insert into state_master VALUES(45,'Lohore',3);
insert into state_master VALUES(46,'Rawalpindi',3);
insert into state_master VALUES(47,'State-A',3);
insert into state_master VALUES(48,'State-B',3);