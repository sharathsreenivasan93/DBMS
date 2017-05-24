import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ViewAlerts extends JFrame {

	JPanel contentPane;
	JButton btnViewOwnAlerts;
	JButton btnViewSuppAlerts;
	JButton btnHome;
	String userid = LoginApp.userid;
	public static void main(String args[]) {
		
		ViewAlerts obj=new ViewAlerts();
	}
	
	ViewAlerts(){
		
		setTitle("View Alerts");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 200);
		setLayout(null);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		btnHome = new JButton("Home Page");
	    btnHome.setBounds(150,250,100,20);
	    contentPane.add(btnHome);
		setVisible(true);
		
		if (userid.equals("")) {
			JOptionPane.showMessageDialog(null,"Not Logged In!","Please Login",JOptionPane.ERROR_MESSAGE);
			dispose();
			LoginApp logObj = new LoginApp();
		}
		
		else {
			
			btnViewOwnAlerts = new JButton("View Own Alerts");
			contentPane.add(btnViewOwnAlerts);
			
			btnViewSuppAlerts = new JButton("View Supportees Alerts");
			contentPane.add(btnViewSuppAlerts);
			
			actionClickButtons();
			
		}
	}
	public void actionClickButtons() {
		
		btnViewOwnAlerts.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewOwnAlerts viewobj=new ViewOwnAlerts();
				
				dispose();
			}
		});
		
		btnViewSuppAlerts.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewSuppAlerts viewobj=new ViewSuppAlerts();
				
				dispose();
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
