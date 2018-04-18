package com.edmProxy.entity;

import java.util.Date;

//ͳ��ʵ��
public class StatisticsEntity {
	private int id;
	private int sendTaskId;//��������id
	private int openCount;//�򿪴���
	private int clickeCount;//�������
	private String receiveIds;//����Id��id1��id2��
	private Date createDate;//����������
	private String  remark;
	
	public StatisticsEntity() {
		super();
	}

	public StatisticsEntity(int id, int sendTaskId, int openCount,
			int clickeCount, String receiveIds, Date createDate, String remark) {
		super();
		this.id = id;
		this.sendTaskId = sendTaskId;
		this.openCount = openCount;
		this.clickeCount = clickeCount;
		this.receiveIds = receiveIds;
		this.createDate = createDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSendTaskId() {
		return sendTaskId;
	}

	public void setSendTaskId(int sendTaskId) {
		this.sendTaskId = sendTaskId;
	}

	public int getOpenCount() {
		return openCount;
	}

	public void setOpenCount(int openCount) {
		this.openCount = openCount;
	}

	public int getClickeCount() {
		return clickeCount;
	}

	public void setClickeCount(int clickeCount) {
		this.clickeCount = clickeCount;
	}

	public String getReceiveIds() {
		return receiveIds;
	}

	public void setReceiveIds(String receiveIds) {
		this.receiveIds = receiveIds;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
