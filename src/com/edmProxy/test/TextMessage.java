package com.edmProxy.test;
import java.io.FileOutputStream;  
import java.util.Date;  
import java.util.Properties;  
 
import javax.mail.Message;  
import javax.mail.Session;  
import javax.mail.internet.InternetAddress;  
import javax.mail.internet.MimeMessage;  
/**  
 * �������ı���ʽ���ʼ�������ΪOutlook �� ".eml" �ʼ���ʽ  
 * @author haolloyin  
 */ 
public class TextMessage {  
    public static void main(String[] args) throws Exception{  
          
        String from = "test_hao@sina.cn";  
        String to = "test_hao@163.com";  
        String subject = "����һ�����ı��ʼ���";  
        String body = "���ı��ʼ����ԣ�����";  
          
        // �������ʼ�Ӧ�ó�������Ļ�����Ϣ�Լ��Ự��Ϣ  
        Session session = Session.getDefaultInstance(new Properties());  
          
        // ��������� Session ʵ������ MimeMessage ʵ������һ���ʼ�  
        MimeMessage msg = new MimeMessage(session);  
          
        // ���÷����˵�ַ  
        msg.setFrom(new InternetAddress(from));  
          
        // �����ռ��˵�ַ  
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));  
          
        // ���� E-mail ����  
        msg.setSubject(subject);  
          
        // ���÷���ʱ��  
        msg.setSentDate(new Date());  
          
        // ���� E-mail ���Ĳ���  
        msg.setText(body);  
          
        // ���뱣��Ը� MimeMessage ʵ���ĸ���  
        msg.saveChanges();  
          
        // �� msg ����������д�뵱ǰ�ļ���textMail.eml�ļ���  
        msg.writeTo(new FileOutputStream("textMail.eml"));  
    }  
} 