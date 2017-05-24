import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FirstWindow extends JFrame {
	
	JPanel contentPane;
	JButton login_button;
	JButton signup_button;
	
	public static void main(String args[]) {
		
		FirstWindow frameTable=new FirstWindow();
	}

	FirstWindow() {
		
		super("Welcome");
		setSize(400,200);
		setLocation(500,280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);	
//		panel.add(welcome);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		login_button = new JButton("Login");
		signup_button = new JButton("Sign Up");
		
		contentPane.add(login_button);
		contentPane.add(signup_button);
		
		actionClick();
	}
	
	public void actionClick() {
		
		login_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
					
					LoginApp loginFrame=new LoginApp();
					
					loginFrame.setVisible(true);
					dispose();
			}
		});
		
		signup_button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
					
					SignUp signUpFrame=new SignUp();
					
					signUpFrame.setVisible(true);
					dispose();
			}
		});
	}
}
