package com.edmProxy.util.mail.receive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import com.edmProxy.entity.AccountEntity;
import com.sun.mail.pop3.POP3Folder;

public class AcceptMail {
	 public static void main(String[] args) throws Exception{
//	        Properties ps = new Properties();
//	        ps.put("mail.smtp.host","server");
//	        ps.put("mail.smtp.auth","true");
//	        Session mySession = Session.getInstance(ps,null);
//	        Store myStore = mySession.getStore("pop3");//Э��
//	        myStore.connect("pop3.126.com","lookskystaremail@126.com","8524127");
//	        Folder myFolder = myStore.getFolder("INBOX");//�ļ���
//	        myFolder.open(Folder.READ_ONLY);//���ļ���
//
//	        Message[] messages = myFolder.getMessages();//��������ʼ�
//
//	        for(int i = 0; i<messages.length ; i++){
//	            System.out.println(messages[i].getSubject());//����
//	            //System.out.println(messages[i].getContent());//����
//	            messages[i].setFlag(Flags.Flag.SEEN, true);
//	            messages[i].setFlag(Flags.Flag.DELETED, true);
//	            myFolder.close(true);
//	            System.out.println("ɾ���ɹ�");
//	        }
//	        
//	       // messages[1].writeTo(System.out);
//	        //System.out.println(messages[1].getContentType());
		 
		 
		 //AcceptMail.mailReceive("looktestandy@163.com", "andy123456", "imap.163.com", "33018953@qq.com", "test");
	    }  

	 //imap�����ʼ�           //��ȡ���˺ţ����룬�ʾ֣���ѯ���˺��ʼ��ķ����ˡ�����
	 public static String mailReceive(String reveiceEmail,String password,String post,AccountEntity accountEntity,String searchSubject){
		 	String msg="";
		 // ����pop3����������������Э�顢�û���������  
//	        String pop3Server = "pop3.qq.com";  
//	        String protocol = "pop3";  
	        String server = "imap."+post;//"imap.163.com"; 
	        //System.out.println(server);
	        String temp=server.substring(0, server.indexOf("."));
	        
	        String protocol = temp;//"imap";  
	        String user = reveiceEmail;//"looktestandy@163.com";  
	        String pwd = password;//"andy123456";  
	         
	        
	       
	        // ����һ���о���������Ϣ��Properties����  
	        Properties props = new Properties();  
	        props.setProperty("mail.store.protocol", protocol);  
	        //props.setProperty("mail.pop3.host", pop3Server);
	        props.setProperty("mail."+protocol+".host", server);  
	          
	        // ʹ��Properties������Session����  
	        Session session = Session.getInstance(props);  
	        session.setDebug(true);  
	        try {
	        	// ����Session������Store���󣬲�����pop3������  
		        Store store = session.getStore();  
		        store.connect(server, user, pwd);  
		          
		        // ��������ڵ��ʼ���Folder������"��-д"��  
		        Folder folder = store.getFolder("inbox");  
		        folder.open(Folder.READ_WRITE);  
		          
		          
		        // ����������Ϊ test_hao@sina.cn ������Ϊ"����1"���ʼ�  
		        SearchTerm st = new AndTerm(  
		                new FromStringTerm(accountEntity.getAccount()),//"33018953@qq.com"),  
		                new SubjectTerm(searchSubject));//"test"));  
		          
//		      // ����ʼ���Folder�ڵ������ʼ�Message����  
//		      Message [] messages = folder.getMessages();  
		          
		        // ��������������ֱ�ӷ��������ʼ�������ʹ��Folder.search()����  
		        Message [] messages = folder.search(st);  
		        int mailCounts = messages.length;  
//		        System.out.println("�������˵�" + mailCounts + " ������������ʼ���");  
		        
		        for(int i = 0; i < mailCounts; i++) {  
		            String subject = messages[i].getSubject();  
		            String from = (messages[i].getFrom()[0]).toString();  
		              
//		            System.out.println("�� " + (i+1) + "���ʼ������⣺" + subject);  
//		            System.out.println("�� " + (i+1) + "���ʼ��ķ����˵�ַ��" + from);  
		              
//		            System.out.println("�Ƿ�ɾ�����ʼ�(yes/no)?��");  
//		            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  
//		            String input = br.readLine();  
//		            if("yes".equalsIgnoreCase(input)) {  
		                // ֱ�����������̨��  
//		              messages[i].writeTo(System.out);  
		                // ����ɾ����ǣ�һ��Ҫ�ǵõ���saveChanges()���� 
		            if(from.equals(accountEntity.getAccount())&&searchSubject.equals(subject)){
		            	msg+="true-"+accountEntity.getAccount()+"-"+accountEntity.getPassword()+"-�˺�,���Ͳ����ųɹ�\n"; 
		            	
		            }else{
		            	//msg+="false-�����ʼ�:�˺� "+searchFromEmail+" ���Ͳ�����ʧ��\n"; 
		            }
		            messages[i].setFlag(Flags.Flag.DELETED, true);
	            	messages[i].saveChanges(); 
//		            }             
		        }  
		        // �ر�����ʱ������ɾ����ǵ��ʼ��Żᱻ����ɾ�����൱��"QUIT"����  
		        
		        folder.close(false);  
		        store.close(); 
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				return msg;
			}

	        //return msg;
	 }
	 
//	public static void receive(String server, int port, String username, String password, boolean isDelete)throws MessagingException {
//		Properties props =System.getProperties();
//		PopupAuthenticator auth =new PopupAuthenticator();
//		auth.performCheck(username,password);
//		
//		Session session = Session.getInstance(props,auth);
//		session.setDebug(false);
//		Store store = session.getStore("POP3");
//		store.connect(server,port,username,password);
//		//����ռ���
//		POP3Folder folder =(POP3Folder)store.getFolder("INBOX");
//		try{
//		//��д��ʽ��
//			folder.open(Folder.READ_WRITE);
//		} catch(MessagingException ex) {
//		//�ƶȷ�ʽ��
//			folder.open(Folder.READ_ONLY);
//		}
//		// int totalMessages = folder.getMessageCount������
//		Message m_message = null;
//		Message[] msgs = folder.getMessages();
//		for(int i = 0; i < msgs.length; i++){
//			m_message = msgs[i];
//			String UID = folder.getUID(m_message);
//			
//			
//			m_message.setFlag(Flags.Flag.SEEN, true);
//			m_message.setFlag(Flags.Flag.DELETED, isDelete);
//	//		if(haveReceived(UID){
//	//			// �������ݿ�
//	//			// mailList.add��new RecvMailTask��m_message,
//	//			// p_st_attachmentParentDir, UID������
//	//			// ����Ϊ�Ѷ���IMAPЭ��֧�֣�POP3Э�鲻֧�ָù���
//	//			m_message.setFlag(Flags.Flag.SEEN, true);
//	//			// POP3Э�����ɾ��
//	//			m_message.setFlag(Flags.Flag.DELETED, isDelete);
//	//		����}
//		 }
//	}

}
