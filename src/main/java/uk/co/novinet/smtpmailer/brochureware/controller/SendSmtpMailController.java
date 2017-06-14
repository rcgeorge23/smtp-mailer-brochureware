package uk.co.novinet.smtpmailer.brochureware.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SendSmtpMailController {

	@RequestMapping(value="/sendSmtpMail", method=RequestMethod.POST)
	public Map<String, Boolean> sendSmtpMail(@RequestBody SmtpMailBean smtpMailBean) throws AddressException, MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.ssl.trust", "fakesmtp.novinet.co.uk");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.host", "fakesmtp.novinet.co.uk");
		props.put("mail.smtp.user", smtpMailBean.getUsername());
		props.put("mail.smtp.password", smtpMailBean.getPassword());
		props.put("mail.smtp.port", 25);

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		Multipart multipart = new MimeMultipart();

		message.setFrom(new InternetAddress(smtpMailBean.getFromAddress()));
		message.setSubject(smtpMailBean.getSubject());
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(smtpMailBean.getToAddress()));

		MimeBodyPart plainBodyPart = new MimeBodyPart();
		plainBodyPart.setContent(smtpMailBean.getPlainContent(), "text/plain");
		multipart.addBodyPart(plainBodyPart);

		MimeBodyPart htmlBodyPart = new MimeBodyPart();
		htmlBodyPart.setContent(smtpMailBean.getHtmlContent(), "text/html");
		multipart.addBodyPart(htmlBodyPart);

		message.setContent(multipart);

		Transport transport = null;

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		try {
			transport = session.getTransport("smtp");
			transport.connect("fakesmtp.novinet.co.uk", smtpMailBean.getUsername(), smtpMailBean.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
			result.put("success", true);
			return result;
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
}
