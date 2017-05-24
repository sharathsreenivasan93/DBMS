import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HomePage extends JFrame {

	JPanel contentPane;
	JButton btnEnterObs;
	JButton btnViewObs;
	JButton btnViewAlerts;
	JButton btnPatInf;
	JButton btnAddSpecRec;
	JButton btnViewRec;
	JButton btnHome;
	JButton btnViewDisSupp;
	
	public static void main(String args[]) {
		
		HomePage obj=new HomePage();
	}
	
	HomePage() {
		
		setTitle("Home Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		setBounds(100, 100, 650, 200);
		contentPane = new JPanel();
//		contentPane.setLayout(null);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		
		btnEnterObs = new JButton("Enter Observation Data");
		btnEnterObs.setBounds(150, 150, 89, 23);
		contentPane.add(btnEnterObs);
		
		btnViewObs = new JButton("View Observation Data");
		btnViewObs.setBounds(150, 200, 89, 23);
		contentPane.add(btnViewObs);
		
		btnViewAlerts = new JButton("View Alerts");
		btnViewAlerts.setBounds(150, 250, 89, 23);
		contentPane.add(btnViewAlerts);
		
		btnPatInf = new JButton("Update Patient Information");
		btnPatInf.setBounds(150, 300, 89, 23);
		contentPane.add(btnPatInf);
		
		btnAddSpecRec = new JButton("Add Specific Recommendation");
		btnAddSpecRec.setBounds(150, 350, 89, 23);
		contentPane.add(btnAddSpecRec);
		
		btnViewRec = new JButton("View Recommendation");
		btnViewRec.setBounds(150, 400, 89, 23);
		contentPane.add(btnViewRec);
		
		btnViewDisSupp = new JButton("View Disease of Supportee");
		btnViewDisSupp.setBounds(150, 450, 89, 23);
		contentPane.add(btnViewDisSupp);
		
		setVisible(true);
		actionClickButtons();
	}
	
	public void actionClickButtons(){
		
		btnEnterObs.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				EnterObservation enterObsFrame=new EnterObservation();
				
//				enterObsFrame.setVisible(true);
				dispose();
			}
		});
		
		btnViewObs.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewObservations viewObsFrame=new ViewObservations();
				
//				viewObsFrame.setVisible(true);
				dispose();
			}
		});
		
		btnViewAlerts.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewAlerts viewAlertsFrame=new ViewAlerts();
				
//				viewAlertsFrame.setVisible(true);
				dispose();
			}
		});
		
		btnPatInf.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				UpdatePatient viewPatientFrame=new UpdatePatient();
				
//				viewPatientFrame.setVisible(true);
				dispose();
			}
		});
		
		btnAddSpecRec.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				AddSpecRec addSpecObj=new AddSpecRec();
				
//				viewPatientFrame.setVisible(true);
				dispose();
			}
		});
		
		btnViewRec.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewRecommendation addSpecObj=new ViewRecommendation();
				
//				viewPatientFrame.setVisible(true);
				dispose();
			}
		});
		
		btnViewDisSupp.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae) {
				
				ViewDiseaseSupp addSpecObj=new ViewDiseaseSupp();
				
//				viewPatientFrame.setVisible(true);
				dispose();
			}
		});
		
	}
}
