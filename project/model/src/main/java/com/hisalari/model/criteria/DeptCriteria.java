package com.hisalari.model.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DeptCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uid;
	private String name;
	private String phone;
	private String sex;
	private String deptName;
	private String position;
	private String deptUid;
	private String companyUid;
	private Integer isDelete;

	private List<String> list = new ArrayList<String>();
	/**
	 * 分页使用
	 */
	private Integer pageSize = 10;
	private Integer pageNumber = 1;
	private Integer first = 0;
	private Integer size = 15;

	private List<String> cities = new ArrayList<String>();

	private List<Integer> staffStatus = new LinkedList<>();

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getCompanyUid() {
		return companyUid;
	}

	public void setCompanyUid(String companyUid) {
		this.companyUid = companyUid;
	}

	public String getDeptUid() {
		return deptUid;
	}

	public void setDeptUid(String deptUid) {
		this.deptUid = deptUid;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getFirst() {
		return first;
	}

	public void setFirst(Integer first) {
		this.first = first;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public List<Integer> getStaffStatus() {
		return staffStatus;
	}

	public void setStaffStatus(List<Integer> staffStatus) {
		this.staffStatus = staffStatus;
	}
}
