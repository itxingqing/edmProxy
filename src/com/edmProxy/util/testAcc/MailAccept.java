package com.edmProxy.util.testAcc;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailAccept {
	 public static void main(String[] args) throws Exception{
	        Properties ps = new Properties();
	        ps.put("mail.smtp.host","server");
	        ps.put("mail.smtp.auth","true");
	        Session mySession = Session.getInstance(ps,null);
	        Store myStore = mySession.getStore("pop3");//Э��
	        myStore.connect("pop3.126.com","lookskystaremail@126.com","8524127");
	        Folder myFolder = myStore.getFolder("INBOX");//�ļ���
	        myFolder.open(Folder.READ_ONLY);//���ļ���

	        Message[] messages = myFolder.getMessages();//��������ʼ�

	        for(int i = 0; i<messages.length ; i++){
	            System.out.println(messages[i].getSubject());//����
	            //System.out.println(messages[i].getContent());//����
	        }
	       // messages[1].writeTo(System.out);
	        System.out.println(messages[1].getContentType());

	    }
}
