package com.edmProxy.util.mail.send;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.SendTaskDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskEntity;
import com.edmProxy.gui.panel.account.AccountPageing;
import com.edmProxy.util.ReadFile;



//�����̣߳�����list�����߳�
public class CopyOfSendThread {
	public static void main(String[] args) throws IOException {	
		ArrayList<SendTaskEntity> sendTaskList=new ArrayList();
		ReceiveGroup rg=new ReceiveGroup();
		ArrayList<ArrayList> receiveGroupList=new ArrayList();
		
		AccountDAO accountDAO=new AccountDAO();
		
		int accountSum=accountDAO.queryCount();
		

		
	    ArrayList<String> receiveList=new ArrayList();
	   
	    receiveList=ReadFile.ReadTxt("F:\\Test2-20000.txt");
	    
	    receiveGroupList=rg.receiveGroup(receiveList,10,0);
	    
	    //test
//	    for (int i = 0; i < receiveGroupList.size(); i++) {
//	    	System.out.println("��"+i+"���˺ŷ���-------");
//			ArrayList<String> list=new ArrayList();
//			list=receiveGroupList.get(i);
//			for (int j = 0; j < list.size(); j++) {
//				System.out.println("��"+i+"���˺ŷ���-------��"+j+"���ʼ�--"+list.get(j));
//			}
//		}
	    
	    
		
		CopyOfSendThread sendThread=new CopyOfSendThread();
		SendTaskDAO sendTaskDAO=new SendTaskDAO(); 
		sendTaskList=sendTaskDAO.pageingBypageNowAndpageSize(1,1);//1����������
		sendThread.startThread(sendTaskList,receiveGroupList.size(),receiveGroupList);//20�˺�������
	}
	
	
	
	
	
	
	private AccountPageing accountPageing;
	private ArrayList<AccountEntity> accountList;
	private StartTask startTask;
	private Runnable runnable;
	private Thread thread;
	
	public void startThread(ArrayList<SendTaskEntity> sendTaskList,int accountStartLinks,ArrayList<ArrayList> receiveGroupList){
		accountPageing=new AccountPageing();
		//�������������˺���������
//		for (int i = 0; i < sendTaskList.size(); i++) {
//			accountList=new ArrayList();
//			accountList=accountPageing.pageingBypageNowAndpageSize(i+1,accountStartLinks);
////			System.out.println("---->"+accountList.get(i).getAccount());
//			
//			startTask=new StartTask(sendTaskList.get(i),accountList);
//			runnable=new TaskThread(startTask,accountList);
//			thread=new Thread(runnable);
//			thread.start();
//		}
		accountList=new ArrayList();
		accountList=accountPageing.pageingBypageNowAndpageSize(1,accountStartLinks);
//		System.out.println("---->"+accountList.get(i).getAccount());
		
		startTask=new StartTask(sendTaskList.get(0),accountList,receiveGroupList);
		runnable=new TaskThread(startTask,accountList);
		thread=new Thread(runnable);
		thread.start();
		
		
	}
}

//�����߳�
class TaskThread implements Runnable{
	private StartTask startTask;
	private ArrayList<AccountEntity> accountList;
	public TaskThread(StartTask startTask,ArrayList<AccountEntity> accountList){
		this.startTask=startTask;
		this.accountList=accountList;
	}
	public void run(){
		startTask.startTask();
	}
}
//�����߳̾��巽��
class StartTask{
	private SendTaskEntity sendTaskEntity;
	private ArrayList<AccountEntity> accountList;
	private ArrayList<ArrayList> receiveGroupList;
	public StartTask(SendTaskEntity sendTaskEntity,ArrayList<AccountEntity> accountList,ArrayList<ArrayList> receiveGroupList){
		this.sendTaskEntity=sendTaskEntity;
		this.accountList=accountList;
		this.receiveGroupList=receiveGroupList;
	}
	public synchronized void startTask(){
		System.out.println(accountList.size()+":"+receiveGroupList.size());
		for (int i = 0; i < accountList.size(); i++) {			StartAccount startAccount=new StartAccount(sendTaskEntity,accountList.get(i),receiveGroupList.get(i));
			Runnable r=new AccountThread(startAccount);
			Thread t=new Thread(r);
			t.start();
		}
	}
}
//�˺��߳�
class AccountThread implements Runnable{
	private StartAccount startAccount;
	public AccountThread(StartAccount startAccount){
		this.startAccount=startAccount;
	}
	public void run(){
		startAccount.startAccount();
	}
}

//�˺��߳̾��巽��
class StartAccount{
	private SendTaskEntity sendTaskEntity;
	private AccountEntity accountEntity;
	private ArrayList<String> list;
	private SendMail sendMail;
	public StartAccount(SendTaskEntity sendTaskEntity,
			AccountEntity accountEntity,ArrayList<String> list){
		this.sendTaskEntity=sendTaskEntity;
		this.accountEntity=accountEntity;
		this.list=list;
	}
	public synchronized void startAccount(){
		sendMail=new SendMail();
		for (int i = 0; i < list.size(); i++) {
//			System.out.println(sendTaskEntity.getSendTask()+
//					"--��"+accountEntity.getAccount()+
//					"�˺�,�����ʼ�"+list.get(i));
			System.out.println(accountEntity.getAccount()+"�˺ŷ���"+list.get(i));
			System.out.println("----->"+list.get(i));
			
			ArrayList<String> listTemp=new ArrayList<String>();
			listTemp.add(list.get(i));
			try {
				//sendMail.sendMailTrue(accountEntity,listTemp,null,accountEntity.getAccount(),"����",0);
				Thread.sleep(5000);
			} 
//			catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MessagingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	
	}
}
