package com.edmProxy.util.mail.send;

import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
import javax.mail.internet.MimeUtility;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;


public class CopyOfSendMail {
	
	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
		CopyOfSendMail s=new CopyOfSendMail();
		
		AccountEntity accountEntity=new AccountEntity();
		accountEntity.setAccount("duhuai8416@126.com");
		accountEntity.setPassword("ercui534");
		accountEntity.setPost("126.com");
		
		
		
		ArrayList<String> receiveList=new ArrayList<String>();
		receiveList.add("lookskystaremail@126.com");
		
		String contentStr=MailUtil.readTemplateMail("F:\\mobang.html");
		
		s.sendMailTrue(accountEntity, receiveList, null, "test", contentStr, 0);
	}
	
	
	
	
	
	
	private static ArrayList<String> accessoryMailList;
	
	//���ô�������,javamailֻ֧��scoket
	public static void setProxyHost(Properties properties){//ProxyEntiy proxyEntiy
//		System.getProperties().setProperty("proxySet", "true");//��������ã�ֻҪ����IP�ʹ���˿���ȷ,�������Ҳ����  
//		System.getProperties().setProperty("http.proxyHost", "23.104.72.2");  
//		System.getProperties().setProperty("http.proxyPort", "808"); 

		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", "23.109.91.98"); //proxyEntiy.getProxyHost()
		properties.setProperty("socksProxyPort", "1080"); //proxyEntiy.getProxyPort();
		Authenticator.setDefault(new Authenticator(){
      	   protected PasswordAuthentication getPasswordAuthentication() {
      	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
      	 }
		});
	}
	
	//�����ʼ���������Ϣ
	public static void setMialHost(Properties properties,AccountEntity accountEntity){
		properties.setProperty("mail.transport.protocol","smtp");    //����smtpЭ���ֵ
		properties.setProperty("mail.smtp.host", "smtp."+accountEntity.getPost());// ���÷�����Ip��������ֵ
		properties.setProperty("mail.smtp.port", "25");// ���÷�����Ip�˿ںź�ֵ
		properties.setProperty("mail.smtp.auth", "true"); //�����Ƿ�ͨ����֤��ֵ
	}
	
	//�����ռ���
	public static void setReveice(ArrayList<String> receiveMailInfoList,MimeMessage mimeMessage) throws MessagingException{
		//to:�ռ��˵�ַ,һ���ռ��˵�ַ
		int receiveMailCount=receiveMailInfoList.size();
		 if (receiveMailCount > 0) {
			//�½�һ����ַ���飬���ȵ���receiveMailList�ĳ���
			InternetAddress[] address = new InternetAddress[receiveMailCount];
			//��receiveMailList�����е����ݿ���address
			 for (int i = 0; i < receiveMailCount; i++) {
			   String receiveMailStr =receiveMailInfoList.get(i);
			   //System.out.println("receiveMailStr--"+receiveMailStr);
			   if(receiveMailStr.trim().length() > 0){
				   address[i] = new InternetAddress(receiveMailStr);  
			   }else{
				   System.out.println("��ַΪ��!");
			   }
      	     }
			 //�����ռ��˵ĵ�ַ�ͷ��ͷ�ʽ//TO�����ռ��ˣ�CC�������ռ��ˣ�BCC����������
			 mimeMessage.addRecipients(Message.RecipientType.TO, address);
		 }else{
			 System.out.println("û���ռ����ʼ���ַ��! �����˳�!");
		 }
	}
	
	//���ø���
	public static void setAccessorys(Multipart multipartMain,MimeMessage mimeMessage,ArrayList<String> list) throws MessagingException, UnsupportedEncodingException{
		accessoryMailList=new ArrayList<String>();
		accessoryMailList=list;
		//accessoryMailList.add("F:\\mobang.html");
		int accessoryMailCount=accessoryMailList.size();
		if(accessoryMailCount>-1){
			for (int i = 0; i < accessoryMailCount; i++) {
				//�����ʼ�����MimeBodyPart���� 
				MimeBodyPart mimeBodyPartMccessory = new MimeBodyPart(); 
				//ѡ���������
				String accessoryPath =(String)accessoryMailList.get(i);
				//�õ�����Դ���Ѹ����ŵ�����Դ�У�
				FileDataSource fileDataSource = new FileDataSource(accessoryPath);
				 /*
     	        * javax.activation.DataHandler��(������JAF��)JavaMail API��������ϢֻΪ�ı�,
     	        * �κ���ʽ����Ϣ�����������Ը�MimeMessage��һ����.�����ı���Ϣ,��Ϊ�ļ����������ڵ����ʼ���Ϣ��һ�����Ǻ��ձ��.
     	        * JavaMail APIͨ��ʹ��DataHandler����,�ṩһ���������ǰ������ı�BodyPart����ļ�㷽��. 
     	        */
				DataHandler dataHandler=new DataHandler(fileDataSource);
				//�õ�������������mccessoryMimeBodyPart
				mimeBodyPartMccessory.setDataHandler(dataHandler);
				//�õ��ļ���ͬ������mccessoryMimeBodyPart
     	        mimeBodyPartMccessory.setFileName(fileDataSource.getName());
     	        
     	       //�������������������
     	       String fileName=accessoryPath.substring(accessoryPath.lastIndexOf("\\")+1,accessoryPath.length());
     	       //System.out.println("�������͵ĸ���Ϊ--��:"+fileName);
     	       mimeBodyPartMccessory.setFileName(MimeUtility.encodeText("����"));
     	       //System.out.println("------------------===>:"+mimeBodyPartMccessory.getFileName());
     	       //���ظ������е�ͼƬ<img src="cid:top_bg.jpg" /> Ϊ�����е�ͼƬ����content-id,����html��ʽ�ʼ��оͿ�����ʾ�����е�ͼƬ��
    	       mimeBodyPartMccessory.addHeader("content-id", mimeBodyPartMccessory.getFileName());
    	       //���ʼ����������ʼ�����multipartMain
    	       multipartMain.addBodyPart(mimeBodyPartMccessory);
			}
			//Multipart���뵽�ż�(���ʼ�������ӵ�������Ϣ��)
		    mimeMessage.setContent(multipartMain);
		    //���߼����е�����Ԫ�أ����List���еĸ�����
		    accessoryMailList.removeAll(accessoryMailList);
		}
	}
	//�������������
	public static void setTitleContent(MimeMessage mimeMessage,Multipart multipartMain,String titleMail,String contentMail,int flag) throws MessagingException{
		//�����ʼ�����
		 mimeMessage.setSubject(titleMail);
		 //MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ����ʼ����壩���ʼ�����
		//�������ݣ���html���ı�
//		 if(flag==0){
			//����һ������HTML���ݵ�MimeBodyPart��html�ʼ��������ݣ�
			 MimeBodyPart mimeBodyPartHtml = new MimeBodyPart();
			//����html�ʼ��������ݺ�ҳ��ı����ʽ
			mimeBodyPartHtml.setContent(contentMail, "text/html ;charset=gbk");
			//��html�ʼ�����������ӵ�multipartMain������
			multipartMain.addBodyPart(mimeBodyPartHtml);
			mimeMessage.setContent(multipartMain);
//		 }else if(flag==1){
//			 mimeMessage.setText(contentMail);  
//		 }
	}
	
	
	//�����ʼ�                                                                                       //����                                //htmlģ��                
	public void sendMailTrue(AccountEntity accountEntity,ArrayList<String> receiveList,ArrayList<String> accessoryMailList,String titleMail,String contentMail,int state) throws MessagingException, UnsupportedEncodingException{
		//String templateMail = MailUtil.readTemplateMail("F:\\mobang.html");//mail.getTemplateMail());// ��ȡ�����ʼ�ģ��
		//String titleMail = "test";
		//System.out.println(accountEntity.getAccount()+":"+accountEntity.getPassword()+":"+accountEntity.getPost());
		// �����֤�����˵�ַ������
		PopupAuthenticator popupAuthenticator = new PopupAuthenticator();// �����ʼ����ͷ����������֤
		popupAuthenticator.performCheck(accountEntity.getAccount(),accountEntity.getPassword()); // ��˾�Լ�������ʼ��������û���������,���뷢���˵�ַ�ͷ��������루��������ʵ���ڵģ������������ϵģ�

		//��װ����������,Properties��ʾһ���־õ����Լ���Properties�ɱ��������м��ء������б���ÿ���������Ӧֵ����һ���ַ���,����Properties�����û���ȡProperties�ļ���
		//Properties properties = System.getProperties();
		Properties properties = new Properties();
//		if(state==1){//==1ʹ�ô���
//			setProxyHost(properties);//���ô�������
//		}
		setMialHost(properties,accountEntity);//�����ʼ���������Ϣ
		Session session = Session.getDefaultInstance(properties,popupAuthenticator);// �õ�Ĭ�ϵĶԻ�����������ϵͳ�����͵����ʼ����ͷ����������֤���ݣ�

//		try {
			// ��ʼ�����ʼ�����(��Ҫ�����쳣����)
			/*
			 * javax.mail.internet,MinmeMessage�ࣺ�������ʵ����Ϣ��ģ�Ͱ塣�����״δ���ʱ�����Ĺ��ڸ���Ϣ����Ϣ����ֵ�������ޣ�
			 * ���ź����ķ����Ӹ���Ϣ���ҵ�������ֵ�������������洢��Щ��ֵ��
			 * Message���󽫴洢����ʵ�ʷ��͵ĵ����ʼ���Ϣ��Message������Ϊһ��MimeMessage����������������Ҫ֪��Ӧ��ѡ����һ��JavaMail
			 * session��
			 */
			// ����һ����Ϣ������ʼ������Ϣ�ĸ���Ԫ��
			MimeMessage mimeMessage = new MimeMessage(session);
			/*
			 * һ���������� Session �� Message����������������Ϣ�󣬾Ϳ�����Addressȷ���ż���ַ�ˡ� �� Message
			 * һ����Address Ҳ�Ǹ������ࡣ���õ���Javax.mail.internet.InternetAddress ��.
			 * this.from�������˵ĵ�ַ
			 */
			String from=new String(accountEntity.getAccount().getBytes("GBK"),"ISO-8859-1");
			//System.out.println("from-->"+from);
			InternetAddress internetAddress = new InternetAddress(from);
			// �ѷ����˵ĵ�ַ���õ���Ϣ��
			mimeMessage.setFrom(internetAddress);
			Multipart multipartMain = new MimeMultipart(); 
			
			setReveice(receiveList,mimeMessage);//�����ռ���
			//System.out.println("receiveMailInfoList--->"+receiveList.get(0));
			//setAccessorys(multipartMain,mimeMessage,accessoryMailList);//���ø���
			setTitleContent(mimeMessage,multipartMain,titleMail,contentMail,0);//�������������
			 
			//�����ż�ͷ�ķ�������
		    mimeMessage.setSentDate(new Date());
		    //���淢����Ϣ��
		    mimeMessage.saveChanges();
		    //�����ż�
		    Transport transport = session.getTransport("smtp");
		    
		    //System.out.println("smtp."+accountEntity.getPost()+"-"+accountEntity.getAccount()+"-"+accountEntity.getPassword());
		    transport.connect("smtp."+accountEntity.getPost(), accountEntity.getAccount(),accountEntity.getPassword());
		    transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
		    transport.close();
		    
		    removeLocalSendMailInfo();
		    
		    try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//		} catch (AddressException e) {
//			System.out.println("�쳣1");
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			System.out.println("MessagingException�쳣�����ӳ�ʱ��˵������������ǵ�����");
//			e.printStackTrace();
//		} 
//		catch (UnsupportedEncodingException e) {
//			System.out.println("�쳣3");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("�쳣4");
//			e.printStackTrace();
//		}

	}
	
	
	public void removeLocalSendMailInfo()   
	{   
		Properties prop = new Properties();   
		prop.remove("proxySet");   
		prop.remove("socksProxyHost");   

		prop.remove("mail.transport.protocol");   
		prop.remove("mail.smtp.host");   
		prop.remove("mail.smtp.port"); 
		prop.remove("mail.smtp.auth"); 
	}   
	
	
	
}
