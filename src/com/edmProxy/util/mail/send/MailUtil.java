package com.edmProxy.util.mail.send;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 *�����ʼ�������
 *1����ȡ�����ʼ���ַ
 */
public class MailUtil {
	
	//��ȡ�����ʼ���ַ����ȡtxt�ļ��� ��Txt�еĵ�ַ��ȡ����뼯�Ϸ���
	//���ǲ��ԣ��ŵ������У���ʵ��Ҫ�ŵ����ݿ��е�
	public static ArrayList readTxt() throws IOException
	{
		//��str��������ݰ�,����ŵ������У���������û������ַ����
	    ArrayList receiveMailList=new ArrayList();
	    //����������10000000���ַ������� 
	    char data[]=new char[10000000]; 
	    FileInputStream fileInputStream = new FileInputStream("C:\\2011-10-31x1\\mailAddress.txt");//��ȡģ���ļ��л�Ӣ�ż���.txt
	    InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
	    // ��������bufferedReader 
	    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	    String tempStr=""; //��Ŷ�ȡ�������ʼ���ַ����
	    while ((tempStr = bufferedReader.readLine()) != null)
	    {
	    	//�����ʼ���ַ������    
	    	receiveMailList.add(tempStr);
	    }
	    //�ر��������Ͷ�ȡ��
	    fileInputStream.close();
	    bufferedReader.close(); 
	    
	    return receiveMailList;
	}
	//��ȡģ�壬����·��
	 public static String readTemplateMail(String filePath) {
		 String str = "";
         //long beginDate = (new Date()).getTime();
         try {
                 String tempStr = "";
                 FileInputStream is = new FileInputStream(filePath);//��ȡģ���ļ�
                 BufferedReader br = new BufferedReader(new InputStreamReader(is));
                 while ((tempStr = br.readLine()) != null)
                 str = str + tempStr ;
                 is.close();
         } catch (IOException e) {
                 e.printStackTrace();
                 return str;
         }
         return str;
         
         //���³�����ʱû���õ����õ����е�title��content��editer����ʼ�ͽ�����ʱ��
         //�滻ģ���еı��⣬���ݺ�����
 /*      try {
          
   str = str.replaceAll("###title###",
       title);
   str = str.replaceAll("###content###",
       context);
   str = str.replaceAll("###author###",
       editer);//�滻��ģ������Ӧ�ĵط�
   
               //File f = new File(HtmlFile);
               //BufferedWriter o = new BufferedWriter(new FileWriter(f));
               //o.write(str);
               //o.close();
               System.out.println("����ʱ��" + ((new Date()).getTime() - beginDate) + "ms");
       } catch (IOException e) {
               e.printStackTrace();
               return false;
       }
	 }
	 */
	 }
}
