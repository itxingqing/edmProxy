package com.edmProxy.test;

import java.util.ArrayList;

public class MyStar {
	public static void main(String[] args) {
		//SendMail sendMail=new SendMail();
		TestSendMailEntiy tsme=new TestSendMailEntiy();
		tsme.create();
		ArrayList<SendEmailEntiy> sendMailEntiyList=new ArrayList();
		sendMailEntiyList=tsme.sendEmailEntiyList;
		
		
		ArrayList<String> receiveMailInfoList=new ArrayList();
		for (int i = 0; i < 1; i++) {
			receiveMailInfoList.add("lookskystaremail@126.com");
		}
		
//		SendMail sendMail=new SendMail();
//		SendEmailEntiy sendEmailEntiy=new SendEmailEntiy();
//		Runnable r=new MyThread(sendMail,sendMailEntiyList.get(1),receiveMailInfoList);
//		Thread t=new Thread(r);
//		t.start();
		
		SendMail sendMail=new SendMail();
		for (int i = 0; i < 1; i++) {
			Runnable r=new MyThread(sendMail,sendMailEntiyList.get(i),receiveMailInfoList);
			Thread t=new Thread(r);
			t.start();
		}
		
		
//		Send send1=new Send("�˺�2"+"��ʼ����....");
//		Runnable r1=new MyThread(send1);
//		Thread t1=new Thread(r1);
//		
//		t.start();
//		t1.start();
	}	
}
//�߳���
class MyThread implements Runnable{
	private SendMail sendMail;
	private SendEmailEntiy sendEmailEntiy;
	private ArrayList<String> receiveMailInfoList;
	public MyThread(SendMail sendMail,SendEmailEntiy sendEmailEntiy,ArrayList<String> receiveMailInfoList){
		this.sendMail=sendMail;
		this.sendEmailEntiy=sendEmailEntiy;
		this.receiveMailInfoList=receiveMailInfoList;
	}
	int i;
	public void run() {
		
		//���þ�����෽��
		sendMail.SendMailTrue(sendEmailEntiy, receiveMailInfoList);
		
		System.out.println("��ʼ�̷߳���+"+i++);
		try {
			Thread.sleep((long)Math.random()*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

//������෽��
// class  Send{
//	private String name;
//	public Send(String name){
//		this.name=name;
//	}
//	//Ϊ����������
//	public synchronized void starSend(){
//		System.out.println("ͬʱִ��->"+name);
//	}
//}

