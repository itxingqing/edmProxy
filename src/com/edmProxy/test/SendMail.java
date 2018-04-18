package com.edmProxy.test;

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




public class SendMail {
	//�����ʼ�2 ʵ����ʽ
	public static void SendMailTrue(SendEmailEntiy sendEmailEntiy,ArrayList<String> receiveMailInfoList){
        
//		ArrayList<String> receiveMailList=new ArrayList();
//		receiveMailList=receiveMailInfoList;
		
		
		String templateMail = MailUtil.readTemplateMail("F:\\mobang.html");//mail.getTemplateMail());// ��ȡ�����ʼ�ģ��
		String titleMail = "�ʼ�����";
		// �����֤�����˵�ַ������
		PopupAuthenticator popupAuthenticator = new PopupAuthenticator();// �����ʼ����ͷ����������֤
		popupAuthenticator.performCheck(sendEmailEntiy.getSendAccount(),sendEmailEntiy.getPassword()); // ��˾�Լ�������ʼ��������û���������,���뷢���˵�ַ�ͷ��������루��������ʵ���ڵģ������������ϵģ�

		Properties properties = System.getProperties();//��װ����������,Properties��ʾһ���־õ����Լ���Properties�ɱ��������м��ء������б���ÿ���������Ӧֵ����һ���ַ���,����Properties�����û���ȡProperties�ļ���
		
		
//		System.getProperties().setProperty("proxySet", "true");//��������ã�ֻҪ����IP�ʹ���˿���ȷ,�������Ҳ����  
//		System.getProperties().setProperty("http.proxyHost", "23.104.72.2");  
//		System.getProperties().setProperty("http.proxyPort", "808"); 
		
		
		properties.setProperty("proxySet", "true");
		properties.setProperty("socksProxyHost", "23.104.72.2");
		properties.setProperty("socksProxyPort", "808");
		Authenticator.setDefault(new Authenticator(){
      	   protected PasswordAuthentication getPasswordAuthentication() {
      	       return new PasswordAuthentication("user001", new String("123456").toCharArray());
      	 }
		});
	        
		
		
		properties.setProperty("mail.transport.protocol","smtp");    //����smtpЭ���ֵ
		properties.setProperty("mail.smtp.host", sendEmailEntiy.getHost());// ���÷�����Ip��������ֵ
		properties.setProperty("mail.smtp.port", sendEmailEntiy.getPort());// ���÷�����Ip�˿ںź�ֵ
		properties.setProperty("mail.smtp.auth", "true"); //�����Ƿ�ͨ����֤��ֵ
		
		Session session = Session.getDefaultInstance(properties,
				popupAuthenticator);// �õ�Ĭ�ϵĶԻ�����������ϵͳ�����͵����ʼ����ͷ����������֤���ݣ�

		try {
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
			// this.from=new String(from.getBytes("GBK"),"ISO-8859-1");
			InternetAddress internetAddress = new InternetAddress(sendEmailEntiy.getSendAccount());
			// �ѷ����˵ĵ�ַ���õ���Ϣ��
			mimeMessage.setFrom(internetAddress);
			
		    //to:�ռ��˵�ַ,һ���ռ��˵�ַ
			int receiveMailCount=receiveMailInfoList.size();
			 if (receiveMailCount > 0) {
				//�½�һ����ַ���飬���ȵ���receiveMailList�ĳ���
				InternetAddress[] address = new InternetAddress[receiveMailCount];
				//��receiveMailList�����е����ݿ���address
				 for (int i = 0; i < receiveMailCount; i++) {
				   String receiveMailStr =receiveMailInfoList.get(i);
				   System.out.println("receiveMailStr--"+receiveMailStr);
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
			 //�����ʼ�����
			 mimeMessage.setSubject(titleMail);
			 //MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ����ʼ����壩���ʼ�����
			 Multipart multipartMain = new MimeMultipart(); 
			//����һ������HTML���ݵ�MimeBodyPart��html�ʼ��������ݣ�
			 MimeBodyPart mimeBodyPartHtml = new MimeBodyPart();
			//����html�ʼ��������ݺ�ҳ��ı����ʽ
			mimeBodyPartHtml.setContent("F:\\mobang.html", "text/html ;charset=gbk");
			//��html�ʼ�����������ӵ�multipartMain������
			multipartMain.addBodyPart(mimeBodyPartHtml);
			
			//������ӷ��Ͷ������
			ArrayList accessoryMailList=new ArrayList();
			accessoryMailList.add("F:\\mobang.html");
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
			//�����ż�ͷ�ķ�������
		    mimeMessage.setSentDate(new Date());
		    //���淢����Ϣ��
		    mimeMessage.saveChanges();
		    //�����ż�
		    Transport transport = session.getTransport("smtp");
		    
		    transport.connect(sendEmailEntiy.getHost(), sendEmailEntiy.getSendAccount(),sendEmailEntiy.getPassword());
		    transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
		    transport.close();
		    
		    Thread.sleep(3000);

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
