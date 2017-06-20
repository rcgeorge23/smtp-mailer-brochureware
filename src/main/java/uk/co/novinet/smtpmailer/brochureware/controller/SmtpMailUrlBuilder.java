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
    	
    	appendParameter(sb, "username", username, false);
    	appendParameter(sb, "password", password, false);
    	appendParameter(sb, "toAddress", toAddress, true);

    	return new URI("http", fakeSmtpHost, "/", sb.toString(), null).toURL();
    }

	private void appendParameter(StringBuilder sb, String parameterName, String parameterValue, boolean isLast) throws UnsupportedEncodingException {
		sb.append(parameterName);
    	sb.append('=');
    	sb.append(parameterValue);
    	if (!isLast) {
    		sb.append('&');
    	}
	}
}
