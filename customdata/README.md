### How do use the Docker demo with my own ABP Index? ###

First you have to create an Elasticsearch index with a subset of Local Authorities (selected via localCustodianCodes) using the instructions here

Then you need to run the alternative Docker Compose script in this directory

1) Run ```docker-compose up``` on https://github.com/ONSdigital/aims-api/blob/main/customdata/docker-compose.yml

2) The cluster status can be viewed with either Cerebro or Kibana:

        Cerebro: http://localhost:1234
        and then http://es:9200
    
        Kibana: http://localhost:5601

You will see that there are no indices in the cluster. This means the API will not work.

3) Restore your index to the cluster.

Create an empty directory on your local machine and copy your snapshot to it
The exact command depends on where the index is stored, the example below is for a GCS bucket

        gsutil -m cp -r gs://aims-test-index-bucket/* .

Now copy the snapshot to the data directory of the Elasticsearch cluster in Docker

        docker cp . elasticsearch:/usr/share/elasticsearch/data/snapshots

Now you can use Cerebro or Kibana to restore the index as shown below:

First create a repository that points to snapshot directory inside Docker
![Alt text](snapshot_cerebro.png "REST command to create repository")

Then restore the index from the snapshot
![Alt text](docker_cerebro_restore.png "Using Cerebro to restore index")

Then add some aliases to the index

        index_full_hist_current
        index_full_nohist_current
        index_full_hist_111
        index_full_nohist_111
        index_skinny_hist_current
        index_skinny_nohist_current
        index_skinny_hist_111
        index_skinny_nohist_111

4) Now restart the API as shown below (screen shot is from Docker Desktop)

![Alt text](docker_restart.png "Restarting API with Docker Desktop")

The API will now run on

        http://localhost:9001/

And only addresses inside your selected LAs will be found. In the example below, a search for ONS only finds the Fareham office

![Alt text](docker_api_example.png "Calling the API with a browser")

5) The UI should automatically detect that epoch 111 is available.

        http://localhost:5000/

Note that the "large bulk" facility does not work with the Docker system, you can only do batches of up to 5000.
