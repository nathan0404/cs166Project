/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class AirlineManagement {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of AirlineManagement
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public AirlineManagement(String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end AirlineManagement

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQueryAndPrintResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
		 if(outputHeader){
			for(int i = 1; i <= numCol; i++){
			System.out.print(rsmd.getColumnName(i) + "\t");
			}
			System.out.println();
			outputHeader = false;
		 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close();
      return rowCount;
   }//end executeQuery

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the results as
    * a list of records. Each record in turn is a list of attribute values
    *
    * @param query the input query string
    * @return the query result as a list of records
    * @throws java.sql.SQLException when failed to execute the query
    */
   public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and saves the data returned by the query.
      boolean outputHeader = false;
      List<List<String>> result  = new ArrayList<List<String>>();
      while (rs.next()){
        List<String> record = new ArrayList<String>();
		for (int i=1; i<=numCol; ++i)
			record.add(rs.getString (i));
        result.add(record);
      }//end while
      stmt.close ();
      return result;
   }//end executeQueryAndReturnResult

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and returns the number of results
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
       // creates a statement object
       Statement stmt = this._connection.createStatement ();

       // issues the query instruction
       ResultSet rs = stmt.executeQuery (query);

       int rowCount = 0;

       // iterates through the result set and count nuber of results.
       while (rs.next()){
          rowCount++;
       }//end while
       stmt.close ();
       return rowCount;
   }

   /**
    * Method to fetch the last value from sequence. This
    * method issues the query to the DBMS and returns the current
    * value of sequence used for autogenerated keys
    *
    * @param sequence name of the DB sequence
    * @return current value of a sequence
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int getCurrSeqVal(String sequence) throws SQLException {
	Statement stmt = this._connection.createStatement ();

	ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
	if (rs.next())
		return rs.getInt(1);
	return -1;
   }

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            AirlineManagement.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if

      Greeting();
      AirlineManagement esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the AirlineManagement object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new AirlineManagement (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
            System.out.println("MAIN MENU");
            System.out.println("---------");
            System.out.println("1. Create user");
            System.out.println("2. Log in");
            System.out.println("9. < EXIT");
            String authorisedUser = null;
            switch (readChoice()){
               case 1: CreateUser(esql); break;
               case 2: authorisedUser = LogIn(esql); break;
               case 9: keepon = false; break;
               default : System.out.println("Unrecognized choice!"); break;
            }//end switch
            if (authorisedUser != null) {
               boolean usermenu = true;
               while(usermenu) {
                  System.out.println("MAIN MENU");
                  System.out.println("---------");

                  //**the following functionalities should only be able to be used by Management**
                  System.out.println("1. View Flights");
                  System.out.println("2. View Flight Seats");
                  System.out.println("3. View Flight Status");
                  System.out.println("4. View Flights of the day");  
                  System.out.println("5. View Full Order ID History");
                  System.out.println("6. View Customer Info");
                  System.out.println("7. View Plane Info");
                  System.out.println("8. View Repairs History");
                  System.out.println(".........................");

                  //**the following functionalities should only be able to be used by customers**
                  System.out.println("11. Search Flights");
                  System.out.println("12. Find Ticket Cost");
                  System.out.println("13. Find Airplane Type");
                  System.out.println("14. Make Reservation");
                  System.out.println(".........................");

                  //**the following functionalities should only be able to be used by Maintenance Staff**
                  System.out.println("15. List Repairs by Plane and Date Range");
                  System.out.println("16. List Maintenance Requests by Pilot");
                  System.out.println("17. Log Repair");
                  System.out.println(".........................");

                  //**the following functionalities should only be able to be used by Pilots**
                  System.out.println("18. Make Maintenance Request");
                  System.out.println(".........................");
                  System.out.println(".........................");

                  System.out.println("20. Log out");
                  switch (readChoice()){
                     case 1: feature1(esql); break;
                     case 2: feature2(esql); break;
                     case 3: feature3(esql); break;
                     case 4: feature4(esql); break;
                     case 5: feature5(esql); break;
                     case 6: feature6(esql); break;
                     case 7: feature7(esql); break;
                     case 8: feature8(esql); break;
                     case 9: feature9(esql); break;
                     case 10: feature10(esql); break;
                     case 11: feature11(esql); break;
                     case 12: feature12(esql); break;
                     case 13: feature13(esql); break;
                     case 14: feature14(esql); break;
                     case 15: feature15(esql); break;
                     case 16: feature16(esql); break;
                     case 17: feature17(esql); break;
                     case 18: feature18(esql); break;
                     case 20: usermenu = false; break;
                     default : System.out.println("Unrecognized choice!"); break;
                  }
              }
            }
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main

   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   /*
    * Creates a new user
    **/
   public static void CreateUser(AirlineManagement esql){
   }//end CreateUser


   /*
    * Check log in credentials for an existing user
    * @return User login or null is the user does not exist
    **/
   public static String LogIn(AirlineManagement esql){
      String input = "hi";
      /* 
      String input;
      do {
         System.out.println("\nEnter your name:");
         try { // read the String, parse it and break.
            input = in.readLine();
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      System.out.println("login successful");
      return input; */
      return input;
   }//end

