package com.hisalari.model.comapny;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hisalari.model.Identify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dept extends Identify implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String deptCode;

	private String parentId;

	private Integer level;

	private Integer isDelete = 0;
	
	private Integer sort;

	// 部门描述
	private String description;
	
	@JsonIgnore
	private String funUid;

	private String staffRecordUid;
	
	private String jobNumber;
	
	private String staffName;
	
	private List<Dept> children = new ArrayList<Dept>();

	private List<Map<String, Object>> counts = new ArrayList<Map<String, Object>>();

    private String fullName;

    private String treePath;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public List<Map<String, Object>> getCounts() {
		return counts;
	}

	public void setCounts(List<Map<String, Object>> counts) {
		this.counts = counts;
	}
	
	/**
	 * 仅权限模块使用
	 */
	private Boolean isConfig = false;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(Boolean isConfig) {
		this.isConfig = isConfig;
	}

	public List<Dept> getChildren() {
		return children;
	}

	public void setChildren(List<Dept> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getFunUid() {
		return funUid;
	}

	public void setFunUid(String funUid) {
		this.funUid = funUid;
	}

	public String getStaffRecordUid() {
		return staffRecordUid;
	}

	public void setStaffRecordUid(String staffRecordUid) {
		this.staffRecordUid = staffRecordUid;
	}

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}