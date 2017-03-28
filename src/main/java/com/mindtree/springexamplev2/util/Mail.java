package com.mindtree.springexamplev2.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import com.mindtree.springexamplev2.exception.ServiceException;

public class Mail {

	//

	public void sendMail(String mailId) throws ServiceException, MessagingException  {
		final String FROM = "akansha.jain@cloud-practice.in"; // Replace with
																// your
																// "From"
																// address.

		// This address must
		// be verified.
		final String TO = mailId; // Replace with a "To"
									// address. If your
									// account is still in
									// the
									// sandbox, this address
									// must be verified.

		final String BODY = "Your Data is Completely Deleted";
		final String SUBJECT = "Regarding Deletion Of Data";

		// Supply your SMTP credentials below. Note that your SMTP credentials
		// are different from your AWS credentials.

		// The port you will connect to on the Amazon SES SMTP endpoint. We are
		// choosing port 25 because we will use
		// STARTTLS to encrypt the connection.
		System.out.println("Helllooo u are in mail.java file");
		final int PORT = 25;
		Properties prop = new Properties();
		InputStream input = null;
		//input = new FileInputStream("");
		input =getClass().getClassLoader().getResourceAsStream("credentials.properties");
		System.out.println("-----------------------"+input.hashCode());
		try{
		prop.load(input);
		// Create a Properties object to contain connection configuration
		// information.
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
		System.out.println("size of prop"+prop.size());
		
		String SMTP_USERNAME = prop.getProperty("smtp_username"); // Replace
																	// with
		// your SMTP
		// username.
		String SMTP_PASSWORD = prop.getProperty("smtp_password");

		// Amazon SES SMTP host name. This example uses the US West (Oregon)
		// region.
		String HOST = prop.getProperty("host");
		System.out.println("..............."+SMTP_USERNAME+"host........................"+HOST+"Password.."+SMTP_PASSWORD);
		System.out.println("before prop");
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", 25);

		// Set properties indicating that we want to use STARTTLS to encrypt the
		// connection.
		// The SMTP session will begin on an unencrypted connection, and then
		// the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the
		// specified properties.
		Transport transport=null;
		Session session = Session.getDefaultInstance(props);
		try {
		// Create a message with the specified information.
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(FROM));
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
		msg.setSubject(SUBJECT);
		msg.setContent(BODY, "text/plain");

		// Create a transport.
		transport = session.getTransport();

		// Send the message.
		
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");

			// Connect to Amazon SES using the SMTP username and password you
			// specified above.
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			System.out.println("coonect-------------------------");
			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		} catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
			ex.printStackTrace();
			throw new ServiceException(ex.getMessage(),ex);
		} finally {
			// Close and terminate the connection.
			transport.close();
		}
	}
}