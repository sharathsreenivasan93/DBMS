import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;

public class alertsFunctions {
	static final String jdbcURL 
	= "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
	public static final String user = "kaushi";
	public static final String passwd = "200111140";
	
	String obsTypeTemp = "";
	String obsType = "";
	String obsValue = "";
	
	public void outsideLimitAlerts(String userid, int r_id, String type) {
		
		String PATTERN="dd-MMM-yy";
		SimpleDateFormat dateFormat=new SimpleDateFormat();
		dateFormat.applyPattern(PATTERN);
		String date1=dateFormat.format(Calendar.getInstance().getTime());
		
		String rid = Integer.toString(r_id);
		
		try {
			
			String query = "";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			conn = DriverManager.getConnection(jdbcURL, user, passwd);
			stmt = conn.createStatement();
			System.out.println("Created connection");
			
			query = "delete from alerts where userid = '"+userid+"' and observationtype = '"+type+"'";
			System.out.println(query);
			stmt.executeUpdate(query);
			
			query = "select observationtype from (select * from observation where patientid='"+userid+"' order by recordingtime desc) where rownum = 1";
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				obsTypeTemp = rs.getString("observationtype");
			}
			obsType = obsTypeTemp.toLowerCase();
			
			query = "select value from (select * from observation where patientid = '"+userid+"' order by recordingtime desc) where rownum = 1";
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				obsValue = rs.getString("value");
			}
			
			switch(obsType) {
				case "weight":
					int wlower = 0, wupper = 0;
					int obsValueWeight = Integer.parseInt(obsValue);
					
					query = "select lower from weight where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						wlower = rs.getInt("lower");
					}
					
