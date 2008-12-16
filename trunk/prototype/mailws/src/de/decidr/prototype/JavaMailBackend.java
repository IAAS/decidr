/**
 * 
 */
package de.decidr.prototype;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * JavaMail demo class for the Decidr prototype.
 * 
 * @author RR
 */
public class JavaMailBackend {

	/**
	 * Header <code>{@link String strings}</code>.
	 */
	private String headerTo, headerFrom, headerSubject, headerCC, headerBCC,
			headerXMailer;

	/**
	 * A map containing additional headers and their contents.
	 */
	private Map<String, String> additionalHeaderMap = new HashMap<String, String>();

	/**
	 * The hostname of the SMTP server. Defaults to localhost.
	 */
	private String SMTPServerHost = "localhost";

	/**
	 * The port numer of the SMTP server. Defaults to 25.
	 */
	private char SMTPPortNum = 25;

	/**
	 * The authentification information. If unset, the server is assumed not to
	 * authenticate.
	 */
	private String username, password;

	/**
	 * List of parts. If there are more than one, a multipart message is
	 * constructed.
	 */
	private List<MimeBodyPart> messageParts = new Vector<MimeBodyPart>();

	/**
	 * 
	 * 
	 * @param to
	 * @param from
	 * @param subject
	 * @throws MessagingException
	 */
	public JavaMailBackend(String to, String from, String subject)
			throws MessagingException {
		super();
		this.headerTo = to;
		this.headerFrom = from;
		this.headerSubject = subject;

		// make sure there is at least a body
		messageParts.add(0, new MimeBodyPart());
		messageParts.get(0).setText("");
	}

	/**
	 * @param message
	 * @throws MessagingException
	 */
	public void setBodyText(String message) throws MessagingException {
		if (messageParts.isEmpty()) {
			messageParts.add(0, new MimeBodyPart());
		}

		messageParts.get(0).setText(message);
	}

	/**
	 * @param part
	 */
	public void addMimePart(MimeBodyPart part) {
		if (messageParts.isEmpty()) {
			messageParts.add(0, new MimeBodyPart());
		}

		messageParts.add(part);
	}

	/**
	 * @param parts
	 */
	public void addMimeParts(List<MimeBodyPart> parts) {
		if (messageParts.isEmpty()) {
			messageParts.add(0, new MimeBodyPart());
		}

		messageParts.addAll(parts);
	}

	/**
	 * @param part
	 */
	public void removeMimePart(MimeBodyPart part) {
		messageParts.remove(part);
	}

	/**
	 * @param parts
	 */
	public void removeMimeParts(List<MimeBodyPart> parts) {
		messageParts.removeAll(parts);
	}

	/**
	 * @param headerName
	 * @param headerContent
	 */
	public void addHeader(String headerName, String headerContent) {
		additionalHeaderMap.put(headerName, headerContent);
	}

	/**
	 * @param headers
	 */
	public void addHeaders(Map<String, String> headers) {
		additionalHeaderMap.putAll(headers);
	}

	/**
	 * @param headerName
	 */
	public void removeHeader(String headerName) {
		additionalHeaderMap.remove(headerName);
	}

	/**
	 * @param headers
	 */
	public void removeHeaders(List<String> headers) {
		for (String header : headers) {
			additionalHeaderMap.remove(header);
		}
	}

	/**
	 * @param cc
	 */
	public void setCC(String cc) {
		headerCC = cc;
	}

	/**
	 * @param cc
	 */
	public void addCC(String cc) {
		headerCC += ", " + cc;
	}

	/**
	 * @param bcc
	 */
	public void setBCC(String bcc) {
		headerBCC = bcc;
	}

	/**
	 * @param bcc
	 */
	public void addBCC(String bcc) {
		headerBCC += ", " + bcc;
	}

	/**
	 * @param newXMailer
	 */
	public void setXMailer(String newXMailer) {
		headerXMailer = newXMailer;
	}

	/**
	 * @param hostname
	 * @param portNum
	 */
	public void setServerInfo(String hostname, char portNum) {
		SMTPServerHost = hostname;
		SMTPPortNum = portNum;
	}

	/**
	 * @param hostname
	 */
	public void setHostname(String hostname) {
		SMTPServerHost = hostname;
	}

	/**
	 * @param portNum
	 */
	public void setPortNum(char portNum) {
		SMTPPortNum = portNum;
	}

	/**
	 * @param username
	 * @param password
	 */
	public void setAuthInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws IOException
	 * 
	 */
	public void sendMessage() throws AddressException, MessagingException,
			IOException {
		Properties sysProps = System.getProperties();
		Session session = Session.getInstance(sysProps);

		Message message = new MimeMessage(session);
		if (headerTo == null || headerTo.isEmpty())
			throw new IllegalArgumentException(
					"You need to specify a receiver!");
		if (headerFrom == null || headerFrom.isEmpty())
			throw new IllegalArgumentException("You need to specify a sender!");
		if (headerSubject == null)
			headerSubject = "";

		message.setFrom(new InternetAddress(headerFrom));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
				headerTo, false));
		if (headerCC != null)
			message.setRecipients(Message.RecipientType.CC, InternetAddress
					.parse(headerCC, false));
		if (headerBCC != null)
			message.setRecipients(Message.RecipientType.BCC, InternetAddress
					.parse(headerBCC, false));
		message.setSubject(headerSubject);

		if (messageParts.size() == 0) {
			throw new IllegalArgumentException(
					"You need to add some content to your message.");
		} else if (messageParts.size() == 1
				&& messageParts.get(0).getContent() instanceof String) {
			message.setText((String) (messageParts.get(0).getContent()));
		} else {
			MimeMultipart multipartMessage = new MimeMultipart();
			for (MimeBodyPart part : messageParts) {
				multipartMessage.addBodyPart(part);
			}
			message.setContent(multipartMessage);
		}

		if (additionalHeaderMap != null) {
			for (String header : additionalHeaderMap.keySet()) {
				message.setHeader(header, additionalHeaderMap.get(header));
			}
		}

		message.setHeader("X-Mailer", headerXMailer);
		message.setSentDate(new Date());

		Transport SMTPTransport = session.getTransport("smtp");
		if (SMTPServerHost == null)
			SMTPServerHost = "localhost";
		SMTPTransport.connect(SMTPServerHost, SMTPPortNum, username, password);
		message.saveChanges();
		SMTPTransport.sendMessage(message, message.getAllRecipients());
		SMTPTransport.close();
	}

	public static void main(String[] args) throws AddressException,
			MessagingException, IOException {
		JavaMailBackend demo = new JavaMailBackend(
				"rumberrd@studi.informatik.uni-stuttgart.de",
				"rumbergerr@yahoo.de", "testDemoMail");
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
		demo.setHostname("smtp.mail.yahoo.de");
		demo.setCC("rumbergerr@gmail.com");
		demo.addCC("rrumberger@gmx.de, rrumberger@web.de");
		demo.setPortNum((char) 25);
		demo.setAuthInfo("rumbergerr", pwd);
		demo.addHeader("X-Test-Header", "stupidcontent");
		demo.setXMailer("testmailer");

		demo.sendMessage();
	}
}
