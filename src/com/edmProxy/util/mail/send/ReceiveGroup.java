package com.edmProxy.util.mail.send;

import java.util.ArrayList;



//���黻ip�˺ŷ����ʼ� //һ�η���һ�����Խ�������Ϊ��
//�ȵ���mailListGroup()���飬�ڰѷ���ĺ��list����mailGroupSend����SendMail����
public class ReceiveGroup {
public static void main(String[] args) {
//		GroupSendMail t=new GroupSendMail();
//		ArrayList<ArrayList> mailGroupList=new ArrayList();
//		ArrayList<SendHostMailInfo> sendHostMailInfoListT=new ArrayList<SendHostMailInfo>();
//		TestEntity1 testEntity1=new TestEntity1();
//		testEntity1.crateEntity();
//		ArrayList emailList=new ArrayList();
//		for(int i=0;i<50;i++){
//			emailList.add("huangliang@tarena.com.cn");
//		}
//		mailGroupList=t.mailListGroup(emailList,10,3);
//		
//		
//		sendHostMailInfoListT=testEntity1.sendHostMailInfoList;
//		//System.out.println("sendHostMailInfoListT->"+sendHostMailInfoListT.size());
//		
//		t.mailGroupSend(mailGroupList,sendHostMailInfoListT);
//		//System.out.println("-------------------------------------");
	}
	
	
	
	
	
	
	//����
	//list ������������ whileSendCountChangeIPNum��IP������ ����,ÿһ��һ��IP    ip����
	public  ArrayList<ArrayList> receiveGroup(ArrayList receiveList,int accountSendNum){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //���list��ÿ��listΪһ�飬����ŷ��͵�ַ
		
		int count=0;//����ֻ�IP������Ҳ�Ƿ������
		int receiveSum=receiveList.size();
		count=receiveSum/accountSendNum;
		//System.out.println("count-->"+count);
		
		//new 3�����ϣ���ֵ����sumCountList��
//		if(count<=sumAccount){//������<=ip��
			System.out.println(">---->������ȡ���ݽ�������ʼ����......");
			receiveGroupList=handReceiveGroupList(receiveList,accountSendNum);
//		}else{//������>ip�� ����ip��ַ�����Զ�����
//			System.out.println("�Զ�");
//			receiveGroupList=autoReceiveGroupList(receiveList,sumAccount);
//		}

		//����
//		for (int i = 0; i < mailGroupList.size(); i++) {
//			System.out.println("��"+i+"��IP��-->"+mailGroupList.get(i).size());
//		}
		return receiveGroupList;
	}
	//������������IP������������ 
	private ArrayList<ArrayList> handReceiveGroupList(ArrayList<String> receiveList,int accountSendNum){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //���list��ÿ��listΪһ�飬����ŷ��͵�ַ
		int count=0;//����ֻ�IP������Ҳ�Ƿ������
		int receiveSum=receiveList.size();
		int mode=receiveSum%accountSendNum;
		count=receiveSum/accountSendNum;
		for(int i=1;i<=count;i++){
			ArrayList list=new ArrayList();//�鼯��
			int n=(i-1)*accountSendNum;
			int m=i*accountSendNum;//-difference;
			int j=0;
			for(j=n;j<m;j++){
				list.add(receiveList.get(j));
				//����������ӵ����һ����	
				if(j==count*accountSendNum-1){
					//System.out.println("j->"+j+"==count*whileSendCountChangeIPNum->"+count*whileSendCountChangeIPNum);
					j=j+1; //��Ϊ���ϵ��±��Ǵ�0��ʼ����0�ֲ��ʺϼ��㣬��������j+1��
					for (int k = j; k < receiveList.size(); k++) {
						list.add(receiveList.get(k));
					}
				}
			}
			receiveGroupList.add(list);
		}
		return receiveGroupList;
	}
	
	//�Զ����� ������>ip�� ����ip��ַ�����Զ�����
	private ArrayList<ArrayList> autoReceiveGroupList(ArrayList receiveList,int sumAccount){
		ArrayList<ArrayList> receiveGroupList=new ArrayList(); //���list��ÿ��listΪһ�飬����ŷ��͵�ַ
		int receiveSum=receiveList.size();
		int groupNum=0;//ÿ��������,�������������������������ӵ����һ��
		groupNum=receiveSum/sumAccount;
		int mode=receiveSum%sumAccount; 
//		System.out.println("groupNum-->"+groupNum);
//		System.out.println("mode-->"+mode);
		//count=mailSum/sumIp;
		for(int i=1;i<=sumAccount;i++){
			ArrayList list=new ArrayList();//�鼯��
			
			int n=(i-1)*groupNum;
			int m=i*groupNum;
			for(int j=n;j<m;j++){
				list.add(receiveList.get(j));
				//System.out.println("��"+i+"��,��"+j+"������");
				//System.out.println(j+":"+groupNum);
				if(j==groupNum*sumAccount-1){
					//System.out.println(j+":"+(groupNum*sumIp-1));
					j=j+1; //��Ϊ���ϵ��±��Ǵ�0��ʼ����0�ֲ��ʺϼ��㣬��������j+1��
					for (int k = j; k < receiveList.size(); k++) {
						//System.out.println("k->"+k);
						list.add(receiveList.get(k));
					}
				}
			}
			receiveGroupList.add(list);
		}
		//����
//		for(int i=0;i<mailGroupList.size();i++){
//			System.out.println("mailGroupList.size()-->"+mailGroupList.get(i).size());
//		}
		return receiveGroupList;
	}

	
}
