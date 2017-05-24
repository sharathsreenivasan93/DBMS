import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ViewDiseaseSupp extends JFrame {

	JPanel contentPane;
	String userid;
	JButton btnHome;
	JButton btnDeldis;
	JTextField disdelname;
	JTextField disdeldis;
	
	public static void main(String args[]) {
		
		ViewDiseaseSupp viewObj=new ViewDiseaseSupp();
	}
	
	ViewDiseaseSupp() {
	
		setTitle("View Disease Supportee");
		contentPane = new JPanel();
		
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String query = "";
		
		setVisible(true);
		userid = LoginApp.userid;
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		btnHome = new JButton("Home Page");
		contentPane.add(btnHome);
		
		btnHome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				HomePage homePageFrame = new HomePage();
			}
		});
		
		try {
			
			conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
			stmt = conn.createStatement();
			int countnum = 0;
			 query = "select count(*) as countnum from supportedby where healthsupporterid ='"+userid+"'";
			 System.out.println(query);
			 
			 rs = stmt.executeQuery(query);
			 while(rs.next()) {
				 countnum = rs.getInt("countnum");
			 }
			 
			 System.out.println(countnum);
			
			 query = "select patientid from supportedby where healthsupporterid ='"+userid+"'";
			 System.out.println(query);
			 
			 rs = stmt.executeQuery(query);
			 
			 ArrayList<String> patientids = new ArrayList<String>();
			 
			 while (rs.next()) {
					patientids.add(rs.getString("patientid"));
			}
			 for(String patientid: patientids)
			  {
				 
//				String patientid = rs.getString("patientid");
				int countsick = 0;
				String supporteename = "";
				String supporteeadd = "";
				
				query = "select username from patients where userid = '"+patientid+"'";
				System.out.println(query);
				rs2 = stmt.executeQuery(query);
				
				while(rs2.next()) {
					supporteename = rs2.getString("username");
				}
				
				query = "select Address from patients where userid = '"+patientid+"'";
				System.out.println(query);
				rs2 = stmt.executeQuery(query);
				
				while(rs2.next()) {
					supporteeadd = rs2.getString("Address");
				}
				
				JLabel lblsupporteename= new JLabel(supporteename);
				contentPane.add(lblsupporteename);
				
				JLabel lblsupporteeadd= new JLabel(supporteeadd);
				contentPane.add(lblsupporteeadd);
				
				contentPane.add(Box.createHorizontalStrut(15));
				query = "select count (*) as countsick from sickpatients where userid='"+patientid+"'";
				System.out.println(query);
				rs2 = stmt.executeQuery(query);
				while(rs2.next()) {
					countsick = rs2.getInt("countsick");
				}
				if (countsick > 0) {
					
					query = "select DiseaseName from disease,sickpatients where disease.diseaseid =  sickpatients.diseaseid and sickpatients.userid = '"+patientid+"'";
					System.out.println(query);
					rs2 = stmt.executeQuery(query);
					while(rs2.next()) {
						
						JLabel lbldiseasename= new JLabel(rs2.getString("DiseaseName"));
						contentPane.add(lbldiseasename);
						contentPane.add(Box.createHorizontalStrut(15));
					}
					
				}
				else {
					
					JLabel lbldiseasename= new JLabel("Well");
					contentPane.add(lbldiseasename);
					contentPane.add(Box.createHorizontalStrut(15));
				}
			 }
			 conn.close();
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel lbldiseasedelname= new JLabel("Enter User Name to Delete");
		contentPane.add(lbldiseasedelname);
		
		disdelname = new JTextField(20);
		contentPane.add(disdelname);
		
		contentPane.add(Box.createHorizontalStrut(15));
		
		JLabel lbldiseasedeldis= new JLabel("Enter Disease to Delete");
		contentPane.add(lbldiseasedeldis);
		
		disdeldis = new JTextField(20);
		contentPane.add(disdeldis);
		contentPane.add(Box.createHorizontalStrut(15));
		
		btnDeldis = new JButton("Delete Disease");
		contentPane.add(btnDeldis);
		actionClickButton();
	}
	public void actionClickButton() {
		
		btnDeldis.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				String patientname = disdelname.getText().trim();
				String diseasename = disdeldis.getText().trim();
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query = "";
				String patientid = "";
				int diseaseid = 0;
				int countsupp = 0;
				int countsick = 0;
				int countsickdis = 0;
				String hsid = "";
				
				if (patientname.equals("") || diseasename.equals("")) {
					JOptionPane.showMessageDialog(null,"Invalid Disease Name or Patient Name!","Invalid Input",JOptionPane.ERROR_MESSAGE);
					dispose();
					ViewDiseaseSupp viewObj = new ViewDiseaseSupp();
				}
				else {
					
					try
					{
						conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
						stmt = conn.createStatement();
						
						query = "select userid from patients where username = '"+patientname+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							
							patientid = rs.getString("userid");
						}
						
						query = "select diseaseid from disease where diseasename = '"+diseasename+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							
							diseaseid = rs.getInt("diseaseid");
						}
						
						query = "select count(*) as count from supportedby where healthsupporterid='"+userid+"' and patientid = '"+patientid+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while (rs.next()) {
							
							countsupp = rs.getInt("count");
						}
						
						if (countsupp == 0) {
							
							JOptionPane.showMessageDialog(null,"Not a Supportee!","Not a Supportee",JOptionPane.ERROR_MESSAGE);
							dispose();
							HomePage homeObj = new HomePage();
							
						}
						else {
							
							query = "select count (*) as countsick from sickpatients where userid = '"+patientid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while(rs.next()) {
								countsick = rs.getInt("countsick");
							}
							query = "select count (*) as countsickdis from sickpatients where userid = '"+patientid+"' and diseaseid = '"+diseaseid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while(rs.next()) {
								countsickdis = rs.getInt("countsickdis");
							}
							
							if (countsick == 0 || countsickdis==0) {
								JOptionPane.showMessageDialog(null,"Cannot Delete Diagnosis!","Sorry. Cannot Delete",JOptionPane.ERROR_MESSAGE);
								dispose();
								ViewDiseaseSupp viewObj = new ViewDiseaseSupp();
							}
							else if (countsick == 1) {
								
								query = "select healthsupporterid from sickpatients where userid = '"+patientid+"'";
								System.out.println(query);
								rs = stmt.executeQuery(query);
								while(rs.next()) {
									hsid = rs.getString("healthsupporterid");
								}
								
								query = "insert into wellpatients values('"+patientid+"', '4','"+hsid+"')";
								System.out.println(query);
								stmt.executeUpdate(query);
								
								query = "delete from sickpatients where userid = '"+patientid+"' and diseaseid = '"+diseaseid+"'";
								System.out.println(query);
								stmt.executeUpdate(query);
						
							}
							else {
								query = "delete from sickpatients where userid = '"+patientid+"' and diseaseid = '"+diseaseid+"'";
								System.out.println(query);
								stmt.executeUpdate(query);
							}
							
						}
						
						
						conn.close();
						dispose();
						ViewDiseaseSupp viewObj = new ViewDiseaseSupp();
						
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
