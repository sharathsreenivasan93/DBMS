import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jdesktop.swingx.prompt.PromptSupport;


public class DelPatient extends JFrame {

	JPanel contentPane;
	public static void main(String args[]) {
		
		DelPatient obj=new DelPatient();
	}
	
	DelPatient(){
		
		setTitle("Delete Patient Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 200);
		contentPane = new JPanel();
		setVisible(true);
		
	}
}
