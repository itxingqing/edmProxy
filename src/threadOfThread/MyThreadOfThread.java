package threadOfThread;

//���߳������ö��߳�����
public class MyThreadOfThread {

	public static void main(String[] args) {
		StartTaskIP startTaskIP=new StartTaskIP();
		Runnable r=new MyThreadIP(startTaskIP);
		Thread t=new Thread(r);
		t.start();
	}
}

//�߳��࣬����������Ip
class MyThreadIP implements Runnable{
	private StartTaskIP startTaskIP;
	public MyThreadIP(StartTaskIP startTaskIP){
		this.startTaskIP=startTaskIP;
	}
	public void run() {
		startTaskIP.startIP();
	}
}
//����ʵ���� IP
class  StartTaskIP{
	public synchronized void startIP(){
		//System.out.println("��ʼIP");
		//�����˺��߳�
		StartTaskAcc startTaskAcc=new StartTaskAcc();
		for (int i = 0; i < 5; i++) {
			System.out.println("����"+i+"����IP");
			Runnable r=new MyThreadAcc(i,7,startTaskAcc);
			Thread t=new Thread(r);
			t.start();
		}
		
		
	}
}
//�߳��࣬�����������˺�
class MyThreadAcc implements Runnable{
	private int num;
	private StartTaskAcc startTaskAcc;
	private int accNum;
	public MyThreadAcc(int num,int accNum,StartTaskAcc startTaskAcc){
		this.num=num;
		this.startTaskAcc=startTaskAcc;
		this.accNum=accNum;
	}
	public void run() {
		startTaskAcc.startAcc(num,accNum);
	}
}
//����ʵ���� �˺�
class  StartTaskAcc{
	public synchronized void startAcc(int num,int accNum){
		//System.out.println("��ʼ�˺�");
		for (int i = 0; i < accNum; i++) {
			System.out.println("������"+num+"��ip����"+i+"���˺�");
		}
		
	}
}

