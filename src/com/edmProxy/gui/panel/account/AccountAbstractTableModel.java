package com.edmProxy.gui.panel.account;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;

public class AccountAbstractTableModel extends AbstractTableModel implements ListSelectionListener{

	// �����ͷ����
	String[] head = {"���" ,"�˺�","����","�ʾ�",
			"�Ƿ���Ч","�Ƿ�����","����ʱ��","���ʹ���","�������",
			"�޸�","ѡ��"};
	Class[] typeArray = {Integer.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Boolean.class};

	
	// ������ÿһ�е���������


	private Object[][] data;
	private ArrayList<AccountEntity> list;
	
	public AccountAbstractTableModel() {
		super();
	}

	public AccountAbstractTableModel( 
			ArrayList<AccountEntity> list) {
		super();
		this.list=list;
		
	}
	
	public void dataToTable(){
		this.data=new Object[list.size()][11];
		for (int i = 0; i < list.size(); i++) {
			data[i][0]=list.get(i).getId()+"";
			data[i][1]=list.get(i).getAccount();
			data[i][2]=list.get(i).getPassword();
			data[i][3]=list.get(i).getPost();
			if(list.get(i).getValid()==0){
				data[i][4]="��";
			}else if(list.get(i).getValid()==1){
				data[i][4]="��";
			}
			if(list.get(i).getStart()==0){
				data[i][5]="��";
			}else if(list.get(i).getStart()==1){
				data[i][5]="��";
			}
			data[i][6]=list.get(i).getCreateDate();
			data[i][7]=list.get(i).getSendCount();
			data[i][8]=list.get(i).getLastSendDate();
			data[i][9]="UPDATE";
			data[i][10]=new Boolean(false);
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
		    if(columnIndex==9){
		    	b=true;
		    }else if(columnIndex==10){
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

}
