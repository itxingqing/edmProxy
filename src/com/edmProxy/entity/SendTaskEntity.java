package com.edmProxy.entity;
import java.util.Date;


//��������-ÿ�������Ӧһ���ʾ�,��Ӧһ������IP���������������Ƕ�Ӧ����ʾ֣���������¶���˺�����
public class SendTaskEntity {
	private int id;
	private String sendTask;//����������
	private String post;//�ʾ�
	private String title;//�����ʼ�����
	private int proxyStart;//�Ƿ����ô���0�����ã�1����
	private String contentPath;//��������·��
	private String accessoryPath;//����·��������1������2��
	private int sendTaskCount;//���ʹ���
	private Date lastsendTaskDate;//�����ʱ��
	private String sendTaskAccounts;//�����˺�(id-�˺�1-���룬)
	private int accountSendNum;//ÿ���˺ŷ�����
	private int accountStartLinks;//�˺���������������IP�£��˺������������
	private String sendTaskReceivesPath;//���յ�ַ(id-���յ�ַ1��)
	private int sendIntervalTime;//���ͼ��ʱ�䣨�룩
	private String remark;
	
	public SendTaskEntity() {
		super();
	}

	public SendTaskEntity(int id, String sendTask, String post, String title,
			int proxyStart, String contentPath, String accessoryPath,
			int sendTaskCount, Date lastsendTaskDate, String sendTaskAccounts,
			int accountSendNum, int accountStartLinks,
			String sendTaskReceivesPath, int sendIntervalTime, String remark) {
		super();
		this.id = id;
		this.sendTask = sendTask;
		this.post = post;
		this.title = title;
		this.proxyStart = proxyStart;
		this.contentPath = contentPath;
		this.accessoryPath = accessoryPath;
		this.sendTaskCount = sendTaskCount;
		this.lastsendTaskDate = lastsendTaskDate;
		this.sendTaskAccounts = sendTaskAccounts;
		this.accountSendNum = accountSendNum;
		this.accountStartLinks = accountStartLinks;
		this.sendTaskReceivesPath = sendTaskReceivesPath;
		this.sendIntervalTime = sendIntervalTime;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSendTask() {
		return sendTask;
	}

	public void setSendTask(String sendTask) {
		this.sendTask = sendTask;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getProxyStart() {
		return proxyStart;
	}

	public void setProxyStart(int proxyStart) {
		this.proxyStart = proxyStart;
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
	}

	public String getAccessoryPath() {
		return accessoryPath;
	}

	public void setAccessoryPath(String accessoryPath) {
		this.accessoryPath = accessoryPath;
	}

	public int getSendTaskCount() {
		return sendTaskCount;
	}

	public void setSendTaskCount(int sendTaskCount) {
		this.sendTaskCount = sendTaskCount;
	}

	public Date getLastsendTaskDate() {
		return lastsendTaskDate;
	}

	public void setLastsendTaskDate(Date lastsendTaskDate) {
		this.lastsendTaskDate = lastsendTaskDate;
	}

	public String getSendTaskAccounts() {
		return sendTaskAccounts;
	}

	public void setSendTaskAccounts(String sendTaskAccounts) {
		this.sendTaskAccounts = sendTaskAccounts;
	}

	public int getAccountSendNum() {
		return accountSendNum;
	}

	public void setAccountSendNum(int accountSendNum) {
		this.accountSendNum = accountSendNum;
	}

	public int getAccountStartLinks() {
		return accountStartLinks;
	}

	public void setAccountStartLinks(int accountStartLinks) {
		this.accountStartLinks = accountStartLinks;
	}

	public String getSendTaskReceivesPath() {
		return sendTaskReceivesPath;
	}

	public void setSendTaskReceivesPath(String sendTaskReceivesPath) {
		this.sendTaskReceivesPath = sendTaskReceivesPath;
	}

	public int getSendIntervalTime() {
		return sendIntervalTime;
	}

	public void setSendIntervalTime(int sendIntervalTime) {
		this.sendIntervalTime = sendIntervalTime;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
