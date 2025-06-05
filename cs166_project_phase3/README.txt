* Go to the project directory that has the following folders/files: 
       data  java  README.txt  sql

* To create the database and load data run the following script: 
    source sql/scripts/create_db.sh

* To run the java program use the following script: 
    source java/scripts/compile.sh  


source java/scripts/run.sh gui

javac -cp java/lib/lanterna-3.1.1.jar:java/src java/src/LanternaUI.java java/src/AirlineManagement.java
