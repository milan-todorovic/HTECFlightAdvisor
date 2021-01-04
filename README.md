# Flight advisor repository

## Usage

Project is expected to be deployed under Tomcat server. Tomcat 9.0.41 was
used in test phase. Together with this Java 11 was used in order to develop
solution. 
Required route and airport files are in resource section of project, and no 
addition configuration of file read is required.
Data persistence (users, cities, comments) is provided by in memory H2 database.
All objects schema together with initial test data for H2 database is available
under resources/sql folder.
In order to test REST API, all required Postman collections are under
test/postmanTestCollections.
Project was developed with intelij idea.

## Documentation

This is a flight advisor implementation via REST API. Implementation is 
provided with Rest Easy. 

Note: Feature that provides return of city with corresponding comments will return
all comments if value is set to 0. Otherwise, it will return specified number of comments.

At preset moment there are some missing features:
* feature that will enable an administrator to import airports and routes file. This
is current done at request level.