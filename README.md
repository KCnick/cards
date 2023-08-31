# Project Setup
## Database
The database is running on MySQL v8. For ease, this has been dockerized. Just locate mysql folder at the root of the project. 
Run docker-compose up. 
The command will automatically download mysql v8 and create the database (card_db)

## APP 
At the root of the project, locate the file Dockerfile. Run the file to install maven and openJdk 17.
This will also install the desired dependencies as defined in the pom.xmk file 

## Tests
