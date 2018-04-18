package com.edmProxy.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

import com.edmProxy.util.GuiUtil;





public class BaseFrame extends JFrame {
	
	public BaseFrame(){
		GuiUtil.alloy(BaseFrame.this);
	}
	@Override
	  public void setVisible(boolean b) {
		this.centerWindow(this);//�������
	    //this.screenWindow(this);//��������
		//this.close(this);//����X��ť��ʾ�ر�
		//����ͼ��
        this.setIconImage(new ImageIcon("img/pc.gif").getImage());
	    super.setVisible(b);
	  }
	
	
	/** ������� */
	public static void centerWindow(Container win) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scr = toolkit.getScreenSize();
		int x = (scr.width - win.getWidth()) / 2;
		int y = (scr.height - win.getHeight()) / 2;
		win.setLocation(x, y);
	}

	/** ���ô����С������Ļ */
	public static void screenWindow(Container win) {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scr = toolkit.getScreenSize();
		win.setSize((int) scr.getWidth(), (int) scr.getHeight() - 25); // ��ǰ���ڴ�С
	}

	/**
	 * �˷�����һ��̬�����ǽ����յ���JTAble������ż�зֱ����óɱ�ɫ������ɫ
	 * 
	 * @param table
	 *            JTable
	 */
	public static void makeFace(JTable table) {
		try {
			DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table,
						Object value, boolean isSelected, boolean hasFocus,
						int row, int column) {
					if (row % 2 == 0)
						setBackground(Color.white); // ���������е�ɫ
					else if (row % 2 == 1)
						setBackground(new Color(206, 231, 255)); // ����ż���е�ɫ
					return super.getTableCellRendererComponent(table, value,
							isSelected, hasFocus, row, column);
				}
			};
			for (int i = 0; i < table.getColumnCount(); i++) {
				table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//����X��ť��ʾ
	public static void close(JFrame source){
		source.setDefaultCloseOperation(source.DO_NOTHING_ON_CLOSE);
		source.addWindowListener(new WindowAdapter() {   
            public void windowClosing(WindowEvent e) {   
                int flag = JOptionPane.showConfirmDialog(null, "�����Ҫ�뿪ϵͳ��?",   
                        "ϵͳ��ʾ", JOptionPane.YES_NO_OPTION,   
                        JOptionPane.INFORMATION_MESSAGE);   
                if(JOptionPane.YES_OPTION == flag){   
                    System.exit(0);   
                }else{   
                    return;   
                }   
            }   
        });  
	}
	
	//ˢ�´���
	public static void refresh(JFrame source){
		source.repaint();
		source.invalidate();
		source.validate();
	}
	

	
	//�뿪ϵͳ��ʾ
	public static void exit(JFrame source){
		   int val = JOptionPane.showConfirmDialog( source, "ȷ���뿪����3Dͳ��ϵͳ��");
		   if(val==JOptionPane.YES_OPTION){
		     System.exit(0);
		   }
			   source.setVisible(true);
	}

}
