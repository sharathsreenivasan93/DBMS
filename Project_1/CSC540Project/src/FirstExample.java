
import java.sql.*;

public class FirstExample {

    static final String jdbcURL 
	= "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";

    public static void main(String[] args) {
        
    	try {


//        	Class.forName("oracle.jdbc.driver.OracleDriver");
	    	String user = "kaushi";	// For example, "jsmith"
	    	String passwd = "200111140";	// Your 9 digit student ID number
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {

					conn = DriverManager.getConnection(jdbcURL, user, passwd);
					stmt = conn.createStatement();

					// Creates and inserts go here
//					stmt.executeUpdate("CREATE TABLE SickPatients " +
//			   				"(PatientID INTEGER PRIMARY KEY, DiseaseID INTEGER, " +
//			   				"PatientName VARCHAR(30), PatientAddress VARCHAR(50), PatientPhone VARCHAR(20), "+
//			   				"PatientAge INTEGER, PatientEmail VARCHAR(30), DateOfVisit DATE, Gender VARCHAR(2),"+
//			   				"HealthSupporterID INTEGER NOT NULL, RecommendationID INTEGER NOT NULL,"+
//			   				"HealthObservationID INTEGER NOT NULL, DateOfDischarge DATE)");
////					
//					stmt.executeUpdate("CREATE TABLE WellPatients " +
//			   				"(PatientID INTEGER PRIMARY KEY, DiseaseID INTEGER, " +
//			   				"PatientName VARCHAR(30), PatientAddress VARCHAR(50), PatientPhone VARCHAR(20), "+
//			   				"PatientAge INTEGER, PatientEmail VARCHAR(30), DateOfVisit DATE, Gender VARCHAR(2),"+
//			   				"HealthSupporterID INTEGER NOT NULL, RecommendationID INTEGER NOT NULL,"+
//			   				"HealthObservationID INTEGER NOT NULL)");
				
					System.out.println("Created Table");
					
//					stmt.executeUpdate("INSERT INTO Patients " +
//			   				"VALUES (1,5,'FNUKaushik','University Commons','789-567-1234',25,'kaushi@ncsu.edu','29-SEP-06','M')");
//					
//					System.out.println("Inserted Table");
					// Populate the COFFEES table

					// Get data from the COFFEES table

					rs = stmt.executeQuery("SELECT * FROM Patients");

					while (rs.next()) {
					    int patient_id = rs.getInt("PatientID");
					    int disease_id = rs.getInt("DiseaseID");
					    String patient_name = rs.getString("PatientName");
					    String patient_address = rs.getString("PatientAddress");
					    String patient_phone = rs.getString("PatientPhone");
					    int patient_age = rs.getInt("PatientAge");
					    System.out.println(patient_id + "   " + disease_id+ "	"+ patient_name+"	"+patient_address+"		"+patient_phone+"	"+patient_age);
					}

           	} 
           	finally {
                close(rs);
                close(stmt);
                close(conn);
            }
        } 
        catch(Throwable oops) {
            oops.printStackTrace();
        }
    }

    static void close(Connection conn) {
        if(conn != null) {
            try { 
            	conn.close(); 
            } 
            catch(Throwable whatever) {

            }
        }
    }

    static void close(Statement st) {
        if(st != null) {
            try { 
            	st.close(); 
            } catch(Throwable whatever) {

            }
        }
    }

    static void close(ResultSet rs) {
        if(rs != null) {
            try { 
            	rs.close();
            } catch(Throwable whatever) {

            }
        }
    }
}
