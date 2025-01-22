# README #

[![Build Status](https://travis-ci.com/ONSdigital/address-index-api.svg?token=wrHpQMWmwL6kpsdmycnz&branch=develop)](https://travis-ci.com/ONSdigital/address-index-api)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/83c0fb7ca2e64567b0998848ca781a36)](https://www.codacy.com/app/Valtech-ONS/address-index-api?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ONSdigital/address-index-api&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/ONSdigital/address-index-api/branch/develop/graph/badge.svg)](https://codecov.io/gh/ONSdigital/address-index-api)

### What is this repository for? ###

Address Index is a Play Framework (2.9.1) application which matches addresses. 

The system works via large Elasticsearch (8.14.3) indices build primarily from AddressBase Premium data.

The input can be a complete address (from any source), and the system uses advanced data science techniques to determine the most likely matching AddressBase entries with UPRNs (Unique Property Reference Numbers).

Addresses can be matched one at a time or in batches.

Additional functions exist for postcode searching and partial address string matching for typeaheads.

There are plans to deploy the application as a service available outside ONS but progress on this is slow.

### How do I get a sneek peak of the API? ###

Docker images have been created which give a feel for the API. The Docker images include an Elasticsearch 8.15.3 cluster prebuilt with the required Address Index indices. The indices are a subset of AddressBase (the Exeter subset).
Another Docker image contains a version of the API that will work with the Elasticsearch indices. We have recently added a third image to deploy the improved Python UI. The ```docker-compose.yml``` file also contains a Kibana and Cerebro service to view the cluster. To get started:

1) Run ```docker-compose up``` on https://github.com/ONSdigital/aims-api/blob/main/docker-compose.yml

2) The cluster status can be viewed with either Cerebro or Kibana:

        Cerebro: http://localhost:1234
        and then http://es:9200
    
        Kibana: http://localhost:5601
    (the compose file has kibana commented out, you can uncomment it if you want to use it)
    
3) To run API calls open a browser or API testing app, Postman etc.

        http://localhost:9001/
        
    Endpoints can be found in the Swagger definition /openapi/swagger.json. View as HTML with examples here: 

   https://github.com/ONSdigital/aims-api/tree/main/api-definitions
    
    The ```ai-swagger.json``` can be copied into the Swagger Editor to view it: 
    
    https://editor.swagger.io/

4) The UI points to the local API and is available on

        http://localhost:5000/

5) Use of Docker alternatives: The docker-compose script works with Colima without modification. For Podman, you will need to enable podman compose (can be done from the Podman Desktop) and if this points to docker-compose rather than podman-compose, docker-compose up will work without changes.
       
### How do I load my own index? ###

This has its own README file in the [customdata](customdata/README.md) directory. 

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
