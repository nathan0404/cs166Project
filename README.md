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
   
   public static void Query1(EmbeddedSQL esql){
      try{
         String query = "SELECT sname, COUNT(pid) FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid GROUP BY sname";

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query1

   public static void Query2(EmbeddedSQL esql){
      try{
         String query = "SELECT sname, COUNT(pid) FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid GROUP BY sname HAVING COUNT(pid) >= 3;";

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query2

   public static void Query3(EmbeddedSQL esql){
      try{
         String query = "SELECT sname, COUNT(catalog.pid) FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE color = 'Green' GROUP BY sname;";

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query3

   public static void Query4(EmbeddedSQL esql) {
      try {
          String query = "SELECT sname, MAX(catalog.cost) FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE sname IN (SELECT suppliers.sname FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE color = 'Red' GROUP BY sname INTERSECT SELECT suppliers.sname FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE color = 'Green' GROUP BY sname) GROUP BY sname;";
  
          int rowCount = esql.executeQuery(query);
          System.out.println("total row(s): " + rowCount);
      } catch (Exception e) {
          System.err.println(e.getMessage());
      }
  } // end Query4
  
   public static void Query5(EmbeddedSQL esql){
      try{
         String query = "SELECT DISTINCT parts.pname FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE cost < ";
         System.out.print("\tEnter cost: $");
         String input = in.readLine();
         query += input;

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query5

   public static void Query6(EmbeddedSQL esql){
      try{
         String query = "SELECT DISTINCT suppliers.address FROM suppliers JOIN catalog ON catalog.sid = suppliers.sid JOIN parts ON catalog.pid = parts.pid WHERE parts.pname = ";
         System.out.print("\n Enter Product name:");
         String input = in.readLine();
         input = "\'" + input + "\'";
         query += input;

         int rowCount = esql.executeQuery(query);
         System.out.println ("total row(s): " + rowCount);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end Query6

}//end EmbeddedSQL
