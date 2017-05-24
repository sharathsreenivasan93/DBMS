import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class ViewSuppReco extends JFrame {

	JPanel contentPane;
	JButton btnViewRec;
	String userid;
	public static void main(String args[]) {
		
		ViewSuppReco obj=new ViewSuppReco();
	}
	
	ViewSuppReco () {
		
		setTitle("View Supportee Recommendation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 200);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		userid = LoginApp.userid;
		
		setVisible(true);
		
		btnViewRec = new JButton("View Recommendation");
		contentPane.add(btnViewRec);
		
		btnViewRec.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				ViewRecommendation viewRec = new ViewRecommendation();
			}
		});
		viewPatientRecommendations(userid,contentPane);
		
	}
	public void viewPatientRecommendations(String userid, JPanel contentPane) {
		
		try {
			String query = "";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			
			ArrayList<String> patientids = new ArrayList<String>();
			
			conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
			stmt = conn.createStatement();
			System.out.println("Created connection");
			
			query = "select patientid from supportedby where healthsupporterid = '"+userid+"'";
			System.out.println(query);
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				patientids.add(rs.getString("patientid"));
			}
			
			for(String s: patientids) {
				viewOwnRecommendations(s,contentPane);
			}
		}
		
		catch(SQLException e) {
//			TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	public void viewOwnRecommendations(String userid, JPanel contentPane) {
		
		try {
			
			String query = "";
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			ResultSet rs2 = null;
			int rec_id = 0;
			
			conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
			stmt = conn.createStatement();
			System.out.println("Created connection");
			
			query = "select count(*) as countrec from (select RecommendationID from recommendation where PatientID = '"+userid+"')";
			rs = stmt.executeQuery(query);
			System.out.println(query);
			
			JLabel patientid = new JLabel(userid);
			contentPane.add(patientid);
			
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
 			
			ArrayList<String> recTypes = new ArrayList<String>();
			recTypes.add("weight");
			recTypes.add("bp");
			recTypes.add("mood");
			recTypes.add("temperature");
			recTypes.add("pain");
			recTypes.add("oxygensaturation");
			
			String rid = Integer.toString(rec_id);
			int ifExists = 0;
			
			for(String s: recTypes) {
				query = "select count(*) as count from "+s+" where recommendationid = "+rid;
				System.out.println(query);
				rs = stmt.executeQuery(query);
				ifExists = 0;
				while (rs.next()) {
					ifExists = rs.getInt("count");
				}
				
				if(ifExists != 0) {
					query = "select * from "+s+" where recommendationid = "+rid;
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						
						if (s.equals("weight")) {
							
							JLabel weightLabel = new JLabel("Weight (Lower, Upper, Frequency)");
							contentPane.add(weightLabel);
							
							JLabel weightlow= new JLabel(Integer.toString(rs.getInt("lower")));
							contentPane.add(weightlow);
							
							JLabel weightupper= new JLabel(Integer.toString(rs.getInt("upper")));
							contentPane.add(weightupper);
							
							JLabel weightfreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(weightfreq);
							contentPane.add(Box.createHorizontalStrut(15));
						}
						else if(s.equals("bp")) {
							JLabel bpLabel = new JLabel("Blood Pressure (Systolic Lower, Systolic Upper, Diastolic Lower, Diastolic Upper, Frequency)");
							contentPane.add(bpLabel);
							
							JLabel bplowsys= new JLabel(Integer.toString(rs.getInt("lowersys")));
							contentPane.add(bplowsys);
							
							JLabel bphighsys= new JLabel(Integer.toString(rs.getInt("highsys")));
							contentPane.add(bphighsys);
							
							JLabel bplowdia= new JLabel(Integer.toString(rs.getInt("lowerdia")));
							contentPane.add(bplowdia);
							
							JLabel bphighdia= new JLabel(Integer.toString(rs.getInt("highdia")));
							contentPane.add(bphighdia);
							
							JLabel bpfreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(bpfreq);
							contentPane.add(Box.createHorizontalStrut(15));
							
						}
						else if(s.equals("oxygensaturation")) {
							
							JLabel oxsatLabel = new JLabel("Oxygen Saturation (Lower, Upper, Frequency)");
							contentPane.add(oxsatLabel);
							
							JLabel oxysatlow= new JLabel(Integer.toString(rs.getInt("lower")));
							contentPane.add(oxysatlow);
							
							JLabel oxysatup= new JLabel(Integer.toString(rs.getInt("upper")));
							contentPane.add(oxysatup);
							
							JLabel oxysatfreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(oxysatfreq);
							
							contentPane.add(Box.createHorizontalStrut(15));
							
						}
						
						else if(s.equals("pain")) {
							JLabel painLabel = new JLabel("Pain (Value, Frequency)");
							contentPane.add(painLabel);
							
							JLabel painValue= new JLabel(Integer.toString(rs.getInt("painval")));
							contentPane.add(painValue);
							
							JLabel painFreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(painFreq);
							contentPane.add(Box.createHorizontalStrut(15));
							
						}
						
						else if (s.equals("mood")) {
							JLabel moodLabel = new JLabel("Mood (Mood, Frequency)");
							contentPane.add(moodLabel);
							
							JLabel moodString= new JLabel(rs.getString("mood"));
							contentPane.add(moodString);
							
							JLabel moodFreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(moodFreq);
							contentPane.add(Box.createHorizontalStrut(15));
						}
						
						else if (s.equals("temperature")) {
							JLabel tempLabel = new JLabel("Temperature (Lower, Upper, Frequency)");
							contentPane.add(tempLabel);
							
							JLabel templow= new JLabel(Integer.toString(rs.getInt("lower")));
							contentPane.add(templow);
							
							JLabel tempup= new JLabel(Integer.toString(rs.getInt("upper")));
							contentPane.add(tempup);
							
							JLabel tempfreq= new JLabel(Integer.toString(rs.getInt("frequency")));
							contentPane.add(tempfreq);
							contentPane.add(Box.createHorizontalStrut(15));
						}
					}
					
				}
			}
			conn.close();
		}
		
		catch(SQLException e) {
//			TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
