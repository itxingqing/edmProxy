package com.edmProxy.util.mail.send;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.gui.panel.SendTaskManagePanel;

public class TimerChange {
	public static void main(String[] args) {
		//timer();
	}
	
	//public static void timer(ChangeProxy changeProxy,ProxyEntity proxyEntity,String getProxyHost,String localHost) {
	public static void timer(SendTaskManagePanel source) {
		Timer timer = new Timer();
		//timer.schedule(new MyTask(changeProxy,proxyEntity,getProxyHost,localHost), 1000, 2000);
		timer.schedule(new MyTask(source), 1000, 2000);
		while (true) {// ���������ֹͣ�������,�����һֱѭ��ִ�д�������
			try {
				int ch = System.in.read();
				if (ch - 'c' == 0) {
					timer.cancel();// ʹ����������˳�����
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class MyTask extends TimerTask {
//	private ChangeProxy changeProxy;
//	private ProxyEntity proxyEntity;
//	private String getProxyHost;
//	private String localHost;
//	private ProxyEntity proxyEntityTemp;
//	public MyTask(ChangeProxy changeProxy,ProxyEntity proxyEntity,String getProxyHost,String localHost){
//		this.changeProxy=changeProxy;
//		this.proxyEntity=proxyEntity;
//		this.getProxyHost=getProxyHost;
//		this.localHost=localHost;
//	}
//	
//	public ProxyEntity getProxyEntityTemp() {
//		return proxyEntityTemp;
//	}
//	public void setProxyEntityTemp(ProxyEntity proxyEntityTemp) {
//		this.proxyEntityTemp = proxyEntityTemp;
//	}

	private SendTaskManagePanel source;
	public MyTask(SendTaskManagePanel source){
		this.source=source;
	}

	@Override
	public void run() {
		
		
		//System.out.println("��ʱ��ʼ________");
		//proxyEntityTemp=changeProxy.changeProxy(proxyEntity, getProxyHost, localHost);
	}
}
