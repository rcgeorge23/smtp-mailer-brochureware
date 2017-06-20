package uk.co.novinet.smtpmailer.brochureware.controller;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uk.co.novinet.smtpmailer.brochureware.DictionaryReservoirSampler;

@RestController()
public class SmtpMailController {
	
	private static Log LOGGER = LogFactory.getLog(SmtpMailController.class);

    @Value("${FAKE_SMTP_HOST}")
    private String fakeSmtpHost;

    @Value("${FAKE_SMTP_PORT}")
    private String fakeSmtpPort;
    
	@Resource
	private DictionaryReservoirSampler dictionaryReservoirSampler;
	
	@Resource
	private SmtpMailUrlBuilder smtpMailUrlBuilder;

	@RequestMapping(value="/sendSmtpMail", method = RequestMethod.POST)
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
			result.put("success", true);
			return result;
		} catch (Exception e) {
			LOGGER.error("Could not send message", e);
			throw new RuntimeException(e);
		} finally {
			if (transport != null) {
				transport.close();
			}
		}
	}
	
	@RequestMapping(value="/getJsonSmtpMail", method = RequestMethod.GET)
	public String getJsonSmtpMail(
			@RequestParam("username") String username, 
    		@RequestParam("password") String password, 
    		@RequestParam("toAddress") String toAddress) throws Exception {
		
		HttpURLConnection con = (HttpURLConnection) smtpMailUrlBuilder.build(username, password, toAddress).openConnection();

		con.setRequestMethod("GET");

		con.setRequestProperty("User-Agent", "SMTPBoxBrochureware");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		
		in.close();

		return response.toString();
	}
	
	@RequestMapping(value="/getSmtpMailBean", method = RequestMethod.GET)
	public SmtpMailBean getSmtpMailBean() {
		SmtpMailBean smtpMailBean = new SmtpMailBean();
		smtpMailBean.setUsername(guid());
		smtpMailBean.setPassword(guid());
		smtpMailBean.setFromAddress(dictionaryReservoirSampler.randomEmailAddress());
		smtpMailBean.setToAddress(dictionaryReservoirSampler.randomEmailAddress());
		smtpMailBean.setPlainContent(dictionaryReservoirSampler.randomSentences(4));
		smtpMailBean.setSubject(dictionaryReservoirSampler.randomSentences(1));
		return smtpMailBean;
	}
	
	@RequestMapping(value="/getConfiguration", method = RequestMethod.GET)
	public Map<String, String> getConfiguration() {
		Map<String, String> result = new HashMap<>();
		result.put("fakeSmtpHost", fakeSmtpHost);
		result.put("fakeSmtpPort", fakeSmtpPort);
		return result;
	}
	
	private String guid() {
		return randomUUID().toString().replaceAll("-", "");
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
		if (isNotBlank(contentString)) {
			MimeBodyPart bodyPart = new MimeBodyPart();
			bodyPart.setContent(contentString, contentType);
			multipart.addBodyPart(bodyPart);
		}
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