// Rest of the functions definition go in here
/*1. Given a flight number, get the flight's schedule for the week
• A flight may be scheduled on multiple days in a week */
   public static void feature1(AirlineManagement esql) {
      try{
         String query = "SELECT * FROM Flight";
         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   /*2. Given a flight and a date, get (1) the number of seats still available and (2) number of seats
sold */
   public static void feature2(AirlineManagement esql) {
      try{
         System.out.println("Enter flight number in format (F***):");
         String FlightNumber = in.readLine();
         
         System.out.println("Enter flight date in format (5/5/25):");
         String Date = in.readLine();
         
         String query = "SELECT SeatsTotal, SeatsSold FROM FlightInstance " +
                     "WHERE FlightNumber = '" + FlightNumber + "' AND FlightDate = '" + Date + "'";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   /*3. Given a flight and date, find whether (1) the flight departed on time, and (2) arrived on time */
   public static void feature3(AirlineManagement esql) {
      try{
       System.out.println("Enter flight number in format (F***):");
         String FlightNumber = in.readLine();
         
         System.out.println("Enter flight date in format (5/5/25):");
         String Date = in.readLine();
         
         String query = "SELECT DepartedOnTime, ArrivedOnTime FROM FlightInstance " +
                     "WHERE FlightNumber = '" + FlightNumber + "' AND FlightDate = '" + Date + "'";
         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   /*4. Given a date, get all flights scheduled on that day 
    * 
    addd another query looking for past flights too
   */
   public static void feature4(AirlineManagement esql) {
      try{
         System.out.println("Enter day of the week in format (Monday):");
         String DayOfWeek = in.readLine();
         
         String query = "SELECT * FROM Schedule " +
                     "WHERE DayOfWeek = '" + DayOfWeek + "'";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   
   /*5. Given a flight and date, get a list of passengers who (1) made reservations, (2) are on the
waiting list, (3) actually flew on the flight (for flights already completed)  */
   public static void feature5(AirlineManagement esql) {
      try{
       System.out.println("Enter flight number in format (F***):");
         String FlightNumber = "F100"; //in.readLine();
         
         System.out.println("Enter flight date in format (5/5/25):");
         String Date = "5/5/25"; //in.readLine();
         
         String query = "SELECT ReservationID,CustomerID,Status FROM Reservation " +
               "WHERE FlightInstanceID = (" +
               "SELECT FlightInstance.FlightInstanceID FROM FlightInstance " +
               "WHERE FlightInstance.FlightNumber = '" + FlightNumber + "' " +
               "AND FlightInstance.FlightDate = '" + Date + "')";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   /* 6. Given a reservation number, retrieve information about the travelers under that number
• First & Last Name, Gender, Date of birth, Address, Phone number, Zip Code*/
   public static void feature6(AirlineManagement esql) {
      try{
       System.out.println("Enter a Reservation number in format (R0001):");
         String ReservationNum = "R0001"; //in.readLine();
         
         String query = "SELECT * FROM Customer " +
               "WHERE CustomerID = (" +
               "SELECT CustomerID FROM Reservation " +
               "WHERE ReservationID = '" + ReservationNum + "')";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }

   /*7. Given a plane number, get its make, model, age, and last repair date */
   public static void feature7(AirlineManagement esql) {
      try{
       System.out.println("Enter a Plane number in format (PL001):");
         String PlaneNum = "PL001"; //in.readLine();
         
         String query = "SELECT Make,Model,Year,LastRepairDate FROM Plane " +
               "WHERE PlaneID = '" + PlaneNum + "'";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   /* 8. Given a maintenance technician ID, list all repairs made by that person */
   public static void feature8(AirlineManagement esql) {
      try{
       System.out.println("Enter a Technician ID number in format (T001):");
         String TechnicianID = "T001"; //in.readLine();
         
         String query = "SELECT RepairID,PlaneID,RepairCode,RepairDate FROM Repair " +
               "WHERE TechnicianID = '" + TechnicianID + "'";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   /*9. Given a plane ID and a date range, list all the dates and the codes for repairs performed */
  public static void feature9(AirlineManagement esql) {
   try{
      System.out.println("Enter a Plane ID number in format (PL001):");
      String PlaneID = "PL001"; //in.readLine();

      System.out.println("Enter a Start Date in format (2025-04-09):");
      String StartDate = "2025-04-03"; //in.readLine();

      System.out.println("Enter a End Date in format (2025-04-09):");
      String EndDate = "2025-04-10"; //in.readLine();
      
      String query = "SELECT RepairID,RepairCode,RepairDate FROM Repair " +
            "WHERE PlaneID = '" + PlaneID + "' " +
            "AND RepairDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'";

      esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   /*10. Given a flight and a range of date (start date, end date), show the statistics of the flight:
   number of days the flight departed and arrived, number of sold and unsold tickets 
   FlightInstanceID,FlightNumber,FlightDate,DepartedOnTime,ArrivedOnTime,SeatsTotal,SeatsSold,NumOfStops,TicketCost*/
   public static void feature10(AirlineManagement esql) {
      try{
         System.out.println("Enter a Plane ID number in format (F100):");
         String FlightNumber = "F100"; //in.readLine();

         System.out.println("Enter a Start Date in format (5/5/25):");
         String StartDate = "5/5/25"; //in.readLine();

         System.out.println("Enter a End Date in format (5/5/25):");
         String EndDate = "6/1/25"; //in.readLine();
         
         String query = "SELECT " +
                  "COUNT(CASE WHEN DepartedOnTime IS TRUE THEN 1 END) AS departed_on_time, " + 
                  "COUNT(CASE WHEN ArrivedOnTime IS TRUE THEN 1 END) AS arrived_on_time, " +
                  "SUM(SeatsSold) AS Total_Sold_Seats, " +
                  "SUM(SeatsTotal - SeatsSold) AS Total_Unsold_Seats " +
                  "FROM FlightInstance " +
               "WHERE FlightNumber = '" + FlightNumber + "' " +
               "AND FlightDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'";

         esql.executeQueryAndPrintResult(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }
   public static void feature11(AirlineManagement esql) {
       // Customer: Given a destination and departure city, find all flights on a given date
       try {
           System.out.println("Enter Departure City:");
           String depCity = in.readLine();
           System.out.println("Enter Destination City:");
           String arrCity = in.readLine();
           System.out.println("Enter Date (YYYY-MM-DD):");
           String date = in.readLine();
           String query = "SELECT F.FlightNumber, S.DepartureTime, S.ArrivalTime, FI.NumOfStops, " +
               "ROUND(100.0 * SUM(CASE WHEN FI.DepartedOnTime AND FI.ArrivedOnTime THEN 1 ELSE 0 END) / COUNT(*), 2) AS OnTimePercentage " +
               "FROM Flight F " +
               "JOIN Schedule S ON F.FlightNumber = S.FlightNumber " +
               "JOIN FlightInstance FI ON F.FlightNumber = FI.FlightNumber " +
               "WHERE F.DepartureCity = '" + depCity + "' AND F.ArrivalCity = '" + arrCity + "' AND FI.FlightDate = '" + date + "' " +
               "GROUP BY F.FlightNumber, S.DepartureTime, S.ArrivalTime, FI.NumOfStops";
           int count = esql.executeQueryAndPrintResult(query);
           if (count == 0) {
               System.out.println("No flights found for the given criteria.");
           }
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature12(AirlineManagement esql) {
       // Customer: Given a flight number, find all ticket costs for that flight number
       try {
           System.out.println("Enter Flight Number (e.g., F100):");
           String flightNum = in.readLine();
           String query = "SELECT FlightDate, TicketCost FROM FlightInstance WHERE FlightNumber = '" + flightNum + "'";
           int count = esql.executeQueryAndPrintResult(query);
           if (count == 0) {
               System.out.println("No ticket costs found for the given flight number.");
           }
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature13(AirlineManagement esql) {
       // Customer: Given a flight number, find the airplane type (make and model)
       try {
           System.out.println("Enter Flight Number (e.g., F100):");
           String flightNum = in.readLine();
           String query = "SELECT P.Make, P.Model FROM Flight F JOIN Plane P ON F.PlaneID = P.PlaneID WHERE F.FlightNumber = '" + flightNum + "'";
           esql.executeQueryAndPrintResult(query);
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature14(AirlineManagement esql) {
       // Customer: Make a reservation for a flight (and get on the waitlist if full)
       try {
           System.out.println("Enter Customer ID:");
           String customerID = in.readLine();
           System.out.println("Enter Flight Number (e.g., F100):");
           String flightNum = in.readLine();
           System.out.println("Enter Date (YYYY-MM-DD):");
           String date = in.readLine();
           // Find the FlightInstanceID
           String findInstance = "SELECT FlightInstanceID, SeatsTotal, SeatsSold FROM FlightInstance WHERE FlightNumber = '" + flightNum + "' AND FlightDate = '" + date + "'";
           List<List<String>> result = esql.executeQueryAndReturnResult(findInstance);
           if (result.size() == 0) {
               System.out.println("No such flight instance found.");
               return;
           }
           String flightInstanceID = result.get(0).get(0);
           int seatsTotal = Integer.parseInt(result.get(0).get(1));
           int seatsSold = Integer.parseInt(result.get(0).get(2));
           String status = (seatsSold < seatsTotal) ? "reserved" : "waitlist";
           // Generate a new ReservationID (e.g., R0001)
           int newID = (int)(Math.random() * 10000);
           String reservationID = String.format("R%04d", newID);
           String insert = "INSERT INTO Reservation (ReservationID, CustomerID, FlightInstanceID, Status) VALUES ('" + reservationID + "', '" + customerID + "', '" + flightInstanceID + "', '" + status + "')";
           esql.executeUpdate(insert);
           System.out.println("Reservation made with status: " + status);
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature15(AirlineManagement esql) {
       // Maintenance Staff: Given a plane ID and a date range, list all the dates and codes for repairs performed
       try {
           System.out.println("Enter Plane ID (e.g., PL001):");
           String planeID = in.readLine();
           System.out.println("Enter Start Date (YYYY-MM-DD):");
           String startDate = in.readLine();
           System.out.println("Enter End Date (YYYY-MM-DD):");
           String endDate = in.readLine();
           String query = "SELECT RepairDate, RepairCode FROM Repair WHERE PlaneID = '" + planeID + "' AND RepairDate BETWEEN '" + startDate + "' AND '" + endDate + "'";
           esql.executeQueryAndPrintResult(query);
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature16(AirlineManagement esql) {
       // Maintenance Staff: Given a pilot ID, list all maintenance requests made by the pilot
       try {
           System.out.println("Enter Pilot ID (e.g., P001):");
           String pilotID = in.readLine();
           String query = "SELECT PlaneID, RepairCode, RequestDate FROM MaintenanceRequest WHERE PilotID = '" + pilotID + "'";
           int count = esql.executeQueryAndPrintResult(query);
           if (count == 0) {
               System.out.println("No maintenance requests found for this pilot.");
           }
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature17(AirlineManagement esql) {
       // Maintenance Staff: After each repair, make an entry showing plane ID, repair code, and date of repair
       try {
           System.out.println("Enter Plane ID (e.g., PL001):");
           String planeID = in.readLine();
           System.out.println("Enter Repair Code:");
           String repairCode = in.readLine();
           System.out.println("Enter Repair Date (YYYY-MM-DD):");
           String repairDate = in.readLine();
           System.out.println("Enter Technician ID (e.g., T001):");
           String techID = in.readLine();
           // Generate a unique RepairID
           String getMaxId = "SELECT COALESCE(MAX(RepairID), 0) + 1 FROM Repair";
           List<List<String>> idResult = esql.executeQueryAndReturnResult(getMaxId);
           String repairID = idResult.get(0).get(0);
           String insert = "INSERT INTO Repair (RepairID, PlaneID, RepairCode, RepairDate, TechnicianID) VALUES ('" + repairID + "', '" + planeID + "', '" + repairCode + "', '" + repairDate + "', '" + techID + "')";
           esql.executeUpdate(insert);
           System.out.println("Repair logged successfully.");
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
   public static void feature18(AirlineManagement esql) {
       // Pilot: Make maintenance request listing plane ID, repair code requested, and date of request
       try {
           System.out.println("Enter Plane ID (e.g., PL001):");
           String planeID = in.readLine();
           System.out.println("Enter Repair Code:");
           String repairCode = in.readLine();
           System.out.println("Enter Request Date (YYYY-MM-DD):");
           String requestDate = in.readLine();
           System.out.println("Enter Pilot ID (e.g., P001):");
           String pilotID = in.readLine();
           // Generate a unique RequestID
           String getMaxId = "SELECT COALESCE(MAX(RequestID), 0) + 1 FROM MaintenanceRequest";
           List<List<String>> idResult = esql.executeQueryAndReturnResult(getMaxId);
           String requestID = idResult.get(0).get(0);
           String insert = "INSERT INTO MaintenanceRequest (RequestID, PlaneID, RepairCode, RequestDate, PilotID) VALUES ('" + requestID + "', '" + planeID + "', '" + repairCode + "', '" + requestDate + "', '" + pilotID + "')";
           esql.executeUpdate(insert);
           System.out.println("Maintenance request submitted.");
       } catch(Exception e) {
           System.err.println(e.getMessage());
       }
   }
}//end AirlineManagement


