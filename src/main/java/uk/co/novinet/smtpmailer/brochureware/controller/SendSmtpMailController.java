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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class SendSmtpMailController {

    @Value("${FAKE_SMTP_HOST}")
    private String fakeSmtpHost;

    @Value("${FAKE_SMTP_PORT}")
    private String fakeSmtpPort;

	@RequestMapping(value="/sendSmtpMail", method=RequestMethod.POST)
	public Map<String, Boolean> sendSmtpMail(@RequestBody SmtpMailBean smtpMailBean) throws AddressException, MessagingException {
		Properties smtpProperties = buildSmtpProperties(smtpMailBean);

		Session session = Session.getDefaultInstance(smtpProperties, null);
		
		MimeMessage message = buildMessage(smtpMailBean, session);

		Transport transport = null;

		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		try {
			transport = session.getTransport("smtp");
			transport.connect(fakeSmtpHost, smtpMailBean.getUsername(), smtpMailBean.getPassword());
			transport.sendMessage(message, message.getAllRecipients());

		} finally {
			if (transport != null) {
				transport.close();
			}
		}
		result.put("success", true);
		return result;
	}

	private MimeMessage buildMessage(SmtpMailBean smtpMailBean, Session session) throws MessagingException, AddressException {
		MimeMessage message = new MimeMessage(session);
		Multipart multipart = new MimeMultipart();
		message.setFrom(new InternetAddress(smtpMailBean.getFromAddress()));
		message.setSubject(smtpMailBean.getSubject());
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(smtpMailBean.getToAddress()));
		addContentToMultipart(multipart, "text/plain", smtpMailBean.getPlainContent());
		addContentToMultipart(multipart, "text/html", smtpMailBean.getHtmlContent());
		message.setContent(multipart);
		return message;
	}

	private void addContentToMultipart(Multipart multipart, String contentType, String contentString) throws MessagingException {
		MimeBodyPart bodyPart = new MimeBodyPart();
		bodyPart.setContent(contentString, contentType);
		multipart.addBodyPart(bodyPart);
	}

	private Properties buildSmtpProperties(SmtpMailBean smtpMailBean) {
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.ssl.trust", fakeSmtpHost);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.host", fakeSmtpHost);
		props.put("mail.smtp.user", smtpMailBean.getUsername());
		props.put("mail.smtp.password", smtpMailBean.getPassword());
		props.put("mail.smtp.port", Integer.parseInt(fakeSmtpPort.trim()));
		return props;
	}
}
