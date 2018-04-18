package com.edmProxy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import com.edmProxy.dao.DBHelp;
/*
 * ��ȡ�����ļ���Ϣ
 */
public class ReadDBPrpperties {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReadDBPrpperties d = new ReadDBPrpperties();
		System.out.println(d.DRIVER);

	}

	private static String DRIVER = ""; // ���ݿ�����
	private static String URL = ""; // ���ݿ��ַ
	private static String USER = ""; // ���ݿ��û���
	private static String PASSWORD = ""; // ���ݿ�����
	public static String getDRIVER() {
		return DRIVER;
	}
	public static void setDRIVER(String driver) {
		DRIVER = driver;
	}
	public static String getURL() {
		return URL;
	}
	public static void setURL(String url) {
		URL = url;
	}
	public static String getUSER() {
		return USER;
	}
	public static void setUSER(String user) {
		USER = user;
	}
	public static String getPASSWORD() {
		return PASSWORD;
	}
	public static void setPASSWORD(String password) {
		PASSWORD = password;
	}




	public ReadDBPrpperties() {
		// ��Ŀ·��
		String pathString = DBHelp.class.getClassLoader().getResource("")
				.toString();
	    //pathString=pathString.substring(6,pathString.length());//���������ȡ�����ļ���Ϣ
		pathString = pathString.substring(6, pathString.length() - 4);// ���Զ�ȡ�����ļ���Ϣ
		pathString = pathString + "resource/db.properties";
		Properties props = new Properties();

		// ����·���¼��������ļ�
		File file = new File(pathString);
		InputStreamReader in;
		try {
			in = new InputStreamReader(new FileInputStream(file), "gbk");
			props.load(in);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DRIVER = props.getProperty("driver");
		URL = props.getProperty("url");
		USER = props.getProperty("user");
		PASSWORD = props.getProperty("password");
	}

}
