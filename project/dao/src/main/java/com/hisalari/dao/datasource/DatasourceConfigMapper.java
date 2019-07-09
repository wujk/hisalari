package com.hisalari.dao.datasource;

import com.hisalari.model.datasource.DatasourceConfig;

import java.util.List;

public interface DatasourceConfigMapper {

	public void createDataBase(String dataBaseName);

	public void createOriginalTable();

	public Integer deleteByPrimaryKey(String uid);

	public Integer insert(DatasourceConfig record);

	public Integer insertSelective(DatasourceConfig record);

	public DatasourceConfig selectByPrimaryKey(String uid);

	public Integer updateByPrimaryKeySelective(DatasourceConfig record);

	public Integer updateByPrimaryKey(DatasourceConfig record);

	public List<DatasourceConfig> selectDatasourceConfigs();

	public void batchInsert(List<DatasourceConfig> datasourceConfigs);

	public List<String> selectValidSource();

	public Integer getCount();

}