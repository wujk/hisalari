package com.hisalari.db;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.hisalari.dao.datasource.DatasourceConfigMapper;
import com.hisalari.db.interfaces.DataBaseManager;
import com.hisalari.model.datasource.DatasourceConfig;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * mybatais多数据源
 */
@Component
public class MybatisMutiXAManager extends MybatisMutiManager {
    private Logger logger = LoggerFactory.getLogger(DataBaseManager.class);

    @Resource(name = "atomikosJta")
    private PlatformTransactionManager platformTransactionManager;

    @Override
    public List<DataBase> findDatabase() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus ts = platformTransactionManager.getTransaction(def);
        List<DataBase> dataBases = new ArrayList<>();
        SqlSession session = null;
        try{
            DatasourceConfigMapper datasourceConfigMapper = getMapper(DatasourceConfigMapper.class, MybatisMutiXAManager.BASE_DB_ID);
            List<DatasourceConfig> datasourceConfigs = datasourceConfigMapper.selectDatasourceConfigs();
            for (DatasourceConfig datasourceConfig: datasourceConfigs) {
                DataBase dataBase = new DataBase();
                dataBase.setDataBaseId(datasourceConfig.getUid());
                dataBase.setUrl(datasourceConfig.getUrl());
                dataBase.setUserName(datasourceConfig.getUsername());
                dataBase.setPassword(datasourceConfig.getPassword());
                dataBases.add(dataBase);
            }
            session = getCurrentSqlSession(MybatisMutiXAManager.BASE_DB_ID);
            platformTransactionManager.commit(ts);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            platformTransactionManager.rollback(ts);
        } finally {
           session.close();
           return dataBases;
        }

    }

    @Override
    public DataSource createDataSource(DataBase dataBase) throws SQLException {
        DruidXADataSource druidXADataSource = (DruidXADataSource)super.createDataSource(dataBase);
        druidXADataSource.setRemoveAbandoned(false);
        return createAtomikosDataSourceBean(druidXADataSource);
    }

    public AtomikosDataSourceBean createAtomikosDataSourceBean(XADataSource xaDataSource) {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);
        atomikosDataSourceBean.setUniqueResourceName(UUID.randomUUID().toString());
        return atomikosDataSourceBean;
    }

    @PostConstruct
    public void loadAllDB() {
        storageDatasource();
    }

}
