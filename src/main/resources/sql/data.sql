--USERS SECTION
INSERT INTO USERS (FIRST_NAME, LAST_NAME, USERNAME, PASSWORD, ROLE, REGISTERED)
VALUES ('Admin', 'Admin', 'admin', 'admin', 'ADMIN', 1);

--CITY SECTION
INSERT INTO CITIES (NAME, COUNTRY, DESCRIPTION) VALUES ('TestCity', 'TestCountry', 'TestDescription');

--COMMENT SECTION
INSERT INTO COMMENTS (COMMENT, FK_CITY_ID, CREATED_DATE, MODIFIED_DATE)
VALUES ('This is comment for TestCity', 1, parsedatetime('01-01-2020', 'dd-MM-yyyy'), parsedatetime('01-01-2020', 'dd-MM-yyyy'));