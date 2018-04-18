package httpClientMail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class MailTest {
	/**
	 * ����
	 */
	//@Test
	public void testSingleSend() {
		SimpleMail sm = new SimpleMail();
		sm.setSubject("��һ���ʼ�");
		String str = ReadHtmlFile.getSource("http://www.baidu.com");
		// String str = ReadHtmlFile.readFile("������д����Ҫ���͵ı����ļ�·��");
		System.out.println(str);
		sm.setContent(str);
		SimpleMailSender sms = new SimpleMailSender("������д��ķ�����", "������д��ķ���������");
		try {
			sms.send("������Ҫ��д�����ʼ�������", sm);
			System.out.println("ִ����ɣ���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ⱥ��
	 * 
	 */
	//@Test
	public void testMassSend() {
		SimpleMail sm = new SimpleMail();
		sm.setSubject("��һ���ʼ�");
		String str = ReadHtmlFile.getSource("http://www.baidu.com");
		// String str = ReadHtmlFile.readFile("������д��Ҫ���͵ı����ļ�·��");
		System.out.println(str);
		sm.setContent("test");
		SimpleMailSender sms = new SimpleMailSender("lookskystar8@sohu.com", "123456");
		List<String> recipients = new ArrayList<String>();
		recipients.add("lookskystaremail@126.com"); //��ӵ�ַ
		try {
			sms.send(recipients, sm);
			System.out.println("ִ����ɣ���");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		MailTest mailTest=new MailTest();
		mailTest.testMassSend();
	}
}
