CREATE TABLE COUNTRY
   ( "COUNTRY" VARCHAR2(255 CHAR) NOT NULL ENABLE,
PRIMARY KEY ("COUNTRY")
  )


  CREATE TABLE STATES
   ( "STATE_NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE,
"AREA" NUMBER(10,0),
"EMPLOYMENT_RATE" FLOAT(126),
"IS_DELETED" VARCHAR2(255 CHAR),
"LAST_UPDATED_TS" DATE,
"NO_OF_EMPLOYED" NUMBER(19,0),
"POPULATION" NUMBER(19,0),
"COUNTRY" VARCHAR2(255 CHAR) NOT NULL ENABLE,
PRIMARY KEY ("STATE_NAME"),
CONSTRAINT "country_state_cons" FOREIGN KEY ("COUNTRY")
  REFERENCES "COUNTRY" ("COUNTRY") ENABLE
   );

 CREATE TABLE COUNTRY_STATE_MASTER
   ( "COUNTRY_NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE,
"STATE_NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE,
PRIMARY KEY ("COUNTRY_NAME", "STATE_NAME")
     );

insert into country_state_master VALUES('India','Andhra Pradesh');
insert into country_state_master VALUES('India','Arunachal Pradesh');
insert into country_state_master VALUES('India','Assam');
insert into country_state_master VALUES('India','Bihar');
insert into country_state_master VALUES('India','Chhattisgarh');
insert into country_state_master VALUES('India','Goa');
insert into country_state_master VALUES('India','Gujarat');
insert into country_state_master VALUES('India','Haryana');
insert into country_state_master VALUES('India','Himachal Pradesh');
insert into country_state_master VALUES('India','Jammu Kashmir');
insert into country_state_master VALUES('India','Jharkhand');
insert into country_state_master VALUES('India','Karnataka');
insert into country_state_master VALUES('India','Kerala');
insert into country_state_master VALUES('India','Madhya Pradesh');
insert into country_state_master VALUES('India','Maharashtra');
insert into country_state_master VALUES('India','Manipur');
insert into country_state_master VALUES('India','Meghalaya');
insert into country_state_master VALUES('India','Mizoram');
insert into country_state_master VALUES('India','Nagaland');
insert into country_state_master VALUES('India','Odisha');
insert into country_state_master VALUES('India','Punjab');
insert into country_state_master VALUES('India','Rajasthan');
insert into country_state_master VALUES('India','Sikkim');
insert into country_state_master VALUES('India','Tamil Nadu');
insert into country_state_master VALUES('India','Tripura');
insert into country_state_master VALUES('India','Uttar Pradesh');
insert into country_state_master VALUES('India','Uttarakhand');
insert into country_state_master VALUES('India','West Bengal');
insert into country_state_master VALUES('India','Andman Nicobar');
insert into country_state_master VALUES('India','Chandigarh');
insert into country_state_master VALUES('India','Dadar Nagar Haveli');
insert into country_state_master VALUES('India','Daman  Diu');
insert into country_state_master VALUES('India','Delhi');
insert into country_state_master VALUES('India','Lakshadweep');
insert into country_state_master VALUES('India','Puducherry');
insert into country_state_master VALUES('Nepal','Koshi Pradesh');
insert into country_state_master VALUES('Nepal','Madesh');
insert into country_state_master VALUES('Nepal','Bagmati Pradesh');
insert into country_state_master VALUES('Nepal','Gandaki Pradesh');
insert into country_state_master VALUES('Nepal','Lumbini Pradesh');
insert into country_state_master VALUES('Nepal','Karnali Pradesh');
insert into country_state_master VALUES('Nepal','Sudurpashchim Pradesh');
insert into country_state_master VALUES('Pakistan','Karachi');
insert into country_state_master VALUES('Pakistan','Lohore');
insert into country_state_master VALUES('Pakistna','Rawalpindi');
insert into country_state_master VALUES('Pakistan','State-A');
insert into country_state_master VALUES('Pakisthan','State-B');
