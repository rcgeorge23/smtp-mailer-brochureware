package uk.co.novinet.smtpmailer.brochureware.controller;

public class SmtpMailBean {
	private String username;
	private String password;
	private String toAddress;
	private String fromAddress;
	private String subject;
	private String htmlContent;
	private String plainContent;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getPlainContent() {
		return plainContent;
	}

	public void setPlainContent(String plainContent) {
		this.plainContent = plainContent;
	}

}
