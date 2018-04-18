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

import com.edmProxy.entity.ProxyEntity;


public class ProxyDAO {

	private Connection conn = null; // �������ݿ�����
	private PreparedStatement pstmt = null; // ����ִ��SQL���

	private Statement stmt = null;

	private ResultSet rs = null; // �û������ѯ�����
	private DBHelp dbHelp = new DBHelp();
	private String dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(Calendar.getInstance().getTime());	
	public ProxyDAO(){
	}
	

	/** ����ԭʼ���� */
	public int insert(ProxyEntity obj) {
		int count = 0; // ���ܷ���ֵ
		// String serialnumber = ""; // �������������Number
		// SQL���
		String preparedSql = "insert into proxy_tab" //+ TABLENAME
				+ "(proxyHost,proxyPort,proxyType,"
				+ " proxyAccount,proxyPassword,"
				+ "valid,start,createDate,"
				+ "proxyCount,lastProxyDate,remark) "
				+ "values(?,?,?,?" +",?,?,?,?," +"?,?,?)";
		// ռλ��������
		Object[] param = {obj.getProxyHost(),obj.getProxyPort(),
						  (Integer)obj.getProxyType(),obj.getProxyAccount()	, 
						  obj.getProxyPassword(),(Integer)obj.getValid(),
						  (Integer)obj.getStart(),
						  dateAndTime,0,dateAndTime,obj.getRemark() };

		count = dbHelp.executeSQL(preparedSql, param);
		//System.out.println("->"+obj.getProxyType()+"��"+obj.getValid()+":"+obj.getStart());
		return count;
	}
	
	// �������봫��list
	public String insertBatch(ArrayList<ProxyEntity> list) throws SQLException,
			ClassNotFoundException {
		Connection con = null;
		PreparedStatement ps = null;
		int i = 1;
		String msg = "";
		try {
			con = dbHelp.getConn();
			con.setAutoCommit(false);
			String sql = "insert into " + "proxy_tab"
					+ "(proxyHost,proxyPort,proxyType,proxyAccount," +
					"proxyPassword,valid,start,createDate,proxyCount," +
					"lastProxyDate,remark"+
					") " + "values(?,?,?,?,?,?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);

			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getProxyHost());
				ps.setString(2, list.get(i).getProxyPort());
				ps.setInt(3, list.get(i).getProxyType());
				ps.setString(4,list.get(i).getProxyAccount());
				ps.setString(5,list.get(i).getProxyPassword());
				ps.setInt(6,list.get(i).getValid());
				ps.setInt(7,list.get(i).getStart());
				ps.setString(8, dateAndTime);
				ps.setInt(9,0);
				ps.setString(10,dateAndTime);
				ps.setString(11,"");
				ps.addBatch();

				// System.out.println("-------i��ֵ��"+i);
				if (i % 10000 == 0) {
					ps.executeBatch();
				}
				ps.executeBatch();
				
				
			}
			
