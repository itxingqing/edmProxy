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


public class AccountDAO {

	private Connection conn = null; // �������ݿ�����
	private PreparedStatement pstmt = null; // ����ִ��SQL���

	private Statement stmt = null;

	private ResultSet rs = null; // �û������ѯ�����
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());

//	private static String TABLENAME = "";
	
	public AccountDAO(){
//		ReadPrpperties readPrpperties=new ReadPrpperties();
//		TABLENAME=readPrpperties.getTABLENAME();
	}
	

	/** ����ԭʼ���� */
	public int insert(AccountEntity obj) {
		int count = 0; // ���ܷ���ֵ
		String preparedSql = "insert into account_tab"
				+ "(account,password,post,"
				+ " valid,start,createDate,"
				+ "sendCount,lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?" +",?,?,?)";
		// ռλ��������
		Object[] param = {obj.getAccount(),obj.getPassword(),
						  obj.getPost(),obj.getValid()	, 
						  obj.getStart(),dateAndTime,0,
						  dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		return count;
	}
	
	// �������봫��list
	public String insertBatch(ArrayList<AccountEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 1;
		String msg = "";
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			String sql = "insert into account_tab"
				+ "(account,password,post,"
				+ " valid,start,createDate,"
				+ "sendCount,lastSendDate,remark) "
				+ "values(?,?,?" +",?,?,?," +"?,?,?)";
			ps = con.prepareStatement(sql);
			int[] count;
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getAccount());
				ps.setString(2, list.get(i).getPassword());
				ps.setString(3, list.get(i).getPost());
				ps.setInt(4,list.get(i).getValid());
				ps.setInt(5,list.get(i).getStart());
				ps.setString(6, dateAndTime);
				ps.setInt(7,0);
				ps.setString(8,dateAndTime);
				ps.setString(9,"");
				ps.addBatch();
			}
			if (i % 10000 == 0) {
				count=ps.executeBatch();
			}
			count=ps.executeBatch();
			for(int j=0;j<count.length;j++){
				System.out.println(count[j]);
			}
			con.commit();
			// con.setAutoCommit(autoCommit);
			msg = "\nһ����-" + i + "-�����ݲ������! \n";
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
	
	
	//���� ����list ���� �ж��Ƿ��и����ݣ����û�������
	public String InsertBatchNotRepetitionByAccount(ArrayList<AccountEntity> list) throws SQLException,
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
			String sql = "insert into account_tab" +
					"(account,password,post,valid,start," +
					"createDate,sendCount," +
					"lastSendDate,remark)" +
					"select " +
					"?,?,?,?,?,?,?,?,? " +
					"from dual " +
					"WHERE NOT EXISTS" +
					"(select account " +
					"from account_tab " +
					"where account=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getAccount());
				ps.setString(2, list.get(i).getPassword());
				ps.setString(3,list.get(i).getPost());
				ps.setInt(4, 1);
				ps.setInt(5, 1);
				ps.setString(6, dateAndTime);
				ps.setInt(7,0);
				ps.setString(8, dateAndTime);
				ps.setString(9,"");
				ps.setString(10,list.get(i).getAccount());
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
	
	public ArrayList<AccountEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<AccountEntity> list = new ArrayList(); // ������������б�
		
		//rowNew��ǰҳ����������
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// ���·�����ҳ����hql�ǲ��еģ�hql��֧��top,Ҫʹ��sql
			sbSql.append("select * from account_tab limit ");
			sbSql.append(rowNewPage+","+pageSize);
				//pstmt = conn.prepareStatement(sbSql.toString()); 
			stmt = conn.prepareStatement(sbSql.toString()); 

			//System.out.println(sbSql);
			rs = stmt.executeQuery(sbSql.toString()); // ִ��sqlȡ�ý����

			/* ѭ�����ظ���Ϣ��װ��List */
			while (rs.next()) {
				AccountEntity obj = new AccountEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setAccount(rs.getString("account"));
				obj.setPassword(rs.getString("password"));
				obj.setPost(rs.getString("post"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setSendCount(rs.getInt("sendCount"));
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
					+ "account_tab";
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
	//
	
    //-------------------------------------------------------------------------------------��ҳ����
	//������ȫ����ѯ------------------------------------------------------------��ҳ--------������


	/// ͨ��id����ɾ�� 
	public int deleteBatch(String ids) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		
		String preparedSql = "delete from account_tab where id in (?)";
		// ռλ��������
		String[] arr=ids.split(",");
		for (int i = 0; i < arr.length; i++) {
			Object[] param = { arr[i] };
			count = dbHelp.executeSQL(preparedSql,param);
		}
		System.out.println("count-->"+count);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//ͨ��id��ѯ
	public AccountEntity findById(String id) {
			AccountEntity obj = new AccountEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "account_tab where id="+id;
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setAccount(rs.getString("account"));
					obj.setPassword(rs.getString("password"));
					obj.setPost(rs.getString("post"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setSendCount(rs.getInt("sendCount"));
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
	public AccountEntity findByAccount(String account) {
			AccountEntity obj = new AccountEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "account_tab where account="+"'"+account+"'";
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setAccount(rs.getString("account"));
					obj.setPassword(rs.getString("password"));
					obj.setPost(rs.getString("post"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setSendCount(rs.getInt("sendCount"));
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
	public int update(AccountEntity obj) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "update " + "account_tab"
				+ " set account=?,password=?,post=?," +
						"valid=?,start=?," +
						"remark=?  where id=? ";
		Object[] param = {obj.getAccount(),obj.getPassword(),
				obj.getPost(),obj.getValid(),
				obj.getStart(),obj.getRemark(),obj.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//�޸�
	public int updateValidByAccount(String account,int valid) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "update " + "account_tab"
				+ " set valid=? where account=? ";
		Object[] param = {valid,account};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	
	
	//ɾ����������
	public int deleteAll() {
		int count = 0; // ���ܷ���ֵ		
		String preparedSql = "delete from account_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	//��ѯ
	public ArrayList<AccountEntity> findBy(String account) {
		ArrayList<AccountEntity> list = new ArrayList(); 
		
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// SQL���
			String preparedSql = "select * from " + "account_tab where account like "+"'%"+account+"%'";
			//System.out.println(preparedSql);
			pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
			
			//ѭ�����ظ���Ϣ��װ��List
			while (rs.next()) {
				AccountEntity obj = new AccountEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setAccount(rs.getString("account"));
				obj.setPassword(rs.getString("password"));
				obj.setPost(rs.getString("post"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setSendCount(rs.getInt("sendCount"));
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
