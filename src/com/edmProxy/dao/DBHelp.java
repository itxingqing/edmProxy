package com.edmProxy.dao;

/*
 * ����������
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

import com.edmProxy.util.ReadDBPrpperties;

//import com.lottery.util.ReadPrpperties;



public class DBHelp {
	//���峣���������ݿ����������һ���ַ������������������ݿ��������һ���ַ��������ݿ��û��������һ���ַ��������ݿ����붨���һ���ַ���
    private  static String DRIVER="";   //���ݿ�����
    private  static String URL="";       //
    private  static String USER="";    //���ݿ��û���
    private  static String PASSWORD=""; //���ݿ�����
    
    
    
    public ResultSet rs=null;            //��������
    
    public Connection conn=null;
    
    
    public PreparedStatement pstmt=null;
    
    public ReadDBPrpperties readPrpperties=new ReadDBPrpperties();
    
    /*
     * �õ����ݿ�����
     * @throws classNotFoundException
     * @throws SQLException
     * @return ���ݿ�����
     */
    public Connection getConn() throws ClassNotFoundException, SQLException{
    	try {
    		/*
    		//��Ŀ·��
            String pathString=DBHelp.class.getClassLoader().getResource("").toString();
	    	//pathString=pathString.substring(6, pathString.length());//���������ȡ�����ļ���Ϣ
	    	pathString=pathString.substring(6, pathString.length()-4);//���Զ�ȡ�����ļ���Ϣ
	    	pathString=pathString+"resource/db.properties";     
	    	
	    	//System.out.println("---------"+pathString);
    		
			Properties props=new Properties();
			
			//����·���¼��������ļ�
			File file=new File(pathString);
			InputStreamReader in=new InputStreamReader(new FileInputStream(file),"gbk");
			*/

			DRIVER=readPrpperties.getDRIVER();
			URL=readPrpperties.getURL();
			USER=readPrpperties.getUSER();
			PASSWORD=readPrpperties.getPASSWORD();
			Class.forName(DRIVER);
			
			//Connection conn=DriverManager.getConnection(URL,USER,PASSWORD);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);
    		//System.out.println("�������ݿ�ɹ�.................");
    		return conn;    //��������	
			
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    		
    }
    
    
    
    /*
     * �ͷ���Դ
     * @param conn ���ݿ�����
     * @param pstmt PreparedStatement����
     * @param rs �����
     */
    public static void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
    	//�ر���ԴӦ�ôӺ���ǰ��
    	/*���rs���գ��ر�rs*/
    	if(rs!=null){
    		try{
    			rs.close();
    		}
    		catch(SQLException e){
    				//����쳣
    				e.printStackTrace();
    			}
    		/*���pstmt���գ��ر�pstmt*/
    		if(pstmt!=null){
    			try {
					pstmt.close();
				} catch (Exception e) {
					//����쳣
					e.printStackTrace();
				}
    		}
    		/*���conn���գ��ر�conn*/
    		if(conn!=null){
    			try {
					conn.close();
				} catch (Exception e) {
					//����쳣
					e.printStackTrace();
				}
    		}
    		}
    	}
	
    /**
     * ִ��SQL��䣬���Խ�������ɾ���ĵĲ��������ܽ��в�ѯ
     * @param sql Ԥ�����sql���
     * @param param Ԥ�����sql����еġ��������������ַ�������
     * @return Ӱ�������
     */
    public int executeSQL(String preparedSql,Object[] param){
    	conn=null;
    	PreparedStatement pstmt=null;
    	ResultSet rs=null;
    	int num=0;
    	/*����SQL,ִ��SQL*/
    	try {
			conn=getConn();       //�õ����ݿ�����
			pstmt=conn.prepareStatement(preparedSql);
			if(pstmt!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);  //���ò���   ����Object����Ϊ�����int���;Ͳ��������Ͳ�ͬ�Ĵ���
				}
			}
			pstmt.executeUpdate();     //ִ��SQL���
			rs=pstmt.getGeneratedKeys(); //�õ��ղ��������id
			if(rs.next()){
				num=rs.getInt(1);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();   //����ClassNotFoundException�쳣
		} catch (SQLException e){
			e.printStackTrace();    //����SQLException�쳣
		}finally{
			closeAll(conn, pstmt, rs);      //�ͷ���Դ
		}
		return num;
    }
    
    /**
     * ִ��SQL��䣬���в�ѯ
     * @param sql Ԥ�����sql���
     * @param param Ԥ�����sql����еġ��������������ַ�������
     * @return ResultSet�����
     */
    public ResultSet executeQuerySQL(String preparedSql,Object[] param){
    	Connection conn=null;
    	pstmt=null;
    	/*����SQL,ִ��SQL*/
    	try {
			conn=getConn();       //�õ����ݿ�����
			pstmt=conn.prepareStatement(preparedSql);
			if(pstmt!=null){
				for(int i=0;i<param.length;i++){
					pstmt.setObject(i+1, param[i]);  //���ò���   ����Object����Ϊ�����int���;Ͳ��������Ͳ�ͬ�Ĵ���	
				}
			}
			rs=pstmt.executeQuery();     //ִ��SQL���
		} catch (ClassNotFoundException e) {
			e.printStackTrace();   //����ClassNotFoundException�쳣
		} catch (SQLException e){
			e.printStackTrace();    //����SQLException�쳣
		}finally{
			closeAll(conn, pstmt, rs);      //�ͷ���Դ
		}
		return rs;
    }
    
    
    
    
    //���Դ���
    public static void main(String[] args) throws Exception {
    	DBHelp dBHelp=new DBHelp();
    	System.out.println(dBHelp.getConn());
	}
}