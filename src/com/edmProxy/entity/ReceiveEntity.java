package com.edmProxy.entity;

import java.util.Date;

//���յ�ַ
public class ReceiveEntity {
	private int id;
	private String receive;//���յ�ַ
	private String post;//�ʾ�
	private int sendCount;//���ʹ���
	private Date createDate;//����ʱ��
	private Date lastSendDate;//���һ�η���ʱ��
	private String remark;

	public ReceiveEntity() {
		super();
	}

	public ReceiveEntity(int id, String receive, String post, int sendCount,
			Date createDate, Date lastSendDate, String remark) {
		super();
		this.id = id;
		this.receive = receive;
		this.post = post;
		this.sendCount = sendCount;
		this.createDate = createDate;
		this.lastSendDate = lastSendDate;
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
