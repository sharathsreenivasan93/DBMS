import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ViewOwnObs extends JFrame {
	
	JPanel contentPane;
	String userid;
	JButton btnViewObs;
	
	public static void main(String args[]) {
		
		ViewOwnObs viewObj=new ViewOwnObs();
	}
	ViewOwnObs() {
		
		setTitle("View Own Observation Data");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		contentPane = new JPanel();
		setContentPane(contentPane);
		setLayout(null);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query="";
		userid = LoginApp.userid;
		String useridpatient = "";
		
		btnViewObs = new JButton("View Observation");
	    btnViewObs.setBounds(150,450,100,20);
	    contentPane.add(btnViewObs);
		
		if (userid.equals("")) {
			JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
			dispose();
			LoginApp logObj = new LoginApp();
		}
		else {
		
			try {
				conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
				stmt = conn.createStatement();
				query = "select userid from sickpatients where userid = '"+userid+"' UNION select userid from wellpatients where userid = '"+userid+"'";;
				System.out.println(query);
				
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					useridpatient = rs.getString("userid");
				}
				if (useridpatient.equals("")) {
					JOptionPane.showMessageDialog(null,"Not a patient!","Not logged in as a patient",JOptionPane.ERROR_MESSAGE);
					dispose();
					LoginApp loginobj = new LoginApp();
				}
				else {
					System.out.println(useridpatient);
					query = "select * from observation where patientid='"+useridpatient+"'";
					System.out.println(query);
					rs = stmt.executeQuery(query);
					int x = 100;
					while (rs.next()) {
						
						JLabel patientid = new JLabel(rs.getString("patientid"));
						patientid.setBounds(70,x,150,20);
						contentPane.add(patientid);
						JLabel observationtime = new JLabel(rs.getTimestamp("observationtime").toString());
						observationtime.setBounds(120,x,200,20);
						contentPane.add(observationtime);
						JLabel recordingtime = new JLabel(rs.getTimestamp("recordingtime").toString());
						recordingtime.setBounds(320,x,200,20);
						contentPane.add(recordingtime);
						JLabel value = new JLabel(rs.getString("value"));
						value.setBounds(520,x,50,20);
						contentPane.add(value);
						JLabel observationtype = new JLabel(rs.getString("observationtype"));
						observationtype.setBounds(570,x,50,20);
						contentPane.add(observationtype);
						
						System.out.println(rs.getString("patientid"));
						System.out.println(rs.getTimestamp("observationtime"));
						System.out.println(rs.getTimestamp("recordingtime"));
						System.out.println(rs.getString("value"));
						System.out.println(rs.getString("observationtype"));
						x = x + 50;
					}
				}
				conn.close();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		btnViewObs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				ViewObservations viewObj = new ViewObservations();
			}
		});
	}
}
