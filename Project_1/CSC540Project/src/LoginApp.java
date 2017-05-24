import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginApp extends JFrame {
	
	public static String userid;
	JPanel contentPane; //declare variable
	JTextField txtUser;
	JButton btnLogin;
	JTextField txtPassword;
	protected java.lang.String Spassword;
	
	static final String jdbcURL 
	= "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
	
//	static final String DB_URL = "jdbc:mysql://localhost/demo";
	
	public static final String user = "kaushi";
	public static final String passwd = "200111140";
	
	
	protected static final String String = null;
	
	
	public static void main(String args[]) {
		LoginApp frameTable = new LoginApp();
	}
	
	LoginApp() {
		
		setTitle("User Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
//		panel.setLayout (null); 
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(188, 51, 99, 20);
		
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		JLabel lblUID = new JLabel("User ID");
		lblUID.setBounds(70, 54, 86, 14);
		contentPane.add(lblUID);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(70, 109, 86, 14);
		
		contentPane.add(lblPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.setBounds(131, 165, 89, 23);
		contentPane.add(btnLogin);
		txtPassword = new JTextField();
		txtPassword.setBounds(188, 106, 99, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);	
		
		setVisible(true);
		actionClickLogin();
	}
	
	public void actionClickLogin() {
		
		btnLogin.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
					
					String query = "";
					userid = "";
					String password = "";
					Connection conn = null;
					Statement stmt = null;
					userid = txtUser.getText().trim();
					password = txtPassword.getText().trim();
					ResultSet rs = null;
					int count = 0;
					int countdef = 0;
					JPanel myPanel = new JPanel();
					JTextField txtPrimHealth = new JTextField(10);
					JTextField txtPrimAuth = new JTextField(10);
					String hsid="";
					String hsauth="";
					
					if (userid.equals("")|| password.equals(""))
					{
						JOptionPane.showMessageDialog(null," name or password or Role is wrong","Error",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						try {
						
							conn = DriverManager.getConnection(jdbcURL, user, passwd);
							stmt = conn.createStatement();
							query = "select count(*) as count from patients where USERID ='"+userid+"'and PASSWORD = '"+password+"'";
							System.out.println(query);
							
							rs = stmt.executeQuery(query);
							
							while(rs.next()){
								
								count = rs.getInt("count");
							}
							if (count == 1){
									
								query = "select count(*) as countdef from sickpatients where userid = '"+userid+"' and healthsupporterid = 'DEF'";
								System.out.println(query);
								
								rs = stmt.executeQuery(query);
								
								while(rs.next()) {
									countdef = rs.getInt("countdef");
								}
									if (countdef > 0) {
									
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
								
								alertsFunctions alert = new alertsFunctions();
								alert.frequencyAlerts(userid);
								dispose();
								HomePage homePageFrame=new HomePage();
								
							}
							else {
									JOptionPane.showMessageDialog(null,"Invalid User ID or Password!","Invalid Login",JOptionPane.ERROR_MESSAGE);
									LoginApp logobj = new LoginApp();
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
		
	}
	
}