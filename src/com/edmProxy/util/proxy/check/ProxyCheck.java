package com.edmProxy.util.proxy.check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.util.WriteFile;
import com.edmProxy.util.proxy.check.ProxyCheckUtil;
import com.edmProxy.entity.TestEntiy;

public class ProxyCheck {	
	//����ʵ�壬����������ַ����֤��ʱ�䣬���ر���������ַ����ַ
	public String[] start(ProxyEntity proxyEntiy,String outNetAddress,String checkTime,String getProxyHost,String newFile[]) throws IOException{
		String[] arry =new String[2];
		ProxyCheckUtil proxyCheckUtil = new ProxyCheckUtil();
		String getHtml = proxyCheckUtil.check(proxyEntiy, checkTime,getProxyHost);
		boolean flag=ProxyCheck.checkHtml(getHtml,outNetAddress);
		String msg="";
		String proxy=proxyEntiy.getProxyType()+"-"+proxyEntiy.getProxyHost()+"-"+proxyEntiy.getProxyPort()+"-"+proxyEntiy.getProxyAccount()+"-"+proxyEntiy.getProxyPassword();
		if(newFile!=null){//�������д���ļ�
			if(flag){
				
				msg=proxyEntiy.getProxyHost()+"--���������Ч��";
				//��Ч��¼
				if(!"".equals(newFile[0])){//������⣬д���ļ�
					
					WriteFile.getDataWriteFile(newFile[0],proxy);
				}
			}else{
				//WriteFile.getDataWriteFile(newFile[1],proxy);
				//msg=proxyEntiy.getProxyHost()+"--���������Ч��";
				//��Ч��¼
			}
		}
//		else{//���������ʾ
//			if(flag){
//				msg=proxyEntiy.getProxyHost()+"--���������Ч��";
//			}else{
//				msg=proxyEntiy.getProxyHost()+"--���������Ч��";
//			}
//		}
		arry[0]=flag+"";
		arry[1]=getHtml+"\n\n"+msg+"\n----------------------------";
		removeLocalProxy();
		return arry;
	}
	
	public static boolean checkHtml(String getHtml,String localhost){
		//localhost="58.20.53.243";
		if(getHtml==null){
			return false;
		}else if(getHtml.indexOf(localhost)>-1){
			return false; //��Ч����IP
		}
		return true;
	}
	
	
	// ���proxy����   
	public void removeLocalProxy()   
	{   
		Properties prop = System.getProperties();   
		prop.remove("http.proxyHost");   
		prop.remove("http.proxyPort");   

		prop.remove("socksProxyHost");   
		prop.remove("socksProxyPort");   
	}   

	public static void main(String[] args) {
		TestEntiy testEntiy = new TestEntiy();
		testEntiy.create();

		ArrayList<ProxyEntity>  list = new ArrayList<ProxyEntity>();
		list = testEntiy.list;
		ProxyCheck proxyCheck = new ProxyCheck();
		for (int i = 0; i < list.size(); i++) {
			//proxyCheck.start(list.get(i),"58.20.53.243","10","http://iframe.ip138.com/ic.asp");
		}
	}
}
