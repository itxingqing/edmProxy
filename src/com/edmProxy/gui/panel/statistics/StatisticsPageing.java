package com.edmProxy.gui.panel.statistics;

import java.util.ArrayList;

import com.edmProxy.dao.AccountDAO;
import com.edmProxy.dao.ProxyDAO;
import com.edmProxy.dao.ReceiveDAO;
import com.edmProxy.dao.StatisticsDAO;
import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskStatisticsObj;
import com.edmProxy.entity.StatisticsEntity;

//��ҳ ÿҳ20������
public class StatisticsPageing {
	private StatisticsDAO statisticsDAO;
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
		statisticsDAO=new StatisticsDAO();
		Integer totalRow=statisticsDAO.queryCountByCondition(condition); //������
		return totalRow;
	}	
	
	//��ҳ ����pageSize:ҳ���С��pageNumber����ǰҳ��
	public ArrayList<StatisticsEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition){
		statisticsDAO=new StatisticsDAO();
		return statisticsDAO.pageingBypageNowAndpageSizeByCondition(pageNow, pageSize,condition);
	}
	
	
	//��ҳ ����pageSize:ҳ���С��pageNumber����ǰҳ�� �õ�ͳ�ƺͷ�������ʵ�����װ��SendTaskStatisticsObj
	public ArrayList<SendTaskStatisticsObj> pageingBypageNowAndpageSizeObjByCondition(int pageNow,int pageSize,String condition){
		statisticsDAO=new StatisticsDAO();
		return statisticsDAO.pageingBypageNowAndpageSizeObjByCondition(pageNow, pageSize,condition);
	}
	
	public static void main(String[] args) {
		ArrayList<StatisticsEntity> list=new ArrayList<StatisticsEntity>();
		StatisticsPageing p=new StatisticsPageing();
		list=p.pageingBypageNowAndpageSizeByCondition(2, 20,"");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getId());
		}
		
		
	}
}
