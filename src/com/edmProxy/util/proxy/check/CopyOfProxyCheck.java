package com.edmProxy.util.proxy.check;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import com.edmProxy.entity.*;
import com.edmProxy.util.proxy.*;



public class CopyOfProxyCheck {
	private int count=0;
	private ArrayList<ProxyEntity> list;
	private ExecutorService threadPool;     //�̳߳�
	private BlockingQueue<String> queue;    //��������
	
	
	//Boolean flag=true;
	
	public synchronized void incCount(){   //�����ж���̷߳�����������Ҫͬ����
		count++;
	}
	public synchronized void decCount(){
		count--;
	}
	public synchronized int getCount(){
		return count;
	}
	
	
	public void start() throws Exception{
		threadPool=Executors.newFixedThreadPool(10);   //�̳߳ش���100���߳�  
		queue=new LinkedBlockingDeque<String>(20);   //����10000����������
		
		TestEntiy testEntiy=new TestEntiy();
		
		testEntiy.create();
		
		list=new ArrayList<ProxyEntity>();
		list=testEntiy.list;
		int i;
		
		for (i = 0; i < list.size(); i++) {
			System.out.println(i);
			ProxyCheckUtil proxyCheckUtil=new ProxyCheckUtil();
			//while(flag){//��Զ�ȴ� ���յ�һ�����ӣ�������һ���̣߳��ټ����ȴ���
				Handler handler=new Handler(list.get(i),proxyCheckUtil);
				//handler.start();
				threadPool.execute(handler);//���̳߳������ó�һ���̣߳�����run����
				incCount();
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
		}
		
		
	}
	
	
	//�߳���-����
	class Handler implements Runnable{      //�ýӿ� Handler��һ����ͨ���࣬������ʵ����run����
		private ProxyEntity proxyEntiy;
        private ProxyCheckUtil proxyCheckUtil;
		Handler(ProxyEntity proxyEntiy,ProxyCheckUtil proxyCheckUtil){
			this.proxyEntiy=proxyEntiy;
        	this.proxyCheckUtil=proxyCheckUtil;
		}
		public void run(){
			while(true){
				System.out.println(getCount()+":"+list.size()+":"+(getCount()==list.size()));
				if(getCount()==list.size())
					break;
				System.out.println("-->"+getCount());
				System.out.println("����:-"+proxyEntiy.getProxyHost()+"-��֤��ʼ");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	proxyCheckUtil.check(proxyEntiy, "10", "http://iframe.ip138.com/ic.asp");
			}	
		}
	}
	
	
	
	public static void main(String[] args) {
		CopyOfProxyCheck proxyCheck=new CopyOfProxyCheck();
		try {
			proxyCheck.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
