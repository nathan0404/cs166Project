# cs166Project

* Go to the project directory that has the following folders/files: 
       data  java  README.txt  sql

* To create the database and load data run the following script: 
    source sql/scripts/create_db.sh

* To run the java program use the following script: 
    source java/scripts/compile.sh  

assuming for number 10 of airline mangament, "number of days the flight departed and arrived" means on time, 
where 1 is on time and 0 is not on time
also assuming flight means flightNumber not ID, becuase ID is unique

Assuming for Query 2 of Customers for finding Ticket Cost after given a Flight Number to provide all flights rather than submitting a specific date prior.


For different view menus i am assuming that roles to not stack, meaning if a pilot only have pilot queries.



example queries from lab 

   public static void QueryExample(EmbeddedSQL esql){
      try{
         String query = "SELECT * FROM Catalog WHERE cost < ";
         System.out.print("\tEnter cost: $");
         String input = in.readLine();
         query += input;

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end QueryExample
