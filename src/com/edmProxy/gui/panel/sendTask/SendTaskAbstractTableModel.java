package com.edmProxy.gui.panel.sendTask;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;
import com.edmProxy.entity.SendTaskEntity;

public class SendTaskAbstractTableModel extends AbstractTableModel implements ListSelectionListener{

	// �����ͷ����
	String[] head = {"���" ,"��������","�ʾ�",
			"����","�Ƿ����","����","����","���ʹ���","�������",
			"�˺ŷ�����","�˺�������","�ռ�����","���ͼ��ʱ��","ѡ��"};
	Class[] typeArray = {Integer.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Object.class,Boolean.class};

	
	// ������ÿһ�е���������
	private Object[][] data;
	private ArrayList<SendTaskEntity> list;
	
	public SendTaskAbstractTableModel() {
		super();
	}

	public SendTaskAbstractTableModel( 
			ArrayList<SendTaskEntity> list) {
		super();
		this.list=list;
		
	}
	
	public void dataToTable(){
		this.data=new Object[list.size()][15];
		for (int i = 0; i < list.size(); i++) {
			data[i][0]=list.get(i).getId()+"";
			data[i][1]=list.get(i).getSendTask();
			data[i][2]=list.get(i).getPost();
			data[i][3]=list.get(i).getTitle();
			if(list.get(i).getProxyStart()==0){
				data[i][4]="��";
			}else if(list.get(i).getProxyStart()==1){
				data[i][4]="��";
			}
			data[i][5]=list.get(i).getContentPath();
			data[i][6]=list.get(i).getAccessoryPath();
			data[i][7]=list.get(i).getSendTaskCount();
			data[i][8]=list.get(i).getLastsendTaskDate();
			data[i][9]=list.get(i).getAccountSendNum();
			data[i][10]=list.get(i).getAccountStartLinks();
			data[i][11]=list.get(i).getSendTaskReceivesPath();
			data[i][12]=list.get(i).getSendIntervalTime();
			//data[i][13]="LOOK";
			data[i][13]=new Boolean(false);
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
		    if(columnIndex==13){
		    	b=true;
		    }
//		    else if(columnIndex==7){
//		    	b=true;
//		    }
//		    columnIndex==0?true:false;
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
		System.out.println(e.getFirstIndex());
		
	}

}
