package httpClientMail;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage.RecipientType;

//���ʼ����������ɵ�����Ⱥ���� 
public class SimpleMailSender {
	// �����ʼ���props�ļ� 
	private final transient Properties props = System.getProperties();
	// �ʼ���������¼��֤ 
	private transient MailAuthenticator authenticator;
	// ����session 
	private transient Session session;
	
	/** 
	* ��ʼ���ʼ������� 
	* @param smtpHostName SMTP�ʼ���������ַ 
	* @param username �����ʼ����û���(��ַ) 
	* @param password �����ʼ������� 
	*/ 
	public SimpleMailSender(final String smtpHostName,final String username,final String password) { 
		init(username, password, smtpHostName); 
	} 
	/** 
	* ��ʼ���ʼ������� 
	* @param username �����ʼ����û���(��ַ)�����Դ˽���SMTP��������ַ 
	* @param password �����ʼ������� 
	*/ 
	public SimpleMailSender(final String username,final String password) {
		// ͨ�������ַ������smtp���������Դ�������䶼���� 
		final String smtpHostName = "smtp." + username.split("@")[1]; 
		init(username, password, smtpHostName); 
	} 
	
	/** 
	* ��ʼ�� 
	* @param username �����ʼ����û���(��ַ) 
	* @param password ���� 
	* @param smtpHostName SMTP������ַ 
	*/ 
	private void init(String username, String password, String smtpHostName) {
		
		//����
		props.put("proxySet", "true");//��������ã�ֻҪ����IP�ʹ���˿���ȷ,�������Ҳ����  
		props.put("http.proxyHost", "23.104.72.3");  
		props.put("http.proxyPort", "808"); 
		Authenticator.setDefault(new Authenticator(){
     	   protected PasswordAuthentication getPasswordAuthentication() {
     	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
     	 }
		});
		// ��ʼ��props 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.host", smtpHostName); 
		// ��֤ 
		authenticator = new MailAuthenticator(username, password);
		// ����session 
		session = Session.getInstance(props, authenticator); 
	} 
	/** 
	* �����ʼ� 
	* @param recipient�ռ��������ַ 
	* @param subject�ʼ����� 
	* @param content�ʼ����� 
	* @throws AddressException 
	* @throws MessagingException 
	*/ 
	public void send(String recipient, String subject, String content)throws AddressException, MessagingException { 
		// ����mime�����ʼ� 
		final MimeMessage message = new MimeMessage(session); 
		// ���÷����� 
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ��� 
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		// �������� 
		message.setSubject(subject); 
		// �����ʼ����� 
		Multipart mp = new MimeMultipart("related");
		MimeBodyPart mbp = new MimeBodyPart(); 
		mbp.setContent(content.toString(),"text/html;charset=utf-8");
		mp.addBodyPart(mbp); 
		message.setContent(mp); 


		// �����ʼ����� 
		// message.setContent(content.toString(), "text/html;charset=utf-8");
		// ���� 
		Transport.send(message); 
	}
	/** 
	* Ⱥ���ʼ� 
	* @param recipients�ռ����� 
	* @param subject ���� 
	* @param content ���� 
	* @throws AddressException 
	* @throws MessagingException 
	*/ 
	public void send(List<String> recipients, SimpleMail simpleMail)throws AddressException, MessagingException { 
		// ����mime�����ʼ� 
		final MimeMessage message = new MimeMessage(session); 
		// ���÷����� 
		message.setFrom(new InternetAddress(authenticator.getUsername()));
		// �����ռ����� 
		final int num = recipients.size();
		InternetAddress[] addresses = new InternetAddress[num];
		for (int i = 0; i < num; i++) {
			addresses[i] = new InternetAddress(recipients.get(i));
		} 
		message.setRecipients(RecipientType.TO, addresses); 
		// �������� 
		message.setSubject(simpleMail.getSubject()); 
		// �����ʼ����� 
		message.setContent(simpleMail.getContent(), "text/html;charset=utf-8");
		// ���� 
		Transport.send(message); 
	} 
	
	/** 
	* �����ʼ� 
	* @param recipient�ռ��������ַ 
	* @param mail�ʼ����� 
	* @throws AddressException 
	* @throws MessagingException 
	* �� 
	*/ 
	public void send(String recipient, SimpleMail mail)throws AddressException, MessagingException { 
		send(recipient, mail.getSubject(), mail.getContent()); 
	} 
	

}
