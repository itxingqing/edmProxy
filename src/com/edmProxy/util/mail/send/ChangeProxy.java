package com.edmProxy.util.mail.send;

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.util.proxy.check.ProxyCheckUtil;

public class ChangeProxy {
	private static ProxyDAO proxyDAO;
	private static int count;
	private static ProxyEntity proxyEntityTemp;
	public static ProxyEntity changeProxy(ProxyEntity proxyEntity,String getProxyHost,String localHost){
		String getHtml=ProxyCheckUtil.check(proxyEntity, "5",getProxyHost);
		
		//System.out.println("-->"+getHtml);
	
		if(getHtml==null){
			//if(getHtml.indexOf(localHost)>-1){
				System.out.println("�����ɹ�");
				//�޸Ĵ���ʧЧ״̬
				proxyDAO=new ProxyDAO();
				count=proxyDAO.updateValidByProxyHost(proxyEntity.getProxyHost(), 0);
				//ͨ����ѯ�µĴ���ͨ����Ч��ѯ
				proxyEntityTemp=new ProxyEntity();
				proxyEntityTemp=proxyDAO.findFistByValid(1);
			//}
			System.out.println(proxyEntityTemp.getProxyHost());
		}else{
			System.out.println("����ɹ�");
		}
		return proxyEntityTemp;
	}
	
	public static void main(String[] args) {
		ProxyEntity proxyEntity=new ProxyEntity();
		
		proxyEntity.setProxyHost("173.234.50.210");
		proxyEntity.setProxyPort("1080");
		proxyEntity.setProxyAccount("user001");
		proxyEntity.setProxyPassword("123456");
		
		changeProxy(proxyEntity,"http://iframe.ip138.com/ic.asp","58.20.53.243");
	}
}