			// System.out.println(dateAndTime);
			con.commit();
			// con.setAutoCommit(autoCommit);
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
	public String InsertBatchNotRepetitionByProxyHost(ArrayList<ProxyEntity> list) throws SQLException,
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
			String sql = "insert into  proxy_tab" +
					"(proxyHost,proxyPort,proxyType,proxyAccount," +
					"proxyPassword,valid,start,createDate,proxyCount," +
					"lastProxyDate,remark)" +
					"select " +
					"?,?,?,?,?,?,?,?,?,?,? " +
					"from dual " +
					"WHERE NOT EXISTS" +
					"(select proxyHost " +
					"from proxy_tab " +
					"where proxyHost=?);";
			ps = con.prepareStatement(sql);
			for (i = 0; i < list.size(); i++) {
				ps.setString(1, list.get(i).getProxyHost());
				ps.setString(2, list.get(i).getProxyPort());
				ps.setInt(3, 1);
				ps.setString(4,list.get(i).getProxyAccount());
				ps.setString(5,list.get(i).getProxyPassword());
				ps.setInt(6,1);
				ps.setInt(7,1);
				ps.setString(8, dateAndTime);
				ps.setInt(9,0);
				ps.setString(10,dateAndTime);
				ps.setString(11,"");
				ps.setString(12,list.get(i).getProxyHost());
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
	
	public ArrayList<ProxyEntity> pageingBypageNowAndpageSize(int pageNow,int pageSize) {
		ArrayList<ProxyEntity> list = new ArrayList(); // ������������б�
		
		//rowNew��ǰҳ����������
		int rowNewPage=(pageNow-1)*pageSize;
		
		StringBuffer sbSql = new StringBuffer();
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// ���·�����ҳ����hql�ǲ��еģ�hql��֧��top,Ҫʹ��sql
			sbSql.append("select * from proxy_tab limit ");
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
				ProxyEntity obj = new ProxyEntity(); // �ظ�����
				obj.setId(rs.getInt("id"));
				obj.setProxyHost(rs.getString("proxyHost"));
				obj.setProxyPort(rs.getString("proxyPort"));
				obj.setProxyType(rs.getInt("proxyType"));
				obj.setProxyAccount(rs.getString("proxyAccount"));
				obj.setProxyPassword(rs.getString("proxyPassword"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setProxyCount(rs.getInt("proxyCount"));
				obj.setLastProxyDate(rs.getDate("lastProxyDate"));
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
					+ "proxy_tab";
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
		
		String preparedSql = "delete from proxy_tab where id in (?)";
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
		public ProxyEntity findById(String id) {
		ProxyEntity obj = new ProxyEntity(); // �ظ�����
		try {
			conn = dbHelp.getConn(); // �õ����ݿ�����
			// SQL���
			String preparedSql = "select * from " + "proxy_tab where id="+id;
			pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
			
			//ѭ�����ظ���Ϣ��װ��List
			while (rs.next()) {
				obj.setId(rs.getInt("id"));
				obj.setProxyHost(rs.getString("proxyHost"));
				obj.setProxyPort(rs.getString("proxyPort"));
				obj.setProxyType(rs.getInt("proxyType"));
				obj.setProxyAccount(rs.getString("proxyAccount"));
				obj.setProxyPassword(rs.getString("proxyPassword"));
				obj.setValid(rs.getInt("valid"));
				obj.setStart(rs.getInt("start"));
				obj.setCreateDate(rs.getDate("createDate"));
				obj.setProxyCount(rs.getInt("proxyCount"));
				obj.setLastProxyDate(rs.getDate("lastProxyDate"));
				obj.setRemark(rs.getString("remark"));
			}
		} catch (Exception e) {
			e.printStackTrace(); // �����쳣
		} finally {
			dbHelp.closeAll(conn, pstmt, rs);
		}
		return obj;
	}
		//ͨ��������ѯ
		public ProxyEntity findByProxyHost(String proxyHost) {
			ProxyEntity obj = new ProxyEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "proxy_tab where proxyHost='"+proxyHost+"'";
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // �����쳣
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
		}
		
		//ͨ��������ѯ
		public ProxyEntity findFistByValid(int valid) {
			ProxyEntity obj = new ProxyEntity(); // �ظ�����
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "proxy_tab where valid="+valid+" limit 0,1";
				System.out.println(preparedSql);
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
					obj.setRemark(rs.getString("remark"));
				}
			} catch (Exception e) {
				e.printStackTrace(); // �����쳣
			} finally {
				dbHelp.closeAll(conn, pstmt, rs);
			}
			return obj;
		}
		
		
		
	//ģ����ѯ
		public ArrayList<ProxyEntity> findBy(String proxyHost) {
			ArrayList<ProxyEntity> list = new ArrayList(); 
			
			try {
				conn = dbHelp.getConn(); // �õ����ݿ�����
				// SQL���
				String preparedSql = "select * from " + "proxy_tab where proxyHost like "+"'%"+proxyHost+"%'";
				//System.out.println(preparedSql);
				pstmt = conn.prepareStatement(preparedSql); // ִ��sqlȡ�ý����
				rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
				
				//ѭ�����ظ���Ϣ��װ��List
				while (rs.next()) {
					ProxyEntity obj = new ProxyEntity(); // �ظ�����
					obj.setId(rs.getInt("id"));
					obj.setProxyHost(rs.getString("proxyHost"));
					obj.setProxyPort(rs.getString("proxyPort"));
					obj.setProxyType(rs.getInt("proxyType"));
					obj.setProxyAccount(rs.getString("proxyAccount"));
					obj.setProxyPassword(rs.getString("proxyPassword"));
					obj.setValid(rs.getInt("valid"));
					obj.setStart(rs.getInt("start"));
					obj.setCreateDate(rs.getDate("createDate"));
					obj.setProxyCount(rs.getInt("proxyCount"));
					obj.setLastProxyDate(rs.getDate("lastProxyDate"));
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
	
	
	//�޸�
	public int update(ProxyEntity proxyEntiy) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "update " + "proxy_tab"
				+ " set proxyHost=?,proxyPort=?,proxyType=?," +
						"proxyAccount=?,proxyPassword=?," +
						"valid=?,start=?,remark=?  where id=? ";
		Object[] param = {proxyEntiy.getProxyHost(),proxyEntiy.getProxyPort(),
				proxyEntiy.getProxyType(),proxyEntiy.getProxyAccount(),
				proxyEntiy.getProxyPassword(),proxyEntiy.getValid(),
				proxyEntiy.getStart(),proxyEntiy.getRemark(),proxyEntiy.getId()};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	
	//�޸� ͨ�����������޸�valid
	public int updateValidByProxyHost(String proxyHost,int valid) {
		int count = 0; // ���ܷ���ֵ
		// SQL���
		String preparedSql = "update " + "proxy_tab"
				+ " set valid=? where proxyHost=? ";
		Object[] param = {valid,proxyHost};
		count = dbHelp.executeSQL(preparedSql, param);
		return count;	
	}	
	
	//ɾ����������
	public int deleteAll() {
		int count = 0; // ���ܷ���ֵ		
		String preparedSql = "delete from proxy_tab";
		Object[] param = {};
		count = dbHelp.executeSQL(preparedSql,param);
		dbHelp.closeAll(conn, pstmt, rs);
		return count;
	}
	

	/**
	 * ��ѯ
	 */
//	public List findListLottery() {
//		List list = new ArrayList(); // ������������б�
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// SQL���
//			String preparedSql = "select * from " + TABLENAME;
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			// stmt = conn.prepareStatement(preparedSql); //
//			// �õ�PreparedStatement����
//			rs = stmt.executeQuery(preparedSql); // ִ��sqlȡ�ý����
//			/* ѭ�����ظ���Ϣ��װ��List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // �ظ�����
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}

	/** ͨ��idɾ�� */
//	public int deleteLottery(int id) {
//		int count = 0; // ���ܷ���ֵ
//		// SQL���
//		String preparedSql = "delete from " + TABLENAME + " where id=? ";
//		// ռλ��������
//		Object[] param = { id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}


	/** ͨ��id�޸�omitsum */
//	public int updateOmitsum(int omitsum, int id) {
//		int count = 0; // ���ܷ���ֵ
//		// SQL���
//		String preparedSql = "update " + TABLENAME
//				+ " set omitsum=?  where id=? ";
//		Object[] param = { omitsum, id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	/** ͨ��id�޸�comparecount */
//	public int updateComparecount(int comparecount, int id) {
//		int count = 0; // ���ܷ���ֵ
//		// SQL���
//		String preparedSql = "update " + TABLENAME
//				+ " set comparecount=?  where id=? ";
//		Object[] param = { comparecount, id };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	/**
	 * ��գ�һ����©��¼null��һ����©-1��һ����©״̬1���ϴ���©��¼null���ϴ���©-1���ϴ���©״̬1��
	 * ������©��¼null��������©-1��������©״̬1������©��¼null������©-1������©״̬1, ��©ͳ��0���Ƚϴ���0
	 * 
	 */
//	public int updateLotteryOmit1Omit2Omit3OmitnewOther() {
//		int count = 0; // ���ܷ���ֵ
//		// SQL���
//		// omit1record,omit1,omit1state,omit2record,omit2,omit2state,omit3record,omit3,omit3state,
//		// omitnewrecord,omitnew,omitnewstate,omitsum,comparecount,dateAndTime
//		String preparedSql = "update " + TABLENAME
//				+ " set omit1record=? ,omit1=?,omit1state=?,"
//				+ "omit2record=?,omit2=?,omit2state=?,"
//				+ "omit3record=?,omit3=?,omit3state=?,"
//				+ "omitnewrecord=?,omitnew=?,omitnewstate=?,"
//				+ "omitsum=?,comparecount=?";
//		Object[] param = { "", -1, 1, "", -1, 1, "", -1, 1, "", -1, 1, 0, 0 };
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}
	
	
	//������ȫ����ѯ------------------------------------------------------------��ҳ--------��ʼ
	// ��ѯ������������,����״̬����λ��ʮλ����©ͳ�ƣ�����state��ֵ���ж�������ͳ�ƣ�1�����ݰ�λ��ʮλ��ѯ��2��������©ͳ�Ʋ�ѯ��3����������ѯ��
//	public int queryCount(int state,String hundredsNumberStr, String decadeNumberStr,String singledigitStr,int num) {
//		
//		int count = 0;
//		String preparedSql="";
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// SQL���
//			if(state==1){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME+" where hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitStr+"%'";
//				//pstmt.setString(1, "%" + hundredsNumberStr + "%");
//				//pstmt.setString(2, "%" + decadeNumberStr + "%");
//			}else if(state==2){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME+" where omitsum>="+num;
//				//pstmt.setInt(1, num);
//			}else if(state==3){
//				preparedSql = "select count(*) totalCount  from "+ TABLENAME;
//			}
//			
//			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
//			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
//
//			while (rs.next()) {
//				count = rs.getInt("totalCount");
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return count;
//	}
	
	
	/*
	 * ��ҳ ��Ҫ���ݣ�1������������2��ÿҳ��ʾ���������ݡ�3����������/ÿҳ��ʾ����������=ҳ����4��PageSize��ÿҳ��ʾ���������ݡ�
	 * TotalRow:����������pageTotalRow����ǰҳ���������� page:��ǰҳ��
	 */

	/*
	 * ��ҳ ���룺pageTotalRow����ǰҳ����������PageSize:ÿҳ��ʾ���������ݡ�
	 */
//	public List<Lottery> findPageByPageTotalRowAndPage(int state,String hundredsNumberStr, String decadeNumberStr,String singledigitNumberStr,int num,int pageTotalRow,int pageSize) {
//		List list = new ArrayList(); // ������������б�
//		StringBuffer sbSql = new StringBuffer();
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// ���·�����ҳ����hql�ǲ��еģ�hql��֧��top,Ҫʹ��sql
//			if(state==1){  //���ݰ�λ��ʮλ,��λ ��ѯ
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "where hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitNumberStr+"%'");
//				sbSql.append(" " + "order by id asc)");
//				sbSql.append(" " + "and hundreds like '%"+hundredsNumberStr+"%' and decade like '%"+decadeNumberStr+"%' and singledigit like '%"+singledigitNumberStr+"%'");
//				sbSql.append(" " + "order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
////				pstmt.setString(1, "%" + hundredsNumberStr + "%");
////				pstmt.setString(2, "%" + decadeNumberStr + "%");
//			}else if(state==2){  //������©ͳ�Ʋ�ѯ
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "where omitsum>="+num);
//				sbSql.append(" " + "order by id asc)");
//				sbSql.append(" " + "and omitsum>="+num);
//				sbSql.append(" " + "order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
////				pstmt.setInt(1, num);
//			}else if(state==3){  //��������ѯ
//				sbSql.append("select top" + " " +pageSize + " " + "*");
//				sbSql.append(" " + "from " +TABLENAME);
//				sbSql.append(" " + "where id not in");
//				sbSql.append("(select top " +pageTotalRow);
//				sbSql.append(" " + "id from " +TABLENAME);
//				sbSql.append(" " + "order by id asc) order by id asc");
//				//pstmt = conn.prepareStatement(sbSql.toString()); 
//			}
//			stmt = conn.prepareStatement(sbSql.toString()); 
//
//			//conn = dbHelp.getConn();
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = stmt.executeQuery(sbSql.toString()); // ִ��sqlȡ�ý����
//
//			/* ѭ�����ظ���Ϣ��װ��List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // �ظ�����
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}
    //-------------------------------------------------------------------------------------��ҳ����
	//������ȫ����ѯ------------------------------------------------------------��ҳ--------������

	

	// ͨ�������ͳ������������ڸ�ͳ�����Ľ��
//	public List queryByOmitsum(int num) {
//		List list = new ArrayList(); // ������������б�
//		list.clear();
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// SQL���
//			String preparedSql = "select * from " + TABLENAME
//					+ " where omitsum>=?";
//			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
//			pstmt.setInt(1, num);
//
//			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
//
//			/* ѭ�����ظ���Ϣ��װ��List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // �ظ�����
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}

	// ��ʾ�ϼ�ͳ��sumֵ
//	public int sumShow(int id) {
//
//		int sumOmit123 = 0;
//		List<Lottery> list = new ArrayList(); // ������������б�
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// SQL���
//			String preparedSql = "select * from " + TABLENAME + " where id=?";
//			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
//			pstmt.setInt(1, id);
//
//			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
//
//			/* ѭ�����ظ���Ϣ��װ��List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // �ظ�����
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//			int omit1Int = list.get(0).getOmit1();
//			int omit2Int = list.get(0).getOmit2();
//			int omit3Int = list.get(0).getOmit3();
//
//			if (omit1Int == -1) {
//				omit1Int = 0;
//			}
//			if (omit2Int == -1) {
//				omit2Int = 0;
//			}
//			if (omit3Int == -1) {
//				omit3Int = 0;
//			}
//
//			sumOmit123 = omit1Int + omit2Int + omit3Int;
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return sumOmit123;
//
//	}

	// ������е�����
//	public int deleteAll() {
//		int count = 0; // ���ܷ���ֵ
//		// SQL���
//		String preparedSql = "delete from " + TABLENAME;
//
//		Object[] param = {};
//
//		count = dbHelp.executeSQL(preparedSql, param);
//		return count;
//	}

	//---------------------------------------------------------------------��ҳ��ʼ
//	// ��ѯ������������
//	public int queryCountTest() {
//		int count = 0;
//		try {
//			conn = dbHelp.getConn(); // �õ����ݿ�����
//			// SQL���
//			String preparedSql = "select count(*) totalCount  from "
//					+ TABLENAME;
//			pstmt = conn.prepareStatement(preparedSql); // �õ�PreparedStatement����
//			// pstmt.setInt();
//
//			rs = pstmt.executeQuery(); // ִ��sqlȡ�ý����
//
//			while (rs.next()) {
//				count = rs.getInt("totalCount");
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return count;
//	}

