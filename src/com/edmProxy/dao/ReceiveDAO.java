package com.edmProxy.dao;

import java.sql.Statement;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.edmProxy.entity.AccountEntity;
import com.edmProxy.entity.ProxyEntity;
import com.edmProxy.entity.ReceiveEntity;


public class ReceiveDAO {

	private Connection conn = null; // �������ݿ�����
	private PreparedStatement pstmt = null; // ����ִ��SQL���

	private Statement stmt = null;

	private ResultSet rs = null; // �û������ѯ�����
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());

//	private static String TABLENAME = "";
	
	public ReceiveDAO(){
//		ReadPrpperties readPrpperties=new ReadPrpperties();
//		TABLENAME=readPrpperties.getTABLENAME();
	}
	

	/** ����ԭʼ���� */
	public int insert(ReceiveEntity obj) {
		int count = 0; // ���ܷ���ֵ
		String preparedSql = "insert into receive_tab"
				+ "(receive,post,"
				+ "sendCount,createDate,"
				+ "lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?)";
		// ռλ��������
		Object[] param = {obj.getReceive(),obj.getPost(),
						  0,dateAndTime,
						  dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		return count;
	}
	
	//���� ����list ���� �ж��Ƿ��и����ݣ����û�������
	public String InsertBatchNotRepetitionByAccount(ArrayList<ReceiveEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 0; //��ִ����
		int k=0;//�ɹ�
		int j=0;//ʧ�������ظ���δִ�У�
		String msg = "";
		int[] count;
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			
			String sql = "insert into receive_tab" +
					"(receive,post," +
					"sendCount,createDate," +
					"lastSendDate,remark)values(?,?,?,?,?,?)";
//			String sql = "insert into receive_tab" +
//					"(receive,post," +
//					"sendCount,createDate," +
//					"lastSendDate,remark)" +
//					"select " +
//					"?,?,?,?,?,? " +
//					"from dual " +
//					"WHERE NOT EXISTS" +
//					"(select receive " +
//					"from receive_tab " +
//					"where receive=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getReceive());
				ps.setString(2,list.get(i).getPost());
				ps.setInt(3, 0);
				ps.setString(4, dateAndTime);
				ps.setString(5, dateAndTime);
				ps.setString(6,"");
				//ps.setString(7,list.get(i).getReceive());
				ps.addBatch();
				if(i>0){
					if (i % 100000 == 0) {
						count=ps.executeBatch();
						if(count.length>0){
							if(count[0]==1){
								k++;
							}else if(count[0]==0){
								j++;
							}
						}
					}	
				}
				count=ps.executeBatch();
				if(count.length>0){
					if(count[0]==1){
						k++;
					}else if(count[0]==0){
						j++;
					}
				}
			}
			
			con.commit();
			// con.setAutoCommit(autoCommit);
			msg="����ִ�У�����"+i+"�����ݣ�����"+j+"���ظ�δִ�У��ɹ�ִ��"+k+"������";
			return msg;
		} catch (SQLException e) {
			e.printStackTrace();
			// conn.rollback();
			return null;
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
	}
	
	

	 // ��ҳ ��Ҫ���ݣ�1������������2��ÿҳ��ʾ���������ݡ�3����������/ÿҳ��ʾ����������=ҳ����4��PageSize��ÿҳ��ʾ���������ݡ�
	 //TotalRow:����������pageTotalRow����ǰҳ���������� page:��ǰҳ��
	 //��ҳ ���룺pageNow����ǰҳ��PageSize:ÿҳ��ʾ���������ݡ�
	
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<ReceiveEntity> list = new ArrayList(); // ������������б�
		
		//rowNew��ǰҳ����������
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// ���·�����ҳ����hql�ǲ��еģ�hql��֧��top,Ҫʹ��sql
			sbSql.append("select * from receive_tab limit ");
			sbSql.append(rowNewPage+","+pageSize);
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 

			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(sbSql.toString()); // ִ��sqlȡ�ý����

			/* ѭ�����ظ���Ϣ��װ��List */
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setLastSendDate(rs.getDate("lastSendDate"));
				obj.setRemark(rs.getString("remark"));
				list.add(obj);
			}

		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
	//		closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return list;
	}

	// ��ѯ������������
	public int queryCount() {
		int count = 0;
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// SQL���
			String preparedSql = "select count(*) totalCount  from "
					+ "receive_tab";
			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
			// pstmt.setInt();

			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����

			while (rs.next()) {
				count = rs.getInt("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return count;
	}
	
	// ��ѯ������������ ��������
	public int queryCountByCondition(String condition) {
		int count = 0;
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// SQL���
			String preparedSql = "select count(*) totalCount  from "
					+ "receive_tab "+condition;
			System.out.println(preparedSql);
			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
			// pstmt.setInt();

			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����

			while (rs.next()) {
				count = rs.getInt("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
			//closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return count;
	}
	
	//��ҳ����������
	public ArrayList<ReceiveEntity> pageingBypageNowAndpageSizeByCondition(int pageNow,int pageSize,String condition) {
		ArrayList<ReceiveEntity> list = new ArrayList(); // ������������б�
		
		//rowNew��ǰҳ����������
//		int rowNewPage=1;
//		if(pageNow>1){
//			rowNewPage=(pageNow-1)*pageSize;
//		}
		int rowNewPage=(pageNow-1)*pageSize;
		
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			sbSql.append("select * from receive_tab ");
			sbSql.append(condition);
			sbSql.append(" limit "+rowNewPage+","+pageSize);
			
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 
			
			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
			System.out.println(sbSql.toString());
			rs = stmt.executeQuery(sbSql.toString()); // ִ��sqlȡ�ý����

			/* ѭ�����ظ���Ϣ��װ��List */
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setLastSendDate(rs.getDate("lastSendDate"));
				obj.setRemark(rs.getString("remark"));
				list.add(obj);
			}

		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
	//		closeAll(conn, pstmt, rs);
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return list;
	}
	
	//
	
    //-------------------------------------------------------------------------------------��ҳ����
	//������ȫ����ѯ------------------------------------------------------------��ҳ--------������


	/// ͨ��id����ɾ�� 
	public int deleteBatch(String ids) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "delete from receive_tab where id in (?)";
		// ռλ��������
		String[] arr=ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			Object[] param = { arr[i] };
			count = dbHelp.executeSQL(preparedSql,param);
		}
		//System.out.println("count-->"+count);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//ͨ��id��ѯ
	public ReceiveEntity findById(String id) {
		    ReceiveEntity obj = new ReceiveEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "receive_tab where id="+id;
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setReceive(rs.getString("receive"));
					obj.setPost(rs.getString("post"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setLastSendDate(rs.getDate("lastSendDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // �����쳣
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
	}
	//ͨ�������ѯ
	public ReceiveEntity findByReceive(String receive) {
		ReceiveEntity obj = new ReceiveEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "receive_tab where receive="+"'"+receive+"'";
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setReceive(rs.getString("receive"));
					obj.setPost(rs.getString("post"));
					obj.setSendCount(rs.getInt("sendCount"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setLastSendDate(rs.getDate("lastSendDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // �����쳣
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
	}
	
	//�޸�
	public int update(ReceiveEntity obj) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "update " + "receive_tab"
				+ " set receive=?,post=?," +
						"remark=?  where id=? ";
		Object[] param = {obj.getReceive(),obj.getPost(),obj.getRemark(),obj.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//ɾ����������
	public int deleteAll() {
		int count = 0; // ���ܷ���ֵ		
		String preparedSql = "delete from receive_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//��ѯ
	public ArrayList<ReceiveEntity> findBy(String value,int condition) {
		ArrayList<ReceiveEntity> list = new ArrayList(); 
		
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// SQL���
			String preparedSql = "";
			
			switch (condition) {
			case 0:
				preparedSql = "select * from " + "receive_tab where receive like "+"'%"+value+"%'";
				break;
			case 1:
				preparedSql = "select * from " + "receive_tab where post like "+"'%"+value+"%'";
				break;
			case 2:
				preparedSql = "select * from " + "receive_tab where sendCount <= "+value;
				break;
			case 3:
				preparedSql = "select * from " + "receive_tab where date_format(createDate,'%Y-%m-%d')='"+value+"'";
				break;
			case 4:
				preparedSql = "select * from " + "receive_tab where date_format(lastSendDate,'%Y-%m-%d')='"+value+"'";
				break;
			default:
				preparedSql = "select * from " + "receive_tab where receive like "+"'%"+value+"%'";
				break;
			}
			
			
			//System.out.println(preparedSql);
			pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
			
			//ѭ�����ظ���Ϣ��װ��List
			while (rs.next()) {
				ReceiveEntity obj = new ReceiveEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setReceive(rs.getString("Receive"));
				obj.setPost(rs.getString("post"));
				obj.setSendCount(rs.getInt("sendCount"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setLastSendDate(rs.getDate("lastSendDate"));
				obj.setRemark(rs.getString("remark"));
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return list;
	}
	

	// ����ID��������
	public static void main(String[] args) {
		//ProxyServerDAO lotteryDAO = new ProxyServerDAO();
		// System.out.println("---------------------->:"+lotteryDAO.queryCount());
	}


	


	


	

}
