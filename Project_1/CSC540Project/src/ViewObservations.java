import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ViewObservations extends JFrame {

	JPanel contentPane;
	JButton btnViewOwn;
	JButton btnViewSupp;
	JButton btnHome;
	public static void main(String args[]) {
		
		ViewObservations obj=new ViewObservations();
	}
	
	ViewObservations(){
		
		setTitle("View Observation Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 200);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnViewOwn = new JButton("View Own");
		btnViewOwn.setBounds(150, 150, 100, 20);
		contentPane.add(btnViewOwn);
		
		btnViewSupp = new JButton("View Supportee");
		btnViewSupp.setBounds(150, 200, 100, 20);
		contentPane.add(btnViewSupp);
		
		btnHome = new JButton("Home Page");
	    btnHome.setBounds(150,250,100,20);
	    contentPane.add(btnHome);
		setVisible(true);
		
		actionClickButtons();
	}
	
	public void actionClickButtons() {
		
		btnViewOwn.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewOwnReco viewobj=new ViewOwnReco();
				
				dispose();
			}
		});
		
		btnViewSupp.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewSuppReco viewobj=new ViewSuppReco();
				
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