	/*
	 * ��ҳ ��Ҫ���ݣ�1������������2��ÿҳ��ʾ���������ݡ�3����������/ÿҳ��ʾ����������=ҳ����4��PageSize��ÿҳ��ʾ���������ݡ�
	 * TotalRow:����������pageTotalRow����ǰҳ���������� page:��ǰҳ��
	 */

//	/*
//	 * ��ҳ ���룺pageTotalRow����ǰҳ����������PageSize:ÿҳ��ʾ���������ݡ�
//	 */
//	public List<Lottery> findPageByPageTotalRowAndPageTest(Integer pageTotalRow,Integer pageSize) {
//		List list = new ArrayList(); // ������������б�
//		try {
//			conn = dbHelp.getConn();
//			// ���·�����ҳ����hql�ǲ��еģ�hql��֧��top,Ҫʹ��sql
//			StringBuffer sbSql = new StringBuffer();
//			sbSql.append("select top" + " " +pageSize + " " + "*");
//			sbSql.append(" " + "from " +TABLENAME);
//			sbSql.append(" " + "where id not in");
//			sbSql.append("(select top " +pageTotalRow);
//			sbSql.append(" " + "id from " +TABLENAME);
//			sbSql.append(" " + "order by id asc) order by id asc");
//			
//			stmt = conn.createStatement(
//					SQLServerResultSet.TYPE_SS_SERVER_CURSOR_FORWARD_ONLY,
//					ResultSet.CONCUR_READ_ONLY);
//			rs = stmt.executeQuery(sbSql.toString()); // ִ��sqlȡ�ý����
//
//			/* ѭ�����ظ���Ϣ��װ��List */
//			while (rs.next()) {
//				Lottery lottery = new Lottery(); // �ظ�����
//				lottery.setId(rs.getInt("id"));
//				lottery.setSerialnumber(rs.getString("serialnumber"));
//				lottery.setHundreds(rs.getString("hundreds"));
//				lottery.setDecade(rs.getString("decade"));
//				lottery.setSingledigit(rs.getString("singledigit"));
//
//				lottery.setOmit1record(rs.getString("omit1record"));
//				lottery.setOmit1(rs.getInt("omit1"));
//				lottery.setOmit1state(rs.getInt("omit1state"));
//
//				lottery.setOmit2record(rs.getString("omit2record"));
//				lottery.setOmit2(rs.getInt("omit2"));
//				lottery.setOmit2state(rs.getInt("omit2state"));
//
//				lottery.setOmit3record(rs.getString("omit3record"));
//				lottery.setOmit3(rs.getInt("omit3"));
//				lottery.setOmit3state(rs.getInt("omit3state"));
//
//				lottery.setOmitnewrecord(rs.getString("omitnewrecord"));
//				lottery.setOmitnew(rs.getInt("omitnew"));
//				lottery.setOmitnewstate(rs.getInt("omitnewstate"));
//
//				lottery.setOmitsum(rs.getInt("omitsum"));
//				lottery.setComparecount(rs.getInt("comparecount"));
//				lottery.setDateAndTime(rs.getDate("dateAndTime"));
//				list.add(lottery);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace(); // �����쳣
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//		return list;
//	}
    //-------------------------------------------------------------------------------------��ҳ����
	
	// �������´���list
//	public String udapteBatch(List<Lottery> list) throws SQLException,
//			ClassNotFoundException {
//		Connection con = null;
//		PreparedStatement ps = null;
//		int i = 1;
//		String msg = "";
//		try {
//			con = dbHelp.getConn();
//			// ���浱ǰ�Զ��ύģʽ
//			// boolean autoCommit=con.getAutoCommit();
//			con.setAutoCommit(false);
//			String sql = "update " + TABLENAME + " set "
//					+ "omit1record=?,omit1=?,omit1state=?,"
//					+ "omit2record=?,omit2=?,omit2state=?,"
//					+ "omit3record=?,omit3=?,omit3state=?,"
//					+ "omitnewrecord=?,omitnew=?,omitnewstate=?,"
//					+ "omitsum=? " + " where id=?";
//			ps = con.prepareStatement(sql);
//			// System.out.println(dateAndTime);
//
//			for (i = 0; i < list.size(); i++) {
//				ps.setString(1, list.get(i).getOmit1record());
//				ps.setInt(2, list.get(i).getOmit1());
//				ps.setInt(3, list.get(i).getOmit1state());
//				ps.setString(4, list.get(i).getOmit2record());
//				ps.setInt(5, list.get(i).getOmit2());
//				ps.setInt(6, list.get(i).getOmit2state());
//				ps.setString(7, list.get(i).getOmit3record());
//				ps.setInt(8, list.get(i).getOmit3());
//				ps.setInt(9, list.get(i).getOmit3state());
//				ps.setString(10, list.get(i).getOmitnewrecord());
//				ps.setInt(11, list.get(i).getOmitnew());
//				ps.setInt(12, list.get(i).getOmitnewstate());
//				ps.setInt(13, list.get(i).getOmitsum());
//				ps.setInt(14, list.get(i).getId());
//
//				ps.addBatch();
//				msg = "һ����-" + i + "-�����ݶԱ����! \n";
//
//				if (i % 20000 == 0) {
//					ps.executeBatch();
//				}
//				ps.executeBatch();
//			}
//			// System.out.println(dateAndTime);
//			con.commit();
//			// con.setAutoCommit(autoCommit);
//			return msg;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			conn.rollback();
//			return null;
//		} finally {
//			closeAll(conn, pstmt, rs);
//			dbHelp.closeAll(conn, pstmt, rs);
//		}
//	}

	
	/*
	 * �ͷ���Դ @param conn ���ݿ����� @param pstmt PreparedStatement���� @param rs �����
	 */
//	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
//		// �ر���ԴӦ�ôӺ���ǰ��
//		/* ���rs���գ��ر�rs */
//		if (rs != null) {
//			try {
//				rs.close();
//				// System.err.println("�ر�rs");
//			} catch (SQLException e) {
//				// ����쳣
//				e.printStackTrace();
//			}
//			/* ���pstmt���գ��ر�pstmt */
//			if (pstmt != null) {
//				try {
//					pstmt.close();
//				} catch (Exception e) {
//					// ����쳣
//					e.printStackTrace();
//				}
//			}
//			/* ���conn���գ��ر�conn */
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (Exception e) {
//					// ����쳣
//					e.printStackTrace();
//				}
//			}
//
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	// ����ID��������
	public static void main(String[] args) {
		//ProxyServerDAO lotteryDAO = new ProxyServerDAO();
		// System.out.println("---------------------->:"+lotteryDAO.queryCount());
	}


	


	

}
