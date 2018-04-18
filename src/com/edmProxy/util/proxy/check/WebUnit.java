package com.edmProxy.util.proxy.check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;

public class WebUnit {
	private static final Logger log = Logger.getLogger(WebUnit.class);

	/**
	 * ����һ����վ��Դ����,��Ҫ����һ����վ����ַ
	 * 
	 * @param webUrl
	 * @return
	 */
	public static String getWebContent(String webUrl) {
		URL url;
		HttpURLConnection conn;
		InputStream stream;
		StringBuffer text = new StringBuffer();
		String temp = "";
		try {
			url = new URL(webUrl);

			// �����Ҫ���������
			if (true) {
				// ����socket5����
				System.getProperties().setProperty("checkTimemaxRedirects", "1");
				System.getProperties().setProperty("socksProxySet", "true");
				System.getProperties().setProperty("socksProxyHost","107.149.216.10");
				System.getProperties().setProperty("socksProxyPort","1080");
				// ���õ�½��������������û���������
				Authenticator.setDefault(new MyAuthenticator("User-001",
						"123456"));
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			conn.setInstanceFollowRedirects(false);// ����ת
			for (int i = 0; i < 50; i++) {
				System.out.println(conn.getHeaderField(i));
			}

			// System.out.println(conn.getContent());
			stream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			while ((temp = reader.readLine()) != null) {
				text.append(temp + "/n");
			}
		} catch (MalformedURLException e) {
			log.error("����" + webUrl + "����!!!");
		} catch (IOException e) {
			log.error(e);
		}
		removeLocalProxy();
		return text.toString();
	}

	// ���proxy����
	public static void removeLocalProxy() {
		Properties prop = System.getProperties();
		
		prop.remove("socksProxyHost");
		prop.remove("socksProxyPort");
	}

	public static void main(String[] args) {
		System.out.println(WebUnit.getWebContent("http://iframe.ip138.com/ic.asp"));
	}

	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {

			// System.out.println("--"+getPasswordAuthentication());

			return new PasswordAuthentication(user, password.toCharArray());
		}
	}
}