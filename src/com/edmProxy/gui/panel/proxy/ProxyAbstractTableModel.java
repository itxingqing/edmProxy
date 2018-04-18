package com.edmProxy.gui.panel.proxy;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.edmProxy.entity.ProxyEntity;

public class ProxyAbstractTableModel extends AbstractTableModel implements ListSelectionListener{

	// �����ͷ����
	private String[] head = {"���" ,"������","�˿�","����","�˺�","����",
			"�Ƿ���Ч","�Ƿ�����","�������","����ʱ��","�������",
			"�޸�","ѡ��"};
	private Class[] typeArray = {Integer.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Boolean.class};

	// ��������������
//	private Object[] data1 = {new Integer(1), "202.168.45.3","1080","scoket5","user","123456",
//			"��","��","2013-10-1 10:01","3","2013-10-5 10:01",
//			"UPD","DEL",new Boolean(false)};
//	private Object[] data2 = {new Integer(2), "202.168.45.4","1080","scoket5","user","123456",
//			"��","��","2013-10-1 10:01","3","2013-10-5 10:01",
//			"UPD","DEL",new Boolean(false)};
	// ������ÿһ�е���������
	private Object[][] data;//={data1,data2};
	private ArrayList<ProxyEntity> list;
	

	public ProxyAbstractTableModel() {
		super();
	}

	public ProxyAbstractTableModel( 
			ArrayList<ProxyEntity> list) {
		super();
		this.list=list;
		
	}
	
	public void dataToTable(){
		this.data=new Object[list.size()][13];
		for (int i = 0; i < list.size(); i++) {
			data[i][0]=list.get(i).getId()+"";
			data[i][1]=list.get(i).getProxyHost();
			data[i][2]=list.get(i).getProxyPort();
			if(list.get(i).getProxyType()==0){
				data[i][3]="HTTP";
			}else if(list.get(i).getProxyType()==1){
				data[i][3]="SCOKET";
			}
			data[i][4]=list.get(i).getProxyAccount();
			data[i][5]=list.get(i).getProxyPassword();
			if(list.get(i).getValid()==0){
				data[i][6]="��";
			}else if(list.get(i).getValid()==1){
				data[i][6]="��";
			}
			if(list.get(i).getStart()==0){
				data[i][7]="��";
			}else if(list.get(i).getStart()==1){
				data[i][7]="��";
			}
			data[i][8]=list.get(i).getProxyCount();
			data[i][9]=list.get(i).getCreateDate();
			data[i][10]=list.get(i).getLastProxyDate();
			data[i][11]="UPDATE";
			data[i][12]=new Boolean(false);
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
		    if(columnIndex==11){
		    	b=true;
		    }else if(columnIndex==12){
		    	b=true;
		    }
		    	
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
		System.out.println("--");
//		System.out.println(e.getSource());
//		System.out.println(e.getValueIsAdjusting());
		System.out.println(e.getFirstIndex());
		
	}

	public Object[][] getData() {
		return data;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

}
