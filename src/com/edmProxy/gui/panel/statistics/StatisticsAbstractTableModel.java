package com.edmProxy.gui.panel.statistics;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskStatisticsObj;
import com.edmProxy.entity.StatisticsEntity;

public class StatisticsAbstractTableModel extends AbstractTableModel implements ListSelectionListener{

	// �����ͷ����
	String[] head = {"���" ,"��������","�򿪴���",
			"�������","����ʱ��","ѡ��"};
	Class[] typeArray = {Integer.class,Object.class,Object.class,
			Object.class,Object.class,Boolean.class};

	
	// ������ÿһ�е���������
	private Object[][] data;
	private ArrayList<SendTaskStatisticsObj> list;
	
	public StatisticsAbstractTableModel() {
		super();
	}

	public StatisticsAbstractTableModel( 
			ArrayList<SendTaskStatisticsObj> list) {
		super();
		this.list=list;
		
	}
	
	public void dataToTable(){
		this.data=new Object[list.size()][7];
		for (int i = 0; i < list.size(); i++) {
			data[i][0]=list.get(i).getStatisticsEntity().getId()+"";
			data[i][1]=list.get(i).getSendTaskEntity().getSendTask();
			data[i][2]=list.get(i).getStatisticsEntity().getOpenCount();
			data[i][3]=list.get(i).getStatisticsEntity().getClickeCount();
			data[i][4]=list.get(i).getStatisticsEntity().getCreateDate();
			data[i][5]=new Boolean(false);
		}
	}

   
	// ��ñ�������
	public int getColumnCount() {
		return head.length;
	}

	// ��ñ�������
	public int getRowCount() {
		return data.length;
	}

	// ��ñ���������
	@Override
	public String getColumnName(int column) {
		return head[column];
	}

	// ��ñ��ĵ�Ԫ�������
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	// ʹ�����пɱ༭��,ʹJTable�е�JButton�����¼��Ĺؼ�����
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		    boolean b=false;
		    if(columnIndex==5){
		    	b=true;
		    }
		return b;
	}

	// �滻��Ԫ���ֵ
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	// ʵ���������boolean�Զ�ת��JCheckbox
	/*
	 * ��Ҫ�Լ���celleditor��ô�鷳�ɡ�jtable�Զ�֧��Jcheckbox��
	 * ֻҪ����tablemodel��getColumnClass����һ��boolean��class�� jtable���Զ���һ��Jcheckbox���㣬
	 * ���value��true����falseֱ�Ӷ�table���Ǹ�cell��ֵ�Ϳ���
	 */
	public Class getColumnClass(int columnIndex) {
		return typeArray[columnIndex];// ����ÿһ�е���������
	}

	public void valueChanged(ListSelectionEvent e) {
//		System.out.println("--");
//		System.out.println(e.getSource());
//		System.out.println(e.getValueIsAdjusting());
//		System.out.println(e.getFirstIndex());
		
	}

}
