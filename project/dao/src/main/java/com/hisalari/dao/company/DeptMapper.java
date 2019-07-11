package com.hisalari.dao.company;

import com.hisalari.model.comapny.Dept;
import com.hisalari.model.criteria.DeptCriteria;
import com.hisalari.model.criteria.StaffCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeptMapper {

    int deleteByPrimaryKey(String uid);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);

    int updateByCompanyName(Dept dept);

    Dept queryCompanyDept(String companyid);

    List<Dept> queryValidCompanyDept(String companyUid);

    Dept queryChildren(String uid);

    Dept queryCompanyDeptStaff(String companyid);

    List<Dept> queryDeptParentId(String uid);

    List<Map<String, String>> selectDeptStaff(DeptCriteria deptCriteria);

    List<Map<String, String>> selectDeptStaffs(DeptCriteria deptCriteria);

    List<Map<String, String>> selectDeptTransferStaffs(DeptCriteria deptCriteria);

    List<Map<String, String>> selectStaffsByDeptUid(String deptUid);

    int deleteByUid(@Param("list") List<String> list);

    public String queryDeptParentIdByDeptUid(String deptUid);

    public List<Dept> queryDeptFunUid(String accountUid, String funUid, String companyUid);

    Integer selectDeptStaffCount(DeptCriteria deptCriteria);

    Integer deptCount(@Param("companyUid") String companyUid);

    Integer staffCount(DeptCriteria deptCriteria);

    Integer insertOrUpdate(Dept dept);

    List<Dept> queryAllDept(String companyUid);

    int batchInsert(List<Dept> idepts);

    int batchUpdate(List<Dept> idepts);

    Integer selectEntryStaffCount(List<String> list);

    Integer selectLeveStaffCount(List<String> list);

    Integer selectTotalStaffCount(List<String> list);

    Map<String, String> selectPrincipal(String deptUid);

    List<Map<String, Object>> selectByCompanyUidAndKey(@Param("companyUid") String companyUid,
                                                       @Param("key") String key);

    Dept selectUidByStaffRecourdUid(@Param("uid") String uid, @Param("staffRecordUid") String staffRecordUid);

    List<Map<String, Object>> selectByCompanyUidAndDeptUid(@Param("companyUid") String companyUid,
                                                           @Param("deptUid") String deptUid);

    List<Map<String, Object>> totalStaff(List<String> list);

    Dept findChildDept(@Param("uid") String uid);

    List<Map<String, Object>> queryStaffByName(Map<String, Object> map);

    List<Map<String, Object>> queryDeptByName(Map<String, Object> map);

    List<Map<String, Object>> queryTaskStaff(StaffCriteria deptCriteria);

    Integer countTaskStaff(StaffCriteria deptCriteria);

    List<Map<String, Object>> getParentInfo(String uid);

    Map<String, String> queryFullName(String phone);

    Integer countReportJobNumber(StaffCriteria criteria);

    List<Map<String, Object>> selectReportJobNumber(StaffCriteria criteria);

    List<Dept> queryAllValidDept(String companyUid);

    List<String> queryAllDeptCode(String companyUid);

    Integer mergeDepts(Map<String, Object> param);

    Integer updateStaffCommon(Map<String, Object> param);

    List<Integer> getSortByParentId(@Param("parentId") String parentId);

	Dept selectTopDept();

	Dept queryDeptByCode(String deptCode);

	Dept queryParent();

	int updateDeptReport(@Param("staffRecordUid") String staffRecordUid);

	List<Dept> selectDeptAll();

	List<Map<String, String>> selectDepts();

	List<Map<String, String>> selectDeptStaffByYearMonth(@Param("starYearMonth") String starYearMonth, @Param("endYearMonth") String endYearMonth);

	Dept selectDeptByParentId();

	void insertBatch(List<Dept> list);

	List<Dept> selectDeptByNotDelete();

	String selectCompanyDeptUid();
}