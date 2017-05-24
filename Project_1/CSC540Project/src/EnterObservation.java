import javax.swing.*;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class EnterObservation extends JFrame {

	JPanel contentPane;
	String query = "";
//	JTextField obsTime;
	JFormattedTextField obsTime;
//	JTextField recTime;
	JFormattedTextField recTime;
	JTextField weightValue;
	JTextField bpValue;
	JTextField tempValue;
	JTextField moodValue;
	JTextField painValue;
	JTextField oxsatValue;
	JButton btnEnter;
	JButton btnHome;
	JLabel lblobsTime = new JLabel("Observation Time");
	JLabel lblrecTime = new JLabel("Recording Time");
	JLabel lblWeight = new JLabel("Weight");
	JLabel lblBp = new JLabel("Blood Pressure");
	JLabel lblTemp = new JLabel("Temperature");
	JLabel lblMood = new JLabel("Mood");
	JLabel lblPain = new JLabel("Pain");
	JLabel lblOxsat = new JLabel("Oxy Sat");
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	String userid = LoginApp.userid;
//	String userid = "P1";
	int rec_id = 0;
	int countweight = 0;
	int countbp = 0;
	int counttemp = 0;
	int countmood = 0;
	int countpain = 0;
	int countoxsat = 0;
	int validcount = 0;
	
	String validinputs[] = {"","","","","",""};
	
	public static void main(String args[]) {
		
		EnterObservation obj=new EnterObservation();
	}
	
	EnterObservation(){
		
		setTitle("Enter Observation Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setVisible(true);
		contentPane.setLayout(null);
		
		lblobsTime.setBounds(70, 50, 200, 15);
		contentPane.add(lblobsTime);
		 	 
		obsTime = new JFormattedTextField(createFormatter("##-UUU-## ##.##.##.## UU"));
		obsTime.setColumns(20);
		obsTime.setBounds(175, 50, 200, 20);
		contentPane.add(obsTime);
		 
		lblrecTime.setBounds(70, 100, 200, 15);
		contentPane.add(lblrecTime);
		 
		recTime = new JFormattedTextField(createFormatter("##-UUU-## ##.##.##.## UU"));
		recTime.setColumns(20);
		recTime.setBounds(175, 100, 200, 20);
		contentPane.add(recTime);
		 
		 
	     btnEnter = new JButton("Enter");
	     btnEnter.setBounds(70, 350, 100, 20);
	     contentPane.add(btnEnter);
	     
	     btnHome = new JButton("Home Page");
	     btnHome.setBounds(200,350,100,20);
	     contentPane.add(btnHome);
	     
	     try {
				conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
				stmt = conn.createStatement();
				
				query = "select count(*) as countrec from (select RecommendationID from recommendation where PatientID = '"+userid+"')";
				System.out.println(query);
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					
					int countrec = rs.getInt("countrec");
					
					if (countrec > 0) {
						query = "select RecommendationID from recommendation where PatientID = '"+userid+"'";
						System.out.println(query);
						rs2 = stmt.executeQuery(query);	
						while (rs2.next()) {
							rec_id = rs2.getInt("RecommendationID");
						}
					}
					else {
						int countsick = 0;
						int countwell = 0;
						
						query = "select count(*) as countsick from sickpatients where userid = '"+userid+"'";
						System.out.println(query);
						rs2 = stmt.executeQuery(query);
						
						while (rs2.next()) {
							countsick = rs2.getInt("countsick");
						}
						
						query = "select count(*) as countwell from wellpatients where userid = '"+userid+"'";
						System.out.println(query);
						rs2 = stmt.executeQuery(query);
						
						while (rs2.next()) {
							countwell = rs2.getInt("countwell");
						}
						
						if (countsick > 0) {
							
							query = "select recommendationid from disease,sickpatients where sickpatients.diseaseid = disease.diseaseid and sickpatients.userid = '"+userid+"'";
							System.out.println(query);
							rs2 = stmt.executeQuery(query);
							
							while (rs2.next()) {
								rec_id = rs2.getInt("recommendationid");
							}	
						}
						else if (countwell > 0) {
							
							query = "select recommendationid from disease where diseasename='Well'";
							System.out.println(query);
							rs2 = stmt.executeQuery(query);					
							while (rs2.next()) {
								rec_id = rs2.getInt("recommendationid");
							}	
						}
						else {
							JOptionPane.showMessageDialog(null,"Not a patient!","Not logged in as a patient",JOptionPane.ERROR_MESSAGE);
							dispose();
							LoginApp homePageFrame = new LoginApp();
						}
					}
					System.out.println(rec_id);
					
					query = "select count(*) as countweight from weight where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						countweight = rs2.getInt("countweight");
					}
					if (countweight > 0) {
						lblWeight.setBounds(70, 150, 200, 15);
						contentPane.add(lblWeight);
						 
						weightValue = new JTextField();
						weightValue.setBounds(175, 150, 100, 20);
						contentPane.add(weightValue);
						weightValue.setColumns(10);
						validinputs[validcount] = "weight";
						validcount++;
					}
					
					query = "select count(*) as countbp from bp where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						countbp = rs2.getInt("countbp");
					}
					if (countbp > 0) {
						lblBp.setBounds(70, 200, 200, 15);
						contentPane.add(lblBp);
						 
						bpValue = new JTextField();
						bpValue.setBounds(175, 200, 100, 20);
						contentPane.add(bpValue);
						bpValue.setColumns(10);
						validinputs[validcount] = "bp";
						validcount++;
					}
					
					query = "select count(*) as countmood from mood where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						countmood = rs2.getInt("countmood");
					}
					if (countmood > 0) {
						lblMood.setBounds(70, 250, 200, 15);
						contentPane.add(lblMood);
						 
						moodValue = new JTextField();
						moodValue.setBounds(175, 250, 100, 20);
						contentPane.add(moodValue);
						moodValue.setColumns(10);
						validinputs[validcount] = "mood";
						validcount++;
					}
					
					query = "select count(*) as countpain from pain where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						countpain = rs2.getInt("countpain");
					}
					if (countpain > 0) {
						lblPain.setBounds(70, 250, 200, 15);
						contentPane.add(lblPain);
						 
						painValue = new JTextField();
						painValue.setBounds(175, 250, 100, 20);
						contentPane.add(painValue);
						painValue.setColumns(10);
						validinputs[validcount] = "pain";
						validcount++;
					}
					
					query = "select count(*) as countoxsat from oxygensaturation where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						countoxsat = rs2.getInt("countoxsat");
					}
					if (countoxsat > 0) {
						lblOxsat.setBounds(70, 300, 200, 15);
						contentPane.add(lblOxsat);
						 
						oxsatValue = new JTextField();
						oxsatValue.setBounds(175, 300, 100, 20);
						contentPane.add(oxsatValue);
						oxsatValue.setColumns(10);
						validinputs[validcount] = "oxygensaturation";
						validcount++;
					}
					
					query = "select count(*) as counttemp from temperature where recommendationID = '"+rec_id+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						counttemp = rs2.getInt("counttemp");
					}
					if (counttemp > 0) {
						lblTemp.setBounds(70, 350, 200, 15);
						contentPane.add(lblTemp);
						 
						tempValue = new JTextField();
						tempValue.setBounds(175, 350, 100, 20);
						contentPane.add(tempValue);
						tempValue.setColumns(10);
						validinputs[validcount] = "temperature";
						validcount++;
					}
					
				}
		
				conn.close();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
		
		checkforpatient();
	}
	
	private MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
	
	public void checkforpatient()
	{	
		
		String userid = LoginApp.userid;
		
//		final Connection conn = null;
//		final Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		btnEnter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				try {
					Connection conn = null;
					Statement stmt = null;
					String obstime = obsTime.getText();
					String rectime = recTime.getText();
					String userid = LoginApp.userid;
//					String userid = "P1";
					conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
					stmt = conn.createStatement();
					
					if (obstime.equals("")) {
						JOptionPane.showMessageDialog(null,"Invalid Obs Time!","Invalid Obs Time",JOptionPane.ERROR_MESSAGE);
						dispose();
						EnterObservation enterObs = new EnterObservation();
					}
					else {
						
						for (int i = 0;i<validinputs.length;i++) {
							
							String type = "";
							
							if(validinputs[i].equals("weight")) {
								
								String value = weightValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','weight')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "weight";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
								}
							}
							else if(validinputs[i].equals("bp")) {
								
								String value = bpValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','blood pressure')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "blood pressure";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
									}
							}
							else if(validinputs[i].equals("temperature")) {
								
								String value = tempValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','temperature')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "temperature";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
								}	
							}
							else if(validinputs[i].equals("mood")) {
								
								String value = moodValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','mood')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "mood";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
								}	
							}
							else if(validinputs[i].equals("pain")) {
								
								String value = painValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','pain')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "maid";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
								}	
							}
							else if(validinputs[i].equals("oxygensaturation")) {
								
								String value = oxsatValue.getText();
								if(!value.equals("")) {
									query = "insert into observation values('"+userid+"','"+obstime+"','"+rectime+"','"+value+"','oxygen saturation')";
									System.out.println(query);
									stmt.executeQuery(query);
									type = "oxygen saturation";
									alertsFunctions alert = new alertsFunctions();
									alert.outsideLimitAlerts(userid, rec_id, type);
								}	
							}
						}
						
					}
					conn.close();
					dispose();
					EnterObservation entObs = new EnterObservation();	
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btnHome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				HomePage homePageFrame = new HomePage();
			}
		});
		
	}
}
