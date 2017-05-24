import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddSpecRec extends JFrame{

	JPanel contentPane;
	String userid;
	JButton btnHome;
	JLabel lblUsername;
	JTextField specRecName;
	JButton btnAddSpecRec;
	
	public static void main(String args[]) {
		
		AddSpecRec obj=new AddSpecRec();
	}
	
	AddSpecRec() {
		
		setTitle("Add Specific Recommendation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 200);
		contentPane = new JPanel();
		setVisible(true);
		userid = "P3";
		btnHome = new JButton("Home Page");
	    btnHome.setBounds(150,450,100,20);
	    contentPane.add(btnHome);
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String enteredname = "";
		
		setContentPane(contentPane);
		try {
			
			conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
			stmt = conn.createStatement();
			
			query = "select username from patients where userid in (select patientid from supportedby where healthsupporterid = '"+userid+"')";
			System.out.println(query);
			rs = stmt.executeQuery(query);
			int y = 100;
			JLabel lblListSupp = new JLabel("List of Supportees");
			contentPane.add(lblListSupp);
			
			while (rs.next()){
				
				JLabel username = new JLabel(rs.getString("username"));
				username.setBounds(70,y,150,20);
				contentPane.add(username);
				y = y + 50;
			}
			
			lblUsername = new JLabel("Enter Name");
			contentPane.add(lblUsername);
			
			specRecName = new JTextField(20);
			contentPane.add(specRecName);
			
			
			btnAddSpecRec = new JButton("Add Specific Recommendation");
			contentPane.add(btnAddSpecRec);
			
			
			buttonActionClick();
			
			
			conn.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void buttonActionClick(){
		btnAddSpecRec.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				String query = "";
				String enteredname = "";
				String patientid = "";
				int countsup = 0;
				
				try {
					
					conn = DriverManager.getConnection(LoginApp.jdbcURL, LoginApp.user, LoginApp.passwd);
					stmt = conn.createStatement();
					
					enteredname = specRecName.getText().trim();
					
					if (enteredname.equals("")) {
						
						JOptionPane.showMessageDialog(null,"Invalid Input!","Enter Valid Name",JOptionPane.ERROR_MESSAGE);
						dispose();
						AddSpecRec specRecObj = new AddSpecRec();
					}
					else {
						
						query = "select userid from patients where username = '"+enteredname+"'";
						System.out.println(query);
						rs = stmt.executeQuery(query);
						
						while(rs.next()) {
							patientid = rs.getString("userid");
						}
						
						if (!patientid.equals("")) {
					
							query = "select count(*) as countsup from supportedby where patientid = '"+patientid+"' and healthsupporterid = '"+userid+"'";
							System.out.println(query);
							rs = stmt.executeQuery(query);
							while (rs.next()) {
								countsup = rs.getInt("countsup");
							}
							if (countsup == 0) {
								JOptionPane.showMessageDialog(null,"Not a Supportee!","Enter Valid Supportee",JOptionPane.ERROR_MESSAGE);
								dispose();
								HomePage homeObj = new HomePage();
							}
							else {
								
								JPanel myPanel = new JPanel();
								myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.PAGE_AXIS));
								
								JLabel lblrecid=new JLabel("RID");
								myPanel.add(lblrecid);
								JTextField recid = new JTextField(10);
								myPanel.add(recid);
								
								JLabel lblweight=new JLabel("Weight");
								myPanel.add(lblweight);
								JTextField weightLower = new JTextField(10);
								myPanel.add(weightLower);
								JTextField weightUpper = new JTextField(10);
								myPanel.add(weightUpper);
								JTextField weightFreq = new JTextField(10);
								myPanel.add(weightFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								JLabel lblbp=new JLabel("Blood Pressure");
								myPanel.add(lblbp);
								
								JTextField bpSysLo = new JTextField(10);
								myPanel.add(bpSysLo);
								JTextField bpSysUp = new JTextField(10);
								myPanel.add(bpSysUp);
								JTextField bpDiLo = new JTextField(10);
								myPanel.add(bpDiLo);
								JTextField bpDiUp = new JTextField(10);
								myPanel.add(bpDiUp);
								JTextField bpFreq = new JTextField(10);
								myPanel.add(bpFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								JLabel lbloxsat=new JLabel("Oxygen Saturation");
								myPanel.add(lbloxsat);
								JTextField oxsatLo = new JTextField(10);
								myPanel.add(oxsatLo);
								JTextField oxsatUp = new JTextField(10);
								myPanel.add(oxsatUp);
								JTextField oxsatFreq = new JTextField(10);
								myPanel.add(oxsatFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								JLabel lblpain=new JLabel("Pain");
								myPanel.add(lblpain);
								JTextField painValue = new JTextField(10);
								myPanel.add(painValue);
								JTextField painFreq = new JTextField(10);
								myPanel.add(painFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								JLabel lblmood=new JLabel("Mood");
								myPanel.add(lblmood);
								JTextField moodString = new JTextField(10);
								myPanel.add(moodString);
								JTextField moodFreq = new JTextField(10);
								myPanel.add(moodFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								JLabel lbltemp=new JLabel("Temperature");
								myPanel.add(lbltemp);
								JTextField tempLo = new JTextField(10);
								myPanel.add(tempLo);
								JTextField tempUp = new JTextField(10);
								myPanel.add(tempUp);
								JTextField tempFreq = new JTextField(10);
								myPanel.add(tempFreq);
								myPanel.add(Box.createHorizontalStrut(15));
								
								
								
								JOptionPane.showConfirmDialog(null, myPanel, 
							               "Please Enter the values", JOptionPane.OK_CANCEL_OPTION);
								
								String rec[] = new String[20];
								
								String strweightLow = weightLower.getText().trim();
//								rec[0] = strweightLow;
								String strweightUp = weightUpper.getText().trim();
//								rec[1] = strweightUp;
								String strweightFreq = weightFreq.getText().trim();
//								rec[2] = strweightFreq;
								
								String strbpSysLow = bpSysLo.getText().trim();
//								rec[3] = strbpSysLow;
								String strbpSysUp = bpSysUp.getText().trim();
//								rec[4] = strbpSysUp;
								String strbpDiLow = bpDiLo.getText().trim();
//								rec[5] = strbpDiLow;
								String strbpDiUp = bpDiUp.getText().trim();
//								rec[6] = strbpDiUp;
								String strbpFreq = bpFreq.getText().trim();
//								rec[7] = strbpFreq;
								
								String stroxsatLow = oxsatLo.getText().trim();
								String stroxsatUp = oxsatUp.getText().trim();
								String stroxsatFreq = oxsatFreq.getText().trim();
								
								String strpainValue = painValue.getText().trim();
								String strpainFreq = painFreq.getText().trim();
								
								String strdmoodString = moodString.getText().trim();
								String strdmoodFreq = moodFreq.getText().trim();
								
								String strTempLow = tempLo.getText().trim();
								String strTempUp = tempUp.getText().trim();
								String strTempFreq = tempFreq.getText().trim();
								
								String rid = recid.getText().trim();
								
								if (rid!=null) {
									
									query = "insert into recommendation values('"+rid+"','Specific','"+patientid+"')";
									System.out.println(query);
									stmt.executeUpdate(query);
									
									if (!(strweightLow.equals("") && strweightUp.equals("") && strweightFreq.equals(""))) {
										
										query = "insert into weight values('"+rid+"','"+strweightLow+"','"+strweightUp+"','"+strweightFreq+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									
									if (!(strbpSysLow.equals("") && strbpSysUp.equals("") && strbpDiLow.equals("") && strbpDiUp.equals("") && strbpFreq.equals(""))) {
										
										query = "insert into bp values('"+rid+"','"+strbpSysLow+"','"+strbpSysUp+"','"+strbpDiLow+"','"+strbpDiUp+"','"+strbpFreq+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									if (!(stroxsatLow.equals("") && stroxsatUp.equals("") && stroxsatFreq.equals(""))) {
										
										query = "insert into oxygensaturation values('"+rid+"','"+stroxsatLow+"','"+stroxsatUp+"','"+stroxsatFreq+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									if (!(strdmoodString.equals("") && strdmoodFreq.equals(""))) {
										
										query = "insert into mood values('"+rid+"','"+strdmoodString+"','"+strdmoodFreq+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									if (!(strTempLow.equals("") && strTempUp.equals("") && strTempFreq.equals(""))) {
										
										query = "insert into temperature values('"+rid+"','"+strTempLow+"','"+strTempUp+"','"+strTempFreq+"')";
										System.out.println(query);
										stmt.executeUpdate(query);
									}
									
								}
								
							}
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
		});
		
		btnHome.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				dispose();
				HomePage homePageFrame = new HomePage();
			}
		});
		
	}
}
