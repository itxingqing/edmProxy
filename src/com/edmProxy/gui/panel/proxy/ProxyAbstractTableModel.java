package com.edmProxy.gui.panel.proxy;

import java.util.ArrayList;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.edmProxy.entity.ProxyEntity;

public class ProxyAbstractTableModel extends AbstractTableModel implements ListSelectionListener{

	// 定义表头数据
	private String[] head = {"编号" ,"服务器","端口","类型","账号","密码",
			"是否有效","是否启用","代理次数","创建时间","最近代理",
			"修改","选择"};
	private Class[] typeArray = {Integer.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Object.class,Object.class,Object.class,
			Object.class,Boolean.class};

	// 定义表的内容数据
//	private Object[] data1 = {new Integer(1), "202.168.45.3","1080","scoket5","user","123456",
//			"是","是","2013-10-1 10:01","3","2013-10-5 10:01",
//			"UPD","DEL",new Boolean(false)};
//	private Object[] data2 = {new Integer(2), "202.168.45.4","1080","scoket5","user","123456",
//			"是","是","2013-10-1 10:01","3","2013-10-5 10:01",
//			"UPD","DEL",new Boolean(false)};
	// 定义表格每一列的数据类型
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
				data[i][6]="否";
			}else if(list.get(i).getValid()==1){
				data[i][6]="是";
			}
			if(list.get(i).getStart()==0){
				data[i][7]="否";
			}else if(list.get(i).getStart()==1){
				data[i][7]="是";
			}
			data[i][8]=list.get(i).getProxyCount();
			data[i][9]=list.get(i).getCreateDate();
			data[i][10]=list.get(i).getLastProxyDate();
			data[i][11]="UPDATE";
			data[i][12]=new Boolean(false);
		}
	}
	
	
	// 获得表格的列数
	public int getColumnCount() {
		return head.length;
	}

	// 获得表格的行数
	public int getRowCount() {
		return data.length;
	}

	// 获得表格的列名称
	@Override
	public String getColumnName(int column) {
		return head[column];
	}

	// 获得表格的单元格的数据
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	// 使表格具有可编辑性,使JTable中的JButton具有事件的关键步骤
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

	// 替换单元格的值
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		data[rowIndex][columnIndex] = aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	// 实现了如果是boolean自动转成JCheckbox
	/*
	 * 需要自己的celleditor这么麻烦吧。jtable自动支持Jcheckbox，
	 * 只要覆盖tablemodel的getColumnClass返回一个boolean的class， jtable会自动画一个Jcheckbox给你，
	 * 你的value是true还是false直接读table里那个cell的值就可以
	 */
	public Class getColumnClass(int columnIndex) {
		return typeArray[columnIndex];// 返回每一列的数据类型
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
