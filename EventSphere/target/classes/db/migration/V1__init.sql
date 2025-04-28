CREATE TABLE EVENTCATEGORY (
                               CATEGORYID NUMBER CONSTRAINT PK_CATEGORY PRIMARY KEY,
                               NAME VARCHAR2(50)
);


CREATE TABLE ROLE_T (
                        ROLEID NUMBER CONSTRAINT PK_ROLE PRIMARY KEY,
                        NAME VARCHAR2(10) UNIQUE
);

CREATE TABLE EVENT (
                       EVENTID NUMBER CONSTRAINT PK_EVENT PRIMARY KEY,
                       NAME VARCHAR2(50) UNIQUE,
                       TICKETCOUNT NUMBER(5),
                       CATEGORYID NUMBER,
                       LOCATION VARCHAR2(100),
                       EDATE DATE,
                       DESCRIPTION VARCHAR2(1000),
                       CONSTRAINT FK_CATEGORY FOREIGN KEY (CATEGORYID) REFERENCES EVENTCATEGORY(CATEGORYID),
);


CREATE TABLE LOGGEDUSER (
                            USERID NUMBER CONSTRAINT PK_USER PRIMARY KEY,
                            NAME VARCHAR2(50),
                            SURNAME VARCHAR2(80),
                            MAIL VARCHAR2(80) UNIQUE,
                            DESCRIPTION VARCHAR2(400),
                            ROLEID NUMBER,
                            PASSWORD VARCHAR2(255),
                            USERNAME VARCHAR2(50) UNIQUE,
                            CONSTRAINT FK_ROLE FOREIGN KEY (ROLEID) REFERENCES ROLE_T(ROLEID)
);

CREATE TABLE TICKET (
                        TICKETID NUMBER CONSTRAINT PK_TICKET PRIMARY KEY,
                        EVENTID NUMBER,
                        PAYMENTID NUMBER,
                        USERID NUMBER
                            CONSTRAINT FK_EVENT FOREIGN KEY (EVENTID) REFERENCES EVENT(EVENTID),
                        CONSTRAINT FK_PAYMENT FOREIGN KEY (PAYMENTID) REFERENCES PAYMENT(PAYMENTID),
                        CONSTRAINT FK_USERID FOREIGN KEY (USERID) REFERENCES LOGGEDUSER(USERID)
);

CREATE TABLE EVENTORGANIZE (
                               EORGANIZEID NUMBER CONSTRAINT PK_EORGANIZE PRIMARY KEY,
                               EVENTID NUMBER,
                               USERID NUMBER,
                               CONSTRAINT FK_EVENT_O FOREIGN KEY (EVENTID) REFERENCES EVENT(EVENTID),
                               CONSTRAINT FK_USER_O FOREIGN KEY (USERID) REFERENCES LOGGEDUSER(USERID)
);


CREATE TABLE PAYMENT (
                         PAYMENTID NUMBER CONSTRAINT PK_PAYMENT PRIMARY KEY,
                         PAYMENTDATE DATE,
                         PAYMETHOD VARCHAR2(100),
                         AMOUNT NUMBER(7,2),
);


CREATE TABLE COMMENT_T (
                           COMMENTID NUMBER CONSTRAINT PK_COMMENT PRIMARY KEY,
                           USERID NUMBER,
                           EVENTID NUMBER,
                           CONTENT VARCHAR2(1000),
                           CONSTRAINT FK_EVENT_C FOREIGN KEY (EVENTID) REFERENCES EVENT(EVENTID),
                           CONSTRAINT FK_USERID_C FOREIGN KEY (USERID) REFERENCES LOGGEDUSER(USERID)
);

CREATE TABLE SUB_EVENT (
                           EVENTID NUMBER,
                           USERID NUMBER,
                           CONSTRAINT PK_SUBSCRIBE PRIMARY KEY (EVENTID, USERID),
                           CONSTRAINT FK_EVENTID_S FOREIGN KEY (EVENTID) REFERENCES EVENT(EVENTID),
                           CONSTRAINT FK_USERID_S FOREIGN KEY (USERID) REFERENCES LOGGEDUSER(USERID)
);