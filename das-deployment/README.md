# DAS Rescue App Deployment

Stages to run the system:

1. `docker compose -p das --profile database up -d` brings mssql up
1. Provision the database with the files located in `./db` following their numeric order
1. `docker compose -p das --profile backend up -d` brings entity services up
1. `docker compose -p das --profile app up -d` brings api and frontend up