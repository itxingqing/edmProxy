package com.edmProxy.gui.panel.receive;

import java.util.ArrayList;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;

//��ҳ ÿҳ20������
public class ReceivePageing {
	private ReceiveDAO receiveDAO;
	//������ҳ�� pageSize��ҳ���С
	public Integer findTotalPageByCondition(Integer pageSize,String condition){
		System.out.println(condition);
		Integer totalRow=getTotalRowByCondition(condition); //������
		Integer pages=0; //��ҳ��
		//��ģ���㣬�����ҳ���������������1
		if(totalRow!=0){
			if(totalRow%pageSize==0){
				pages=totalRow/pageSize;
			}else{
				pages=totalRow/pageSize+1;
			}
		}
		return pages;
	}
	
	//��ǰҳ��������,���룺page����ǰҳ��pageSize��ҳ���С
	public Integer findPageTotalRow(Integer pageSize,Integer page){
		Integer pageTotalRow=(page-1)*pageSize;
		return pageTotalRow;
	}
	//�õ�������
	public Integer getTotalRowByCondition(String condition){
		receiveDAO=new ReceiveDAO();
		Integer totalRow=receiveDAO.queryCountByCondition(condition); //������
		return totalRow;
	}	
	
	//��ҳ ����pageSize:ҳ���С��pageNumber����ǰҳ��
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition){
		receiveDAO=new ReceiveDAO();
		return receiveDAO.pageingBypageNowAndpageSizeByCondition(pageNow, pageSize,condition);
	}
	
	public static void main(String[] args) {
		ArrayList<ReceiveEntity> list=new ArrayList<ReceiveEntity>();
		ReceivePageing p=new ReceivePageing();
		list=p.pageingBypageNowAndpageSizeByCondition(2, 20,"");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
