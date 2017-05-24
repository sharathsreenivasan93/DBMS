import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewOwnAlerts extends JFrame {
	
	JPanel contentPane;
	String userid;
	JButton btnViewAlerts;
	public static void main(String args[]) {
		
		ViewOwnAlerts obj=new ViewOwnAlerts();
	}
	
	ViewOwnAlerts() {
		
		setTitle("View Own Alerts");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		setLayout(null);
		contentPane = new JPanel();
		setContentPane(contentPane);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query="";
		userid = LoginApp.userid;
		
		btnViewAlerts = new JButton("View Alerts");
	    btnViewAlerts.setBounds(150,450,100,20);
	    contentPane.add(btnViewAlerts);
		
		if (userid.equals("")) {
			JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
			dispose();
			LoginApp logObj = new LoginApp();
		}
		else {
		
			try {
				
				conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
				stmt = conn.createStatement();
				
				query = "select username,dateofalert,alertmessage from patients, alerts where alerts.userid = patients.userid and alerts.userid = '"+userid+"'";
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
				conn.close();
				
				
			}
			
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		btnViewAlerts.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				ViewAlerts viewAlertsObj = new ViewAlerts();
			}
		});
	}

}
