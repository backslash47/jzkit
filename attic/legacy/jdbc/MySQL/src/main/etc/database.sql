create table RESOURCE (
  RES_ID INTEGER PRIMARY KEY AUTO INCREMENT,
  RES_TITLE VARCHAR2(255)
);

create table PARTY (
  PARTY_ID INTEGER PRIMARY KEY AUTO INCREMENT,
  PARTY_NAME VARCHAR2(100),
  PARTY_DESCRIPTION TEXT
);

create table RESOURCE_PARTY (
  RP_RESOURCE_ID INTEGER, 
  RP_PARTY_ID INTEGER
);

create table SUBJECT (
  SUB_ID INTEGER PRIMARY KEY AUTO INCREMENT,
  SUB_NAME VARCHAR2(100)
);
