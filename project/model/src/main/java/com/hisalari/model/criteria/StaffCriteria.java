package com.hisalari.model.criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StaffCriteria implements Serializable {
	private static final long serialVersionUID = -37586871156928260L;

	private String staffUid;

	private String recordUid;

	private Integer first = 0;

	private Integer size = 15;

	private List<String> deptUids = new LinkedList<>();

	private String companyUid;

	private Integer gender;

	private String name;

	private String mobile;

	private String jobNumber;

	private Integer minAge;

	private Integer maxAge;

	private Integer pageSize = 10;

	private Integer pageNumber = 1;

	private List<String> podoProvinces = new LinkedList<>();

	private String province;

	private List<Integer> staffStatus = new LinkedList<>();

	private String key;

	private String deptName;

	private String position;

	private Integer age;

	private Boolean isLeave = false;

	private Boolean isDefault = false;

	private String projectUid;

	private List<String> typeOfDemicile = new LinkedList<>();

	private Boolean isConfig;

	private Boolean noEntryIsConfig;
	
	private String startTime;
	
	private List<String> staffRecordId = new ArrayList<String>();
	
	private String leaveOfficeStatus;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLeaveOfficeStatus() {
		return leaveOfficeStatus;
	}

	public void setLeaveOfficeStatus(String leaveOfficeStatus) {
		this.leaveOfficeStatus = leaveOfficeStatus;
	}

	public List<String> getStaffRecordId() {
		return staffRecordId;
	}

	public void setStaffRecordId(List<String> staffRecordId) {
		this.staffRecordId = staffRecordId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getStaffUid() {
		return staffUid;
	}

	public void setStaffUid(String staffUid) {
		this.staffUid = staffUid;
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

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String getCompanyUid() {
		return companyUid;
	}

	public void setCompanyUid(String companyUid) {
		this.companyUid = companyUid;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getDeptUids() {
		return deptUids;
	}

	public void setDeptUids(List<String> deptUids) {
		this.deptUids = deptUids;
	}

	public List<String> getPodoProvinces() {
		return podoProvinces;
	}

	public void setPodoProvinces(List<String> podoProvinces) {
		this.podoProvinces = podoProvinces;
	}

	public List<Integer> getStaffStatus() {
		return staffStatus;
	}

	public void setStaffStatus(List<Integer> staffStatus) {
		this.staffStatus = staffStatus;
	}

	public List<String> getTypeOfDemicile() {
		return typeOfDemicile;
	}

	public void setTypeOfDemicile(List<String> typeOfDemicile) {
		this.typeOfDemicile = typeOfDemicile;
	}

	public Boolean getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(Boolean isLeave) {
		this.isLeave = isLeave;
	}

	public String getRecordUid() {
		return recordUid;
	}

	public void setRecordUid(String recordUid) {
		this.recordUid = recordUid;
	}

	public Boolean getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(Boolean isConfig) {
		this.isConfig = isConfig;
	}

	public Boolean getNoEntryIsConfig() {
		return noEntryIsConfig;
	}

	public void setNoEntryIsConfig(Boolean noEntryIsConfig) {
		this.noEntryIsConfig = noEntryIsConfig;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getProjectUid() {
		return projectUid;
	}

	public void setProjectUid(String projectUid) {
		this.projectUid = projectUid;
	}
}
