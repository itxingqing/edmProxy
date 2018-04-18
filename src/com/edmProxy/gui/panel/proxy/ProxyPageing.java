package com.edmProxy.gui.panel.proxy;

import java.util.ArrayList;

import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.ProxyEntity;

//��ҳ ÿҳ20������
public class ProxyPageing {
	private ProxyDAO proxyServerDAO;
	//������ҳ�� pageSize��ҳ���С
	public Integer findTotalPage(Integer pageSize){
		proxyServerDAO=new ProxyDAO();
		Integer totalRow=getTotalRow(); //������
		Integer pages=0; //��ҳ��
		//��ģ���㣬�����ҳ���������������1
		if(totalRow%pageSize==0){
			pages=totalRow/pageSize;
		}else{
			pages=totalRow/pageSize+1;
		}
		return pages;
	}
	
	//��ǰҳ��������,���룺page����ǰҳ��pageSize��ҳ���С
	public Integer findPageTotalRow(Integer pageSize,Integer page){
		Integer pageTotalRow=(page-1)*pageSize;
		return pageTotalRow;
	}
	//�õ�������
	public Integer getTotalRow(){
		Integer totalRow=proxyServerDAO.queryCount(); //������
		return totalRow;
	}	
	
	//��ҳ ����pageSize:ҳ���С��pageNumber����ǰҳ��  
	public ArrayList<ProxyEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize){
		proxyServerDAO=new ProxyDAO();
		return proxyServerDAO.pageingBypageNowAndpageSize(pageNow, pageSize);
	}
	
	public static void main(String[] args) {
		ArrayList<ProxyEntity> list=new ArrayList<ProxyEntity>();
		ProxyPageing p=new ProxyPageing();
		list=p.pageingBypageNowAndpageSize(2, 20);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
