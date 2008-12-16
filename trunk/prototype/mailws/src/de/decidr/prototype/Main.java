/**
 * 
 */
package de.decidr.prototype;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JOptionPane;
import javax.xml.ws.Endpoint;

/**
 * @author RR
 * 
 */
public class Main {

	public static void main(String[] args) throws AddressException,
			MessagingException, IOException {
		JavaMailService jms = new JavaMailService();

		Endpoint.publish("http://127.0.0.1:8080/mailws", jms);
		JOptionPane.showMessageDialog(null, "srv bndn");
		System.exit(0);

		// JavaMailBackend demo = new JavaMailBackend(
		// "rumberrd@studi.informatik.uni-stuttgart.de",
		// "rumbergerr@yahoo.de", "testDemoMail");
		// JPasswordField passField = new JPasswordField();
		// String pwd;
		// if (JOptionPane.showConfirmDialog(null, passField, "password input",
		// JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
		// pwd = new String(passField.getPassword());
		// else {
		// System.exit(1);
		// return;
		// }
		//	
		// demo.setBodyText("testestest");
		// demo.setHostname("smtp.mail.yahoo.de");
		// demo.setCC("rumbergerr@gmail.com");
		// demo.addCC("rrumberger@gmx.de, rrumberger@web.de");
		// demo.setPortNum((char) 25);
		// demo.setAuthInfo("rumbergerr", pwd);
		// demo.addHeader("X-Test-Header", "stupidcontent");
		// demo.setXMailer("testmailer");
		//	
		// demo.sendMessage();
	}

}
