package com.edmProxy.util.mail.send;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.SendTaskDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskEntity;
import com.edmProxy.gui.panel.account.AccountPageing;
import com.edmProxy.util.GetHtmlUtil;
import com.edmProxy.util.ReadFile;
import com.edmProxy.util.ConsoleToUI.ConsoleTextArea;



//�����̣߳�����list�����߳�
public class SendThread {
	public static void main(String[] args) throws IOException {	
		ArrayList<SendTaskEntity> sendTaskList=new ArrayList();
//		ReceiveGroup rg=new ReceiveGroup();
//		ArrayList<ArrayList> receiveGroupList=new ArrayList();
//		
//		AccountDAO accountDAO=new AccountDAO();
//		
//		int accountSum=accountDAO.queryCount();
//		
//
//		
//	    ArrayList<String> receiveList=new ArrayList();
//	   
//	    receiveList=ReadFile.ReadTxt("F:\\test\\Test2-20000.txt");
//	    
//	    receiveGroupList=rg.receiveGroup(receiveList,10,0);
	    
	    //test
//	    for (int i = 0; i < receiveGroupList.size(); i++) {
//	    	System.out.println("��"+i+"���˺ŷ���-------");
//			ArrayList<String> list=new ArrayList();
//			list=receiveGroupList.get(i);
//			for (int j = 0; j < list.size(); j++) {
//				System.out.println("��"+i+"���˺ŷ���-------��"+j+"���ʼ�--"+list.get(j));
//			}
//		}
	    
	    
////		
		SendThread sendThread=new SendThread();
		SendTaskDAO sendTaskDAO=new SendTaskDAO(); 
		sendTaskList=sendTaskDAO.pageingBypageNowAndpageSize(1,1);//1����������
//		sendThread.startThread(sendTaskList);//20�˺�������
	}
	
	
	
	
	
	
	private AccountPageing accountPageing;
	private ArrayList<AccountEntity> accountList;
	private StartTask startTask;
	private Runnable runnable;
	private Thread thread;
	private ReceiveGroup receiveGroup;
	private ArrayList<ArrayList> receiveGroupList;
	private ArrayList<String> receiveList;
	private String receivePath;
	private int accountSendNum;
	private String msg;

	
	public void startThread(ArrayList<SendTaskEntity> sendTaskList,ProxyEntity proxyEntity,String getProxyHost,String localHost,ConsoleTextArea consoleTextArea) throws IOException{
		//,ArrayList<ArrayList> receiveGroupList){
		accountPageing=new AccountPageing();
		//�������������˺���������		
		for (int i = 0; i < sendTaskList.size(); i++) {
			receiveGroup=new ReceiveGroup();
			accountList=new ArrayList();
			
			receiveGroupList=new ArrayList();
			receiveList=new ArrayList();
			
			//��ȡ�ռ�����Ϊlist
			receivePath=sendTaskList.get(i).getSendTaskReceivesPath();
			accountSendNum=sendTaskList.get(i).getAccountSendNum();
			if(!"".equals(receivePath)&&accountSendNum>0){
				receiveList=ReadFile.ReadTxt(receivePath);
				//�ռ������  10�����յ�ַһ�飬Ҳ���Ƿ���10���ʼ���һ���˺� accountSendNum
				receiveGroupList=receiveGroup.receiveGroup(receiveList,accountSendNum);
				accountList=accountPageing.pageingBypageNowAndpageSize(i+1,receiveGroupList.size());	
				startTask=new StartTask(sendTaskList.get(i),accountList,receiveGroupList,proxyEntity,getProxyHost,localHost,consoleTextArea);
				runnable=new TaskThread(startTask,accountList);
				thread=new Thread(runnable);
				thread.start();
			}
			msg+="��"+i+"��"+sendTaskList.get(i)+"����\n";
		}
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
	private ProxyEntity proxyEntity;
	private ProxyEntity proxyEntityTemp;
	private String getProxyHost; 
	private String localHost;
	private ConsoleTextArea consoleTextArea;
	public StartTask(SendTaskEntity sendTaskEntity,ArrayList<AccountEntity> accountList,ArrayList<ArrayList> receiveGroupList,ProxyEntity proxyEntity,String getProxyHost,String localHost,ConsoleTextArea consoleTextArea){
		this.sendTaskEntity=sendTaskEntity;
		this.accountList=accountList;
		this.receiveGroupList=receiveGroupList;
		this.proxyEntity=proxyEntity;
		this.getProxyHost=getProxyHost;
		this.localHost=localHost;
		this.consoleTextArea=consoleTextArea;
	}
	public synchronized void startTask(){
		//System.out.println(accountList.size()+":"+receiveGroupList.size());
		for (int i = 0; i < accountList.size(); i++) {
			
			//�л����� �������ʧЧ���л������򷵻�ԭ����,ÿ��һ������
			if(sendTaskEntity.getProxyStart()==1){
				proxyEntityTemp=proxyChange(proxyEntity);
			}
			StartAccount startAccount=new StartAccount(sendTaskEntity,accountList.get(i),receiveGroupList.get(i),proxyEntity,consoleTextArea);
			Runnable r=new AccountThread(startAccount);
			Thread t=new Thread(r);
			t.start();
		}
	}
	//�л����� �������ʧЧ���л������򷵻�ԭ����
	public ProxyEntity proxyChange(ProxyEntity proxyEntity){
		proxyEntityTemp=new ProxyEntity();	
		proxyEntityTemp=ChangeProxy.changeProxy(proxyEntity, getProxyHost, localHost); 
		if(proxyEntityTemp!=null){
			proxyEntity=proxyEntityTemp;
		}
		System.out.println("�л�����---->"+proxyEntity.getProxyHost());
		return proxyEntity;
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
	private String contentStr;
	private int accessoryState=0;
	private ArrayList<String> accessoryList;
	private String accessoryPathArray[];
	private ProxyEntity proxyEntity;
	private AccountDAO accountDAO;
	private ProxyDAO proxyDAO;
	private ConsoleTextArea consoleTextArea;
	
	private String msg;
	
	public StartAccount(SendTaskEntity sendTaskEntity,
			AccountEntity accountEntity,ArrayList<String> list,ProxyEntity proxyEntity,ConsoleTextArea consoleTextArea){
		this.sendTaskEntity=sendTaskEntity;
		this.accountEntity=accountEntity;
		this.list=list;
		this.proxyEntity=proxyEntity;
		this.consoleTextArea=consoleTextArea;
	}
	public synchronized String startAccount(){
		sendMail=new SendMail();
		for (int i = 0; i < list.size(); i++) {
			
			//System.out.println(sendTaskEntity.getSendTask()+"-"+accountEntity.getAccount()+"�˺ŷ���"+list.get(i));
			ArrayList<String> listTemp=new ArrayList<String>();
			listTemp.add(list.get(i));
			//��ȡ����
			contentStr=MailUtil.readTemplateMail(sendTaskEntity.getContentPath());
			//���������Ϊ�գ���ȡ��list
			if(!"".equals(sendTaskEntity.getAccessoryPath())){
				accessoryState=1;
				accessoryList=new ArrayList<String>();
				accessoryPathArray=sendTaskEntity.getAccessoryPath().split(",");
				for (int j = 0; j < accessoryPathArray.length; j++) {
					accessoryList.add(accessoryPathArray[j]);
				}
				
			}
			msg=">---->"+sendTaskEntity.getSendTask()+"���˺ţ�"+ accountEntity.getAccount()+" ����"+list.get(i)+" �ռ��䣬�����ʼ��ɹ���\n";
			System.out.println(msg);
			try { 
				sendMail.sendMailTrue(accountEntity,listTemp,accessoryList,accountEntity.getAccount(),contentStr,sendTaskEntity.getProxyStart(),proxyEntity,accessoryState);
				Thread.sleep(sendTaskEntity.getSendIntervalTime()*1000);
				consoleTextArea.startWriterTestThread(sendTaskEntity.getSendTask(), System.out, sendTaskEntity.getSendIntervalTime()*1000, i);
				
			} 
			catch (UnsupportedEncodingException e) {
				proxyDAO.updateValidByProxyHost(proxyEntity.getProxyAccount(), 0);
				System.err.println(">---->����:"+proxyEntity.getProxyAccount()+"���Ӳ����쳣�����ʧЧ������ЧΪʧЧ");
				e.printStackTrace();
			} catch (MessagingException e) {
				accountDAO=new AccountDAO();
				accountDAO.updateValidByAccount(accountEntity.getAccount(), 0);
				System.err.println(">---->�����˺�:"+accountEntity.getAccount()+"���Ӳ��Ϸ����쳣���˺�ʧЧ������ЧΪʧЧ");
				
				e.printStackTrace();
			} 
			catch (InterruptedException e) {
				System.err.println("�����쳣----3");
				e.printStackTrace();
			}
		}
		
		
		
	
		
		
		
		return msg;
	}
}
