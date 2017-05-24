import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

public class SignUpOld extends JFrame {
	
	JPanel contentPane; //declare variable
	JTextField txtUserName;
	JButton btnSignup;
	JButton btnSubmit = new JButton("Ok");
	JTextField txtPassword;
	JTextField txtUserID;
	JTextField txtDate;
	JTextField txtGender;
	JTextField txtAddress;
	protected java.lang.String Spassword;
	DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
	JComboBox<String> comboBox = new JComboBox<String>(model);
	
	int health_supporter_flag = 0;
	
	static final String jdbcURL 
	= "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
	
	
	static final String user = "kaushi";
	static final String passwd = "200111140";
	
	
	protected static final String String = null;
	
	
	public static void main(String args[]) {
		SignUpOld frameTable = new SignUpOld();
	}
	
	SignUpOld() {
		
		setTitle("New User Sign Up");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		JLabel lblUserID = new JLabel("User ID");
		
		lblUserID.setBounds(70, 50, 85, 15);
		contentPane.add(lblUserID);
		
		txtUserID = new JTextField();
		txtUserID.setBounds(120, 50, 100, 20);
		contentPane.add(txtUserID);
		
		JLabel lblUserName = new JLabel("Name");
		
		lblUserName.setBounds(70, 100, 85, 15);
		contentPane.add(lblUserName);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(120, 100, 100, 20);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(70, 150, 85, 15);
		
		contentPane.add(lblPassword);
		txtPassword = new JPasswordField();
		txtPassword.setBounds(150, 150, 100, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(70, 200, 85, 15);
		
		contentPane.add(lblDate);
		txtDate = new JTextField();
		txtDate.setBounds(120, 200, 100, 20);
		contentPane.add(txtDate);
		txtDate.setColumns(10);
		
		JLabel lblGender = new JLabel("Gender");
		lblGender.setBounds(70, 250, 85, 15);
		
		contentPane.add(lblGender);
		txtGender = new JTextField();
		txtGender.setBounds(120, 250, 100, 20);
		contentPane.add(txtGender);
		txtGender.setColumns(10);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setBounds(70, 300, 85, 15);
		
		contentPane.add(lblAddress);
		txtAddress = new JTextField();
		txtAddress.setBounds(130, 300, 100, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		model.addElement("Patient");
		model.addElement("Health Supporter");
	    comboBox.setBounds(70,350,85,15);
		contentPane.add(comboBox);
		
		btnSignup = new JButton("SignUp");
		btnSignup.setBounds(70, 400, 100, 20);
		contentPane.add(btnSignup);
		
        
		setVisible(true);
		actionClickSignUp();
	}
	
	public void actionClickSignUp() {
		
		btnSignup.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
					
					String query = "";
					String userid = "";
					String username = "";
					String password = "";
					String dob = "";
					String gender = "";
					String address = "";
					Connection conn = null;
					Statement stmt = null;
					userid = txtUserID.getText().trim();
					username = txtUserName.getText().trim();
					password = txtPassword.getText().trim();
					dob = txtDate.getText().trim();
					gender = txtGender.getText().trim();
					address = txtAddress.getText().trim();
					
//					HomePage homePageFrame=new HomePage();
					
					String selected = comboBox.getSelectedItem().toString();
					
					System.out.println(selected);
//					System.out.println("You selected " + comboBox.getSelectedItem());
					
					try {
						
						if (userid.equals("")|| password.equals("") || username.equals(""))
						{
							JOptionPane.showMessageDialog(null," Name or password or ID is Invalid","Error",JOptionPane.ERROR_MESSAGE);
//							SignUp signUpFrame=new SignUp();
//							
//							signUpFrame.setVisible(true);
//							dispose();
						}
						else {
							
							conn = DriverManager.getConnection(jdbcURL, user, passwd);
							stmt = conn.createStatement();
							System.out.println("Created connection");
							query = "INSERT INTO patients VALUES ('"+userid+"','"+username+"','"+password+"','"+dob+"','"+gender+"','"+address+"')";
						
							System.out.println(query);
							
							stmt.executeUpdate(query);
//							System.out.println("Finished Query");
							
							if (selected == "Patient") {
								
								JTextField txtDiagnosis = new JTextField(10);
								JTextField txtPrimHealth = new JTextField(10);
								JTextField txtPrimAuth = new JTextField(10);
								
								JTextField txtSecHealth = new JTextField(10);
								JTextField txtSecAuth = new JTextField(10);
								
								JPanel myPanel = new JPanel();
								
								myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
								
								myPanel.add(new JLabel("Enter Diagnosis - "));
								myPanel.add(txtDiagnosis);
								myPanel.add(Box.createHorizontalStrut(15));
								myPanel.add(new JLabel("Enter Primary Health Supporter ID - "));
								myPanel.add(txtPrimHealth);
								myPanel.add(Box.createHorizontalStrut(15));
								myPanel.add(new JLabel("Enter Primary Health Supporter authorization date - "));
								myPanel.add(txtPrimAuth);
								myPanel.add(Box.createHorizontalStrut(15));
								
								myPanel.add(new JLabel("Enter Secondary Health Supporter - "));
								myPanel.add(txtSecHealth);
								myPanel.add(Box.createHorizontalStrut(15));
								myPanel.add(new JLabel("Enter Secondary Health Supporter authorization date - "));
								myPanel.add(txtSecAuth);
								myPanel.add(Box.createHorizontalStrut(15));
								
								myPanel.add(btnSubmit);
								
//								JOptionPane.showMessageDialog(null,myPanel, 
//							               "Please Enter the values",JOptionPane.INFORMATION_MESSAGE);
								 
								JOptionPane.showOptionDialog(null, myPanel,"Empty?", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);

								
								ResultSet rs = null;
								String diagnosis = txtDiagnosis.getText();
								String primehealthid = txtPrimHealth.getText();
								String primehealthauth = txtPrimAuth.getText();
								String sechealthid = txtSecHealth.getText();
								String sechealthauth = txtSecAuth.getText();
								int diseaseid = 0;
//								int supporter_flag = 0;
								
								if (!diagnosis.equals("")) {
									
									if (primehealthid.equals("") || primehealthauth.equals("")){
										
										
										JOptionPane.showMessageDialog(null,"Please enter a valid Primary Health Supporter ID or Authorization Date","Invalid Login",JOptionPane.ERROR_MESSAGE);
//										dispose();
										health_supporter_flag = 1;
//										SignUp signUpFrame=new SignUp();
//										
//										signUpFrame.setVisible(true);
//										dispose();
										
									}
									else {
										
										query = "SELECT DISEASEID as diseaseid FROM DISEASE WHERE DISEASENAME = '"+diagnosis+"'";
										System.out.println(query);
										
										rs = stmt.executeQuery(query);
										
										while(rs.next()){
											
											diseaseid = rs.getInt("diseaseid");
											System.out.println(diseaseid);
										}
										
										query = "insert into sickpatients values('"+userid+"',"+diseaseid+",'"+primehealthid+"')";
										stmt.executeUpdate(query);
										
										String supportertype = "Primary";
										
										query = "insert into supportedby values('"+primehealthid+"','"+userid+"','"+supportertype+"','"+primehealthauth+"')"; 
										stmt.executeUpdate(query);
										
										System.out.println(query);
										
										if (sechealthauth.equals("") && sechealthid.equals("")) {
											
										}
										else {
											
											supportertype = "Secondary";
											query = "insert into supportedby values('"+sechealthid+"','"+userid+"','"+supportertype+"','"+sechealthauth+"')"; 
											System.out.println(query);
											stmt.executeUpdate(query);
										}
									}
										if (primehealthid.equals("") || primehealthauth.equals("")) {
//											supporter_flag = 1;
											JOptionPane.showMessageDialog(null,"Please enter a valid Primary Health Supporter ID and Authorization Date","Invalid Login",JOptionPane.ERROR_MESSAGE);
//											SignUp signUpFrame=new SignUp();
//											signUpFrame.setVisible(true);
//											dispose();
										//	homePageFrame.setVisible(true);
									}
								}
								else {
									
									query = "insert into wellpatients values('"+userid+"','','"+primehealthid+"')";
									System.out.println(query);
									stmt.executeUpdate(query);
									
									String supportertype = "Primary";
									
									if (primehealthid.equals("") || primehealthauth.equals("")) {
										health_supporter_flag = 1;
									}
									else {
										
										query = "insert into supportedby values('"+primehealthid+"','"+userid+"','"+supportertype+"','"+primehealthauth+"')"; 
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									
									if (sechealthid.equals("") || sechealthauth.equals("")) {
										
									}
									else {
										
										supportertype = "Secondary";
										query = "insert into supportedby values('"+sechealthid+"','"+userid+"','"+supportertype+"','"+sechealthauth+"')"; 
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									
//								    homePageFrame.setVisible(true);
//									dispose();
									System.out.println("Well");
									
								}
								
							}
								
						}
//						homePageFrame.setVisible(true);
//						dispose();
						actionClickOK();
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}
		});
	}
	public void actionClickOK() {
		
		btnSubmit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				HomePage homePageFrame=new HomePage();
				SignUpOld signUpFrame=new SignUpOld();
				
				if(health_supporter_flag == 0) {
					homePageFrame.setVisible(true);
					dispose();
				}
				else {
					health_supporter_flag = 0;
					signUpFrame.setVisible(true);
					dispose();
				}
			}
		});
	}
	
}