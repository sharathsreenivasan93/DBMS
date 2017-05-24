import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewSuppAlerts extends JFrame {

	JPanel contentPane;
	String userid;
	JButton btnDelAlerts;
	JTextField delAlerts;
	JButton btnViewAlerts;
	
	public static void main(String args[]) {
		
		ViewSuppAlerts obj=new ViewSuppAlerts();
	}
	
	ViewSuppAlerts() {
		
		setTitle("View Supportee Alerts");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		setLayout(null);
		contentPane = new JPanel();
		setContentPane(contentPane);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		userid = LoginApp.userid;
		
		btnViewAlerts = new JButton("View Alerts");
	    btnViewAlerts.setBounds(150,450,100,20);
	    contentPane.add(btnViewAlerts);
		
		String query="";
	
		
		if (userid.equals("")) {
			JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
			dispose();
			LoginApp logObj = new LoginApp();
		}
		else {

			try {
				conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
				stmt = conn.createStatement();
//				String userid = "P3";
				
				
				String PATTERN="dd-MMM-yy";
				SimpleDateFormat dateFormat=new SimpleDateFormat();
				dateFormat.applyPattern(PATTERN);
				String date1=dateFormat.format(Calendar.getInstance().getTime());
				
				query = "select username,dateofalert,alertmessage from alerts, patients where alerts.userid = patients.userid and patients.userid in (select patientid from supportedby where healthsupporterid = '"+userid+"' and '"+date1+"' > authorizationdate)";
				System.out.println(query);
				rs = stmt.executeQuery(query);
				
				int y = 100;
				while (rs.next()) {
					
					JLabel patientname = new JLabel(rs.getString("username"));
					patientname.setBounds(70,y,50,20);
					contentPane.add(patientname);
					
					JLabel lblalert = new JLabel(rs.getString("alertmessage"));
					lblalert.setBounds(120,y,100,20);
					contentPane.add(lblalert);
					
					JLabel lblalertdate = new JLabel(rs.getString("dateofalert"));
					lblalertdate.setBounds(170,y,100,20);
					contentPane.add(lblalertdate);
					
					contentPane.add(Box.createHorizontalStrut(15));
					
					y = y+50; 
				}
				
				JLabel lblAlerts = new JLabel("Enter user name to be deleted");
				contentPane.add(lblAlerts);
				delAlerts = new JTextField(20);
				
				contentPane.add(delAlerts);
				
				btnDelAlerts = new JButton("Delete Alert");
				
				contentPane.add(btnDelAlerts);
				
				actionClickButton();
				
				
				conn.close();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void actionClickButton() {
		
		btnDelAlerts.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query;
				int countsup = 0;
//				String userid = "P3";
				String username = delAlerts.getText().trim();
				String deluserid = "";
				
				if (username.equals("")) {
					
					JOptionPane.showMessageDialog(null,"Invalid Username!","Please Re Enter",JOptionPane.ERROR_MESSAGE);
					dispose();
					ViewSuppAlerts viewSuppAlertsObj = new ViewSuppAlerts();
					
				}
				else if (userid.equals("")) {
					JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
					dispose();
					LoginApp logObj = new LoginApp();
				}
				else {
				
					try {
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						query = "select count(*) as countsup from supportedby where patientid IN (select userid from patients where username = '"+username+"') and healthsupporterid = '"+userid+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							countsup = rs.getInt("countsup");
						}
						if (countsup == 0) {
							JOptionPane.showMessageDialog(null,"Not a Supportee!","Not a Supportee",JOptionPane.ERROR_MESSAGE);
							dispose();
							HomePage homePageObj = new HomePage();
						}
						else {
							
							query = "select userid from patients where username = '"+username+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while(rs.next()) {
								deluserid = rs.getString("userid");
							}
							if (!deluserid.equals("")) {
								query = "delete from alerts where userid ='"+deluserid+"'";
								System.out.println(query);
								stmt.executeUpdate(query);
							}
						}
						dispose();
						HomePage hpObj = new HomePage();
						
						conn.close();
					}
					catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		btnViewAlerts.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				ViewAlerts viewAlertsObj = new ViewAlerts();
			}
		});
	}
}
