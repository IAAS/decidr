/*
 * The Decidr Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.prototype;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Test class for checking the basic functionality of
 * <code>{@link MailBackend}</code>.
 * 
 * @author RR
 */
public class Main {

	public static void main(String[] args) throws AddressException,
			MessagingException, IOException {
		// JavaMailService jms = new JavaMailService();
		//
		// Endpoint.publish("http://127.0.0.1:8080/mailws", jms);
		// JOptionPane.showMessageDialog(null, "srv bndn");
		// System.exit(0);

		MailBackend demo = new MailBackend(
				"test@example.com",
				"test@example.com", "testDemoMail");
		JPasswordField passField = new JPasswordField();
		String pwd;
		if (JOptionPane.showConfirmDialog(null, passField, "password input",
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
			pwd = new String(passField.getPassword());
		else {
			System.exit(1);
			return;
		}

		demo.setBodyText("testestest");
		demo.setHostname("smtp.googlemail.com");
		demo.useTLS(true);
		demo.setCC("test@example.com");
		demo.addCC("test@example.com, test@example.com");
		demo.setPortNum((char) 25);
		demo.setAuthInfo("test@example.com", pwd);
		demo.addHeader("X-Test-Header", "stupidcontent");
		demo.setXMailer("testmailer");

		demo.sendMessage();
	}
}
