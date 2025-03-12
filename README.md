# README #

[![Build Status](https://travis-ci.com/ONSdigital/address-index-api.svg?token=wrHpQMWmwL6kpsdmycnz&branch=develop)](https://travis-ci.com/ONSdigital/address-index-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/83c0fb7ca2e64567b0998848ca781a36)](https://www.codacy.com/app/Valtech-ONS/address-index-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ONSdigital/address-index-api&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/ONSdigital/address-index-api/branch/develop/graph/badge.svg)](https://codecov.io/gh/ONSdigital/address-index-api)

### What is this repository for? ###

Address Index is a Play Framework (2.9.1) application which matches addresses. 

The system works via large Elasticsearch (8.14.3) indices built primarily from AddressBase Premium data.

The input can be a complete address (from any source), and the system uses advanced data science techniques to determine the most likely matching AddressBase entries with UPRNs (Unique Property Reference Numbers).

Addresses can be matched one at a time or in batches.

Additional functions exist for postcode searching and partial address string matching for typeaheads.

The possibility of making the service available to other public sector bodies is being considered. For now it is possible to deploy a test copy of the service on a local machine using Docker containers.

### How do I run the Dockerised API? ###

Instructions for this now live in a new public repo called [AIMS DIY](https://github.com/ONSdigital/aims-diy)  

### How do I get set up for development? ###

1) Required Installations

    * Java 21 
    * sbt 1.9.9
    * Scala 2.13.13
    * Elasticsearch 8.14.3
    * An IDE such as IntelliJ is recommended

2) Create Project from GitHub (IntelliJ shown as example)

    * File, New, Project From Version Control, GitHub
    * Git Repository URL - select https://github.com/ONSdigital/aims-api.git
    * Parent Directory: any local drive, typically your IdeaProject directory
    * Directory Name: aims-api or aims-spark
    * Clone

    The references in the build.sbt are used to draw down additional components

3) Run

    * The project consists of an assembly of several subprojects - server, parsers and model - there used to be a demo-ui subproject but this has now been removed and there is a separate Python / Flask UI instead.
    * The list of subprojects can be seen by running sbt projects from the root of the project.
    * The list contains the project IDs that must be used for all sbt commands which require a Project ID to be supplied, for example:
    * sbt "project address-index-server" run

    * The application.conf of the server project points to an elastic search endpoint, this can be local or a server

    To run the API on your local machine:
       
    Port 9000 is default but you can override this
    i.e. from the root of the aims-api project run the following commands : 
        sbt "project address-index-server" "run 9001"

    Note that Play's autodeploy feature doesn't work with the API because of the CRFSuite executable. You have to exit out of sbt and rerun.

### How do I run unit tests ###

sbt test

will run them all, or you can select a subproject, or use testOnly feature to restrict what is run.

### Related Repos ###

[AIMS Spark](https://github.com/ONSdigital/aims-spark) - Apache Spark job used to create the Elasticsearch index

[Address Index Developers](https://github.com/ONSdigital/address-index-developers) - Flask web site for API users

[Address Index UI](https://github.com/ONSdigital/address-index-ui) - New Flask UI which replaces old demo-ui

### What if I just want to use the API ###

See [API Help and Swagger](api-definitions/readme.md)

