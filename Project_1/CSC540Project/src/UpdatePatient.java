import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jdesktop.swingx.prompt.PromptSupport;

public class UpdatePatient extends JFrame {

	JPanel contentPane;
	Connection conn = null;
	Statement stmt = null;
	String query = "";
	ResultSet rs = null;
	JTextField username;
    JTextField password;
    JTextField address;
    JTextField diseaseadd;
    JButton btnDiseaseAdd;
    
    JTextField diseasedel;
    JButton btnDiseaseDel;
    
    JTextField hsaddPrim;
    JTextField hsauthPrim; 
    JButton btnHsAddPrim;
    
    JButton btnDelPrim;
    JButton btnDelSec;
    JButton btnUpdate;
    JButton btnDelete;
    
    JTextField hsaddSec;
    JTextField hsauthSec; 
    JButton btnHsAddSec;
    
    JButton btnHome;
    String userid;
//    JButton btnDiagnosis;
	
	public static void main(String args[]) {
		
		UpdatePatient obj=new UpdatePatient();
	}
	
	UpdatePatient(){
		
		setTitle("Update Patient Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		setVisible(true);
		userid = LoginApp.userid;
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		btnHome = new JButton("Home Page");
	    btnHome.setBounds(150,450,100,20);
	    contentPane.add(btnHome);
		
		setContentPane(contentPane);
//		contentPane.setLayout(null);
		
		if (userid.equals("")){
			JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
			dispose();
			LoginApp logObj = new LoginApp();
		}
		else {
			
			try {
				conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
				stmt = conn.createStatement();
				
				query = "select username,password,address from patients where userid = '"+userid+"'";
				System.out.println(query);
				rs = stmt.executeQuery(query);
				while(rs.next()) {
					
					username = new JTextField(50);
					username.setText(rs.getString("username"));
	                password = new JTextField(50);
	                password.setText(rs.getString("password"));
	                address = new JTextField(50);
	                address.setText(rs.getString("address"));
	
	//                PromptSupport.setPrompt(rs.getString("username"), username);
	//                PromptSupport.setPrompt(rs.getString("password"), password);
	//                PromptSupport.setPrompt(rs.getString("address"), address);
					
	                contentPane.add(username);
	                contentPane.add(Box.createHorizontalStrut(15));
	                contentPane.add(password);
	                contentPane.add(Box.createHorizontalStrut(15));
	                contentPane.add(address);
	                contentPane.add(Box.createHorizontalStrut(15));
	                
					System.out.println(rs.getString("username"));
					System.out.println(rs.getString("password"));
					System.out.println(rs.getString("address"));
				}
				
	//			btnDiagnosis = new JButton("Diagnosis");
	//			contentPane.add(btnDiagnosis);
				JLabel healthSupp = new JLabel("Health Supporters");
				contentPane.add(healthSupp);
				query = "select username,address from patients where userid in(select healthsupporterid from supportedby where patientid = '"+userid+"')";
				System.out.println(query);
				rs = stmt.executeQuery(query);
				int y = 100;
				while (rs.next()) {
					
					JLabel username = new JLabel(rs.getString("username"));
					username.setBounds(70,y,150,20);
					contentPane.add(username);
					contentPane.add(Box.createHorizontalStrut(15));
					
					JLabel address = new JLabel(rs.getString("address"));
					address.setBounds(70,y,150,20);
					contentPane.add(address);
					contentPane.add(Box.createHorizontalStrut(15));
					
				}
				
				
				query = "select diseasename from disease d, sickpatients s where d.diseaseid = s.diseaseid and s.userid = '"+userid+"'";
				
				rs = stmt.executeQuery(query);
				
				while(rs.next()) {
					
					JTextField disease = new JTextField(rs.getString("diseasename"));
	//				PromptSupport.setPrompt(rs.getString("diseasename"), disease);
					contentPane.add(disease);
					contentPane.add(Box.createHorizontalStrut(15));
					System.out.println(rs.getString("diseasename"));
					
				}
				
				diseaseadd = new JTextField(20);
				contentPane.add(diseaseadd);
				btnDiseaseAdd = new JButton("Add Disease");
				contentPane.add(btnDiseaseAdd);
				contentPane.add(Box.createHorizontalStrut(15));
				
				diseasedel = new JTextField(20);
				contentPane.add(diseasedel);
				btnDiseaseDel = new JButton("Delete Disease");
				contentPane.add(btnDiseaseDel);
				contentPane.add(Box.createHorizontalStrut(15));
				
				hsaddPrim = new JTextField(20);
				contentPane.add(hsaddPrim);
				hsauthPrim = new JTextField(20);
				contentPane.add(hsauthPrim);
				
				
				btnHsAddPrim = new JButton("Add Primary Health Supporter");
				contentPane.add(btnHsAddPrim);
				contentPane.add(Box.createHorizontalStrut(15));
				
				hsaddSec = new JTextField(20);
				contentPane.add(hsaddSec);
				hsauthSec = new JTextField(20);
				contentPane.add(hsauthSec);
				
				btnHsAddSec = new JButton("Add Secondary Health Supporter");
				contentPane.add(btnHsAddSec);
				contentPane.add(Box.createHorizontalStrut(15));
				
				btnDelPrim = new JButton("Delete Primary Supporter");
				contentPane.add(btnDelPrim);
				contentPane.add(Box.createHorizontalStrut(15));
				
				btnDelSec = new JButton("Delete Secondary Supporter");
				contentPane.add(btnDelSec);
				contentPane.add(Box.createHorizontalStrut(15));
				
				btnUpdate = new JButton("Update");
				contentPane.add(btnUpdate);
				
				btnDelete = new JButton("Delete Patient");
				contentPane.add(btnDelete);
				contentPane.add(Box.createHorizontalStrut(15));
				
				conn.close();
				actionClickButton();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	public void actionClickButton() {
		
		btnDiseaseAdd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query="";
				String diseasename = diseaseadd.getText().trim();
				String diseaseid = "";
				String hsid = "";
				String primAuthVal = "";
				
				int counths = 0;
				int countsick = 0;
				int countwell = 0;
				JPanel myPanel = new JPanel();
				JTextField txtPrimHealth = new JTextField(10);
				JTextField txtPrimAuth = new JTextField(10);
				
				if (diseasename.equals("")) {
					
					JOptionPane.showMessageDialog(null,"Invalid Disease Name!","Enter Disease",JOptionPane.ERROR_MESSAGE);
					dispose();
					UpdatePatient updatePatient = new UpdatePatient();
				}
				else
				{
					try {
						
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						
						query = "select diseaseid from disease where diseasename = '"+diseasename+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while(rs.next()) {
							
							diseaseid = rs.getString("diseaseid");
						}
						
						//Check if sick patient
						query = "select count(*) as countsick from sickpatients where userid = '"+userid+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							countsick = rs.getInt("countsick");
						}
						
						//Check if well patient
						if (countsick == 0) {
							query = "select count(*) as countwell from wellpatients where userid = '"+userid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							
							while(rs.next()) {
								countwell = rs.getInt("countwell");
							}
							if (countwell == 0) {
								JOptionPane.showMessageDialog(null,"Not a Patient!","Invalid Patient",JOptionPane.ERROR_MESSAGE);
								dispose();
								UpdatePatient updatePatient = new UpdatePatient();
							}
							else {
								query =  "select count(*) as count from(select distinct (healthsupporterid) from wellpatients where userid = '"+userid+"' and healthsupporterid is not NULL)";
								rs = stmt.executeQuery(query);
								System.out.println(query);
								while (rs.next()) {
									counths = rs.getInt("count");
								}
								//Ask for primary health supporter
								if (counths == 0) {
									
									
									myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
									myPanel.add(new JLabel("Enter Primary Health Supporter ID - "));
									myPanel.add(txtPrimHealth);
									myPanel.add(Box.createHorizontalStrut(15));
									
									myPanel.add(new JLabel("Enter Primary Health Supporter Auth Date - "));
									myPanel.add(txtPrimAuth);
									myPanel.add(Box.createHorizontalStrut(15));
									
									JOptionPane.showConfirmDialog(null, myPanel, 
								               "Please Enter the values", JOptionPane.OK_CANCEL_OPTION);
									
									hsid = txtPrimHealth.getText().trim();
									
									primAuthVal = txtPrimAuth.getText().trim();
									
									query = "insert into supportedby values('"+hsid+"','"+userid+"', 'Primary', '"+primAuthVal+"')";
									System.out.println(query);
									stmt.executeUpdate(query);
									
								}
								//Removing from Well Patients
								if (hsid.equals("")) {
									
									query = "select distinct (healthsupporterid) from wellpatients where userid = '"+userid+"' and healthsupporterid is not NULL";
									System.out.println(query);
									rs = stmt.executeQuery(query);
									while (rs.next()) {
										hsid = rs.getString("healthsupporterid");
									}
								}
								query = "delete from wellpatients where userid = '"+userid+"'";
								System.out.println(query);
								stmt.executeUpdate(query);
								
							}
						}
						//Sick Patient
						else {
							
							query = "select distinct (healthsupporterid) from sickpatients where userid = '"+userid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while(rs.next()) {
								hsid = rs.getString("healthsupporterid");
							}
						}
						
						query = "insert into sickpatients values ('"+userid+"','"+diseaseid+"','"+hsid+"')";
						System.out.println(query);
						
						stmt.executeUpdate(query);
						
						System.out.println("Done with sick");
						
						conn.close();
						dispose();
						UpdatePatient updateObj=new UpdatePatient();
						
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			}
		});
		
		btnDiseaseDel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
			
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query="";
				String diseasename = diseasedel.getText().trim();
				String diseaseid = "";
				int countsick = 0;
				String hsid = "";
				
				if (diseasename.equals("")) {
					
					JOptionPane.showMessageDialog(null,"Invalid Disease Name!","Enter Disease",JOptionPane.ERROR_MESSAGE);
					dispose();
					UpdatePatient updatePatient = new UpdatePatient();
				}
				else {
					
						try {
							conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
							stmt = conn.createStatement();
							
							query = "select diseaseid from disease where diseasename = '"+diseasename+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							
							while (rs.next()){
								diseaseid = rs.getString("diseaseid");
							}
							if (diseaseid.equals("")){
								JOptionPane.showMessageDialog(null,"Invalid Disease Name!","Enter Disease",JOptionPane.ERROR_MESSAGE);
								dispose();
								UpdatePatient updatePatient = new UpdatePatient();
							}
							else {
								
								query = "select count (*) as countsick from sickpatients where userid = '"+userid+"'";
								System.out.println(query);
								rs = stmt.executeQuery(query);
								while(rs.next()){
									countsick = rs.getInt("countsick");
								}
								if (countsick == 0) {
									JOptionPane.showMessageDialog(null,"Cannot Delete Diagnosis. Probably a well patient!","Cannot Delete",JOptionPane.ERROR_MESSAGE);
									dispose();
									UpdatePatient updatePatient = new UpdatePatient();
								}
								else if (countsick == 1) {
									query = "select distinct healthsupporterid from sickpatients where userid = '"+userid+"'";
									System.out.println(query);
									rs = stmt.executeQuery(query);
									while(rs.next()) {
										hsid = rs.getString("healthsupporterid");
									}
									if (!hsid.equals("")) {
										query = "insert into wellpatients values('"+userid+"',4,'"+hsid+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
										
										System.out.println("Done with inserting into well");
										
										query = "delete from sickpatients where userid = '"+userid+"' and diseaseid = '"+diseaseid+"'";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
								}
								else {
									 query = "delete from sickpatients where userid = '"+userid+"' and diseaseid = '"+diseaseid+"'";
									 System.out.println(query);
									 stmt.executeUpdate(query);
								}
								
							}
							conn.close();
							dispose();
							UpdatePatient updateObj = new UpdatePatient();
							
						}
						catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
				}
			}
		});
		
		
		btnHsAddPrim.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
			
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query="";
				int countprim = 0;
				int countwell = 0;
				String hsid = hsaddPrim.getText().trim();
				String auth = hsauthPrim.getText().trim();
				
				if (hsid.equals("")) {
					
					JOptionPane.showMessageDialog(null,"Invalid Health Supporter!","Enter Valid HS",JOptionPane.ERROR_MESSAGE);
					dispose();
					UpdatePatient updatePatient = new UpdatePatient();
				}
				else {
					try {
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						
						query = "select count(*) as countprim from supportedby where patientid = '"+userid+"' and supportertype = 'Primary'";
						
						
						System.out.println(query);
						rs = stmt.executeQuery(query);
						while(rs.next()) {
							
							countprim = rs.getInt("countprim");
						}
						
						if (countprim > 0) {
							JOptionPane.showMessageDialog(null,"Prime Health Supporter Exists!","Cannot Add Primary HS",JOptionPane.ERROR_MESSAGE);
							dispose();
							UpdatePatient updatePatient = new UpdatePatient();
						}
						else {
							
							query = "select count(*) as countwell from wellpatients where userid = '"+userid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while (rs.next()) {
								countwell = rs.getInt("countwell");
							}
							
							if (countwell == 0){
								JOptionPane.showMessageDialog(null,"Not a Patient!","Not HS",JOptionPane.ERROR_MESSAGE);
								dispose();
								UpdatePatient updatePatient = new UpdatePatient();
							}
							else {
								
								query = "update wellpatients set healthsupporterid = '"+hsid+"' where userid = '"+userid+"'";
								System.out.println(query);
								stmt.executeUpdate(query);
								
								
								query = " insert into supportedby values ('"+hsid+"', '"+userid+"', 'Primary', '"+auth+"')";
								System.out.println(query);
								stmt.executeUpdate(query);
								
							}
							
							
						}
						
					conn.close();	
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		btnHsAddSec.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				int countsec = 0;
				String hsid = hsaddSec.getText().trim();
				String auth = hsauthSec.getText().trim();
				
				if (hsid.equals("")) {
					
					JOptionPane.showMessageDialog(null,"Invalid Health Supporter!","Enter Valid HS",JOptionPane.ERROR_MESSAGE);
					dispose();
					UpdatePatient updatePatient = new UpdatePatient();
				}
				else {
					try {
						
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						
						query = "select count(*) as countsec from supportedby where patientid = '"+userid+"' and supportertype = 'Secondary'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						while (rs.next()) {
							countsec = rs.getInt("countsec");
						}
						if (countsec > 0) {
							JOptionPane.showMessageDialog(null,"Secondary Health Supporter Exists!","Cannot Add Secondary HS",JOptionPane.ERROR_MESSAGE);
							dispose();
							UpdatePatient updatePatient = new UpdatePatient();
						}
						else {
							query = "insert into supportedby values ('"+hsid+"', '"+userid+"', 'Secondary', '"+auth+"')";
							System.out.println(query);
							stmt.executeUpdate(query);
							
						}
						conn.close();
					
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		
		
		btnDelSec.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				
				try {
					conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
					stmt = conn.createStatement();
					
					query = "delete from supportedby where patientid = '"+userid+"' and supportertype = 'Secondary'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					conn.close();
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btnDelPrim.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				int countsick = 0;
				int countwell = 0;
				JPanel myPanel = new JPanel();
				JTextField txtPrimHealth = new JTextField(10);
				JTextField txtPrimAuth = new JTextField(10);
				String hsid="";
				String hsauth="";
				
				try {
					conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
					stmt = conn.createStatement();
					
					query = "select count(*) as countsick from sickpatients where userid = '"+userid+"'";
					System.out.println(query);
					rs = stmt.executeQuery(query);
					
					while (rs.next()) {
						countsick = rs.getInt("countsick");
					}
					//Checking sick patient
					if (countsick == 0) {
						
						query = "select count(*) as countwell from wellpatients where userid = '"+userid+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while(rs.next()) {
							countwell = rs.getInt("countwell");
						}
						if (countwell == 0) {
							JOptionPane.showMessageDialog(null,"Not a Patient!","Invalid Patient",JOptionPane.ERROR_MESSAGE);
							dispose();
							UpdatePatient updatePatient = new UpdatePatient();
						}
						else {
							
							query = "update wellpatients set healthsupporterid = NULL where userid = '"+userid+"'";
							System.out.println(query);
							stmt.executeUpdate(query);
							
							query = "delete from supportedby where patientid = '"+userid+"' and supportertype = 'Primary'";
							System.out.println(query);
							stmt.executeUpdate(query);
						}
						
					}
					else {
						
						myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
						
						myPanel.add(new JLabel("You have been identified as a sick patient so you need a Primary Health Supporter"));
						myPanel.add(Box.createHorizontalStrut(15));
						
						myPanel.add(new JLabel("Enter Primary Health Supporter ID - "));
						myPanel.add(txtPrimHealth);
						myPanel.add(Box.createHorizontalStrut(15));
						
						myPanel.add(new JLabel("Enter Primary Health Supporter Auth Date - "));
						myPanel.add(txtPrimAuth);
						myPanel.add(Box.createHorizontalStrut(15));
						
						JOptionPane.showConfirmDialog(null, myPanel, 
					               "Please Enter the values", JOptionPane.OK_CANCEL_OPTION);
						
						hsid = txtPrimHealth.getText().trim();
						hsauth = txtPrimAuth.getText().trim();
						
						query = "update sickpatients set healthsupporterid = '"+hsid+"' where userid = '"+userid+"'";
						System.out.println(query);
						stmt.executeUpdate(query);
						
						query = "update supportedby set healthsupporterid = '"+hsid+"', authorizationdate = '"+hsauth+"' where patientid = '"+userid+"' and supportertype = 'Primary'";
						System.out.println(query);
						stmt.executeUpdate(query);
					}
					conn.close();
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String namevalue = username.getText().trim();
				String passwordvalue = password.getText().trim();
				String addressvalue = address.getText().trim();
				
				if (namevalue.equals("") || passwordvalue.equals("") || addressvalue.equals("")) {
					JOptionPane.showMessageDialog(null,"Invalid Entries!","Enter Valid Values",JOptionPane.ERROR_MESSAGE);
					dispose();
					UpdatePatient updatePatient = new UpdatePatient();
				}
				else {
					try {
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						
						query = "update patients set username = '"+namevalue+"', password = '"+passwordvalue+"', address = '"+addressvalue+"' where userid = '"+userid+"'";
						System.out.println(query);
						stmt.executeUpdate(query);
						conn.close();
						
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				int countrec = 0;
				int recid = 0;
				String query;
				
				try {
					
					conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
					stmt = conn.createStatement();
					
					//Remove from recommendations
					query = "select count(*) as countrec from recommendation where patientid = '"+userid+"'";
					System.out.println(query);
					rs = stmt.executeQuery(query);
					while (rs.next()) {
						countrec = rs.getInt("countrec");
					}
					if (countrec != 0){
						
						query = "select recommendationid from recommendation where patientid = '"+userid+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							recid = rs.getInt("recommendationid");
						}
						
						query = "delete from weight where recommendationid = '"+recid+"'";
						System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from BP where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from oxygenSaturation where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from pain where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from mood where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from temperature where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
						
				        query = "delete from recommendation where recommendationid = '"+recid+"'";
				        System.out.println(query);
						stmt.executeUpdate(query);
					}
					
					
					query = "delete from observation where patientid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					//All instances of cuurent user being Secondary HS can be removed
					query = "delete from supportedby where healthsupporterid = '"+userid+"' and supportertype ='Secondary'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					//Instances where user is Primary HS to sick patients have to be set to default
					query = "update supportedby set healthsupporterid = 'DEF' where patientid IN(select userid from sickpatients) and healthsupporterid = '"+userid+"' and supportertype='Primary'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "update sickpatients set healthsupporterid = 'DEF' where healthsupporterid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					//Instances where user is Primary HS to wellpatients can be removed
					query = "delete from supportedby where patientid IN(select userid from wellpatients) and supportertype='Primary' and healthsupporterid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "update wellpatients set healthsupporterid = NULL where healthsupporterid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "delete from alerts where userid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
							
					
					//Delete Patient instances
					query = "delete from supportedby where patientid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "delete from sickpatients where userid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "delete from wellpatients where userid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					query = "delete from patients where userid = '"+userid+"'";
					System.out.println(query);
					stmt.executeUpdate(query);
					
					conn.close();
					dispose();
					LoginApp loginObj = new LoginApp();
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