					query = "select upper from weight where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						wupper = rs.getInt("upper");
					}
					
					System.out.println("Performing alert");
					System.out.println("Observed weight value="+obsValueWeight);
					
					if(obsValueWeight < wlower) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Weight too low!','weight')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					else if(obsValueWeight > wupper && wupper != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Weight too high!','weight')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
				break;
				
				case "blood pressure":
					String parts[] = obsValue.split("/");
					String part1 = parts[0];
					String part2 = parts[1];
					int systolic = Integer.parseInt(part1);
					int diastolic = Integer.parseInt(part2);
					int slower = 0, supper = 0, dlower = 0, dupper = 0;
					
					query = "select lowersys from bp where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						slower = rs.getInt("lowersys");
					}
					
					query = "select highsys from bp where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						supper = rs.getInt("highsys");
					}
					
					query = "select lowerdia from bp where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						dlower = rs.getInt("lowerdia");
					}
					
					query = "select highdia from bp where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						dupper = rs.getInt("highdia");
					}
					
					if(systolic < slower) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Systolic too low!','blood pressure')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
					else if(systolic > supper && supper != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Systolic too high!','blood pressure')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
					if(diastolic < dlower) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Diastolic too low!','blood pressure')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
					else if(diastolic > dupper && dupper != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Diastolic too high!','blood pressure')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
				case "mood":
					if(obsValue == "sad") {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Why so serious!','mood')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
				break;
				
				case "temperature":
					int tempLower = 0, tempUpper = 0;
					int obsValueTemp = Integer.parseInt(obsValue);
					
					query = "select lower from temperature where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						tempLower = rs.getInt("lower");
					}
					
					query = "select upper from temperature where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						tempUpper = rs.getInt("upper");
					}
					
					if(obsValueTemp < tempLower) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Temperature too low!','temperature')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					else if(obsValueTemp > tempUpper && tempUpper != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Temperarure too high!','temperature')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
				break;
				
				case "pain":
					int painVal = 0;
					int obsValuePain = Integer.parseInt(obsValue);
					
					query = "select painval from pain where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						painVal = rs.getInt("painval");
					}
					
					if(obsValuePain > painVal && painVal != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','Pain too high!','pain')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
				break;	
				
				case "oxygen saturation":
					int osLower = 0, osUpper = 0;
					int obsValueOS = Integer.parseInt(obsValue);
					
					query = "select lower from oxygensaturation where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						osLower = rs.getInt("lower");
					}
					
					query = "select upper from oxygensaturation where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						osUpper = rs.getInt("upper");
					}
					
					if(obsValueOS < osLower) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','OS too low!','oxygen saturation')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					else if(obsValueOS > osUpper && osUpper != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Outside limit','OS too high!','oxygen saturation')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
				
				break;	
			}
			conn.close();	
		}
		
		catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void frequencyAlerts(String userid) {
		
		String PATTERN="dd-MMM-yy";
		SimpleDateFormat dateFormat=new SimpleDateFormat();
		dateFormat.applyPattern(PATTERN);
		String date1=dateFormat.format(Calendar.getInstance().getTime());
		
		try {
			String query = "";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			int rec_id = 0;
			
			conn = DriverManager.getConnection(jdbcURL, user, passwd);
			stmt = conn.createStatement();
			System.out.println("Created connection");
			
			query = "select count(*) as countrec from (select RecommendationID from recommendation where PatientID = '"+userid+"')";
			rs = stmt.executeQuery(query);
			System.out.println(query);
			
			while (rs.next()) {
				
				int countrec = rs.getInt("countrec");
				
				if (countrec > 0) {
					query = "select RecommendationID from recommendation where PatientID = '"+userid+"'";
					rs2 = stmt.executeQuery(query);
					System.out.println(query);
					while (rs2.next()) {
						rec_id = rs2.getInt("RecommendationID");
					}
				}
				else {
					int countsick = 0;
					int countwell = 0;
					
					query = "select count(*) as countsick from sickpatients where userid = '"+userid+"'";
					rs2 = stmt.executeQuery(query);
					System.out.println(query);
					
					while (rs2.next()) {
						countsick = rs2.getInt("countsick");
					}
					
					query = "select count(*) as countwell from wellpatients where userid = '"+userid+"'";
					rs2 = stmt.executeQuery(query);
					System.out.println(query);
					
					while (rs2.next()) {
						countwell = rs2.getInt("countwell");
					}
					
					if (countsick > 0) {
						
						query = "select recommendationid from disease,sickpatients where sickpatients.diseaseid = disease.diseaseid and sickpatients.userid = '"+userid+"'";
						rs2 = stmt.executeQuery(query);
						System.out.println(query);
						while (rs2.next()) {
							rec_id = rs2.getInt("recommendationid");
						}	
					}
					else if (countwell > 0) {
						
						query = "select recommendationid from disease where diseasename='Well'";
						rs2 = stmt.executeQuery(query);
						System.out.println(query);
						while (rs2.next()) {
							rec_id = rs2.getInt("recommendationid");
						}	
					}
				}	
			}
			
			ArrayList<String> obsTypes = new ArrayList<String>();
			obsTypes.add("weight");
			obsTypes.add("bp");
			obsTypes.add("mood");
			obsTypes.add("temperature");
			obsTypes.add("pain");
			obsTypes.add("oxygensaturation");
			
			ResultSet rs3 = null;
			Timestamp lastRecording = null;
			Date lastRec, currDate;
			String tempType = "";
			
			for(String s: obsTypes) {
				
				int firstCheck = 0,check = 0, frequency = 0, diffInDays = 0;
				if(s=="bp") {
					tempType = "blood pressure";
				}
				else if(s=="oxygensaturation") {
					tempType = "oxygen saturation";
				}
				else 
					tempType = s;
				
				query = "select count(*) as count from "+s+" where recommendationid="+rec_id;
				System.out.println(query);
				rs3 = stmt.executeQuery(query);
				while (rs3.next()) {
					check = rs3.getInt("count");
				}
				if(check > 0) {
					
					query = "select frequency from "+s+" where recommendationid="+rec_id;
					rs3 = stmt.executeQuery(query);
					System.out.println(query);
					while (rs3.next()) {
						frequency = rs3.getInt("frequency");
						System.out.println("Frequency is "+frequency);
					}
					
					query = "select count(*) as count from observation where observationtype = '"+tempType+"' and patientid = '"+userid+"'";
					System.out.println(query);
					rs3 = stmt.executeQuery(query);
					while (rs3.next()) {
						firstCheck = rs3.getInt("count");
					}
					
					if(firstCheck == 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Initial alert','"+tempType+" has never been recorded!','"+tempType+"')";
						System.out.println(query);
						stmt.executeQuery(query);
					}
					
					else {
					query = "select recordingtime from(select * from observation  where observationtype = '"+tempType+"' and patientid = '"+userid+"' order by recordingtime desc) where rownum =1";
					System.out.println(query);
					rs3 = stmt.executeQuery(query);
					while (rs3.next()) {
						System.out.println("entering the result set");
						lastRecording = rs3.getTimestamp("recordingtime");
						System.out.println(lastRecording);
						lastRec = new Date(lastRecording.getTime());
						currDate = new Date(System.currentTimeMillis());
						diffInDays = (int)( (currDate.getTime() - lastRec.getTime()) / (1000 * 60 * 60 * 24) );
						
						System.out.println("last rec "+lastRec);
						System.out.println("curr date "+currDate);
						System.out.println("diff in days "+diffInDays);
						
					}
					
					if(diffInDays > frequency && frequency != 0) {
						query = "insert into alerts values(AUTOINCREMENT.nextval,'"+userid+"','"+date1+"','Low frequency alert','"+tempType+" not being checked regularly','"+tempType+"')";
						System.out.println(query);
						stmt.executeQuery(query);
					
					}
					
				}
				}
				
			}	
		}
		
		catch(SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
//	
}