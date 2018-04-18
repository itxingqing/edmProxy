package com.edmProxy.gui.panel.account;

import java.util.ArrayList;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;

//��ҳ ÿҳ20������
public class AccountPageing {
	private AccountDAO accountDAO;
	//������ҳ�� pageSize��ҳ���С
	public Integer findTotalPage(Integer pageSize){
		accountDAO=new AccountDAO();
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
		Integer totalRow=accountDAO.queryCount(); //������
		return totalRow;
	}	
	
	//��ҳ ����pageSize:ҳ���С��pageNumber����ǰҳ��
	public ArrayList<AccountEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize){
		accountDAO=new AccountDAO();
		return accountDAO.pageingBypageNowAndpageSize(pageNow, pageSize);
	}
	
	public static void main(String[] args) {
		ArrayList<AccountEntity> list=new ArrayList<AccountEntity>();
		AccountPageing p=new AccountPageing();
		list=p.pageingBypageNowAndpageSize(2, 20);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
