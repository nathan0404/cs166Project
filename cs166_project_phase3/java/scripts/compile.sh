#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# compile the java program
javac -d $DIR/../classes $DIR/../src/AirlineManagement.java

#run the java program
#Use your database name, port number and login
java -cp $DIR/../classes:$DIR/../lib/pg73jdbc3.jar AirlineManagement $USER"_project_phase_3_DB" $PGPORT $USER