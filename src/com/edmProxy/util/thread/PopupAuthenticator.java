package com.edmProxy.util.thread;

import javax.mail.*;
import javax.mail.internet.*;

/*
 * ��֤�����û���������
 */
public class PopupAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public PopupAuthenticator() {
	}

	public PasswordAuthentication performCheck(String userName, String password) {
		userName = userName;
		password = userName;
		return getPasswordAuthentication();
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}