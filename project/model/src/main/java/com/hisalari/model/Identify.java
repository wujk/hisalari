package com.hisalari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Identify {

	private String uid;

	private String companyUid;

	private String accountUid;

	@JsonIgnore
	private Long createTime;

	@JsonIgnore
	private String createUser;

	@JsonIgnore
	private Long updateTime;

	@JsonIgnore
	private String updateUser;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getCompanyUid() {
		return companyUid;
	}

	public void setCompanyUid(String companyUid) {
		this.companyUid = companyUid;
	}

	public String getAccountUid() {
		return accountUid;
	}

	public void setAccountUid(String accountUid) {
		this.accountUid = accountUid;
	}

}
