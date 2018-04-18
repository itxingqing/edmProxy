package com.edmProxy.entity;

import java.util.Date;

//��������ʵ����
public class ProxyEntity {
	private int id;
	private int proxyType;//1��http��2��socket4~5
	private String proxyHost;//��������
	private String proxyPort;//�˿�
	private String proxyAccount;//http�����û���������
	private String proxyPassword;
	private int valid;//0:��Ч,1:��Ч
	private int start;
	private Date createDate;//����������
	private int proxyCount;//�������
	private Date lastProxyDate;//���
	private String remark;
	public ProxyEntity() {
		super();
	}
	public ProxyEntity(int id, int proxyType, String proxyHost,
			String proxyPort, String proxyAccount, String proxyPassword,
			int valid, int start, Date createDate, int proxyCount,
			Date lastProxyDate, String remark) {
		super();
		this.id = id;
		this.proxyType = proxyType;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyAccount = proxyAccount;
		this.proxyPassword = proxyPassword;
		this.valid = valid;
		this.start = start;
		this.createDate = createDate;
		this.proxyCount = proxyCount;
		this.lastProxyDate = lastProxyDate;
		this.remark = remark;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProxyType() {
		return proxyType;
	}
	public void setProxyType(int proxyType) {
		this.proxyType = proxyType;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getProxyAccount() {
		return proxyAccount;
	}
	public void setProxyAccount(String proxyAccount) {
		this.proxyAccount = proxyAccount;
	}
	public String getProxyPassword() {
		return proxyPassword;
	}
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getProxyCount() {
		return proxyCount;
	}
	public void setProxyCount(int proxyCount) {
		this.proxyCount = proxyCount;
	}
	public Date getLastProxyDate() {
		return lastProxyDate;
	}
	public void setLastProxyDate(Date lastProxyDate) {
		this.lastProxyDate = lastProxyDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
