package httpClientMail;

import javax.mail.Authenticator; 
import javax.mail.PasswordAuthentication; 

//�����������¼��֤ 
public class MailAuthenticator extends Authenticator {
	private String username; // �û�������¼���䣩 
	private String password; // ���� 
	//��ʼ����������� 
	public MailAuthenticator(String username, String password){
		this.username = username; 
		this.password = password; 
	} 
	String getPassword() { 
		return password; 
	} 
	String getUsername() { 
		return username; 
	} 
	public void setPassword(String password) {
		this.password = password; 
	} 

	public void setUsername(String username) {
		this.username = username; 
	} 
	
	@Override 
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	} 
	
	
}
