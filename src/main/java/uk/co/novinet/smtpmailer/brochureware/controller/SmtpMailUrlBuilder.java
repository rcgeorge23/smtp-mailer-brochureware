package uk.co.novinet.smtpmailer.brochureware.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailUrlBuilder {

    @Value("${FAKE_SMTP_HOST}")
    private String fakeSmtpHost;
    
    public URL build(String username, String password, String toAddress) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	
    	appendParameter(sb, "username", username);
    	appendParameter(sb, "password", password);
    	appendParameter(sb, "toAddress", toAddress);

    	return new URI("http", fakeSmtpHost, "/", sb.toString()).toURL();
    }

	private void appendParameter(StringBuilder sb, String parameterName, String parameterValue) throws UnsupportedEncodingException {
		sb.append(URLEncoder.encode(parameterName, "UTF-8"));
    	sb.append('=');
    	sb.append(URLEncoder.encode(parameterValue, "UTF-8"));
	}
}
