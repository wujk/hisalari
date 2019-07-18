package com.hisalari.db;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.hisalari.dao.datasource.DatasourceConfigMapper;
import com.hisalari.db.interfaces.DataBaseManager;
import com.hisalari.db.interfaces.MapperInterface;
import com.hisalari.model.datasource.DatasourceConfig;
import com.hisalari.spring.SpringContextUtils;
import com.hisalari.util.obj.ObjectUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatais多数据源
 */
@Component
public class MybatisMutiManager extends DataBaseManager<SqlSessionFactory, SqlSession> implements MapperInterface {
    private Logger logger = LoggerFactory.getLogger(DataBaseManager.class);

    @Autowired
    protected SqlSessionFactory sqlSessionFactory;

    @Autowired
    protected SpringContextUtils springContextUtils;

    @PostConstruct
    public void loadAllDB() {
        storageDatasource();
    }

    @Override
    public List<DataBase> findDatabase() {
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
            session.commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            session.rollback();
        } finally {
            session.close();
            return dataBases;
        }
    }

    @Override
    public DataBase findDatabaseById() {
        return null;
    }

    private SqlSessionFactory getSqlSessionFactory(DataSource dataSource) throws Exception {
        List<Interceptor> interceptors = new ArrayList<Interceptor>();
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:mapper/*/*.xml");
        factoryBean.setMapperLocations(resources);
        if (!springContextUtils.isProdEnv()) {
            Resource resource = resourcePatternResolver.getResource("classpath:mybatis-config.xml");
            factoryBean.setConfigLocation(resource);
        }
        factoryBean.setTypeHandlersPackage("com.hisalari.dao.*.*");
        factoryBean.setDataSource(dataSource);
        if (interceptors.size() > 0) {
            Interceptor[] plugins = new Interceptor[interceptors.size()];
            interceptors.toArray(plugins);
            factoryBean.setPlugins(plugins);
        }
        return factoryBean.getObject();
    }

    @Override
    public <M> M getMapper(Class<M> clazz, String dataBaseId) {
        SqlSession session = getCurrentSqlSession(dataBaseId);
        try {
            if (session == null || session.getConnection().isClosed()) {
                SqlSessionFactory sessionFactory = getSqlSessionFactory(dataBaseId);
                setCurrentSqlSession(dataBaseId, sessionFactory.openSession());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return getCurrentSqlSession(dataBaseId).getMapper(clazz);
    }

    @Override
    public SqlSessionFactory createSessionFactory(DataSource dataSource) throws Exception {
        return getSqlSessionFactory(dataSource);
    }

    @Override
    public SqlSessionFactory getBaseSessionFactory() {
        return sqlSessionFactory;
    }

    @Override
    public void closeSessionFactory(SqlSessionFactory sessionFactory) {
        DruidXADataSource source = (DruidXADataSource) sessionFactory.getConfiguration().getEnvironment().getDataSource();
        logger.info("activeCount:" + source.getActiveCount());
        if (source != null && source.getActiveCount() <= 0) {
            logger.info("删除数据源：" + source.getUrl());
            source.close();
            source = null;
        }
    }

    @Override
    public Object getDataSourceFromFactory(DataBase dataBase) {
        return ((SqlSessionFactory)getSqlSessionFactory(dataBase.getDataBaseId())).getConfiguration().getEnvironment().getDataSource();
    }

    @Override
    public Object createDataSource(DataBase dataBase) throws SQLException {
        DruidXADataSource dataSource = new DruidXADataSource();
        dataSource.setUrl(dataBase.getUrl());
        dataSource.setUsername(dataBase.getUserName());
        dataSource.setPassword(dataBase.getPassword());
        dataSource.setInitialSize(ObjectUtil.getValue(initialSize, 10));
        dataSource.setMinIdle(ObjectUtil.getValue(minIdle, 50));
        dataSource.setMaxActive(ObjectUtil.getValue(maxActive, 50));
        dataSource.setMaxWait(ObjectUtil.getValue(maxWait, 60000));
        dataSource.setTimeBetweenEvictionRunsMillis(ObjectUtil.getValue(timeBetweenEvictionRunsMillis, 60000));
        dataSource.setMinEvictableIdleTimeMillis(ObjectUtil.getValue(minEvictableIdleTimeMillis, 300000));
        dataSource.setValidationQuery(ObjectUtil.getValue(validationQuery, "SELECT 'x'"));
        dataSource.setTestWhileIdle(ObjectUtil.getValue(testWhileIdle, true));
        dataSource.setTestOnBorrow(ObjectUtil.getValue(testOnBorrow, false));
        dataSource.setTestOnReturn(ObjectUtil.getValue(testOnReturn, false));
        dataSource.setPoolPreparedStatements(ObjectUtil.getValue(poolPreparedStatements, true));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(
                ObjectUtil.getValue(maxPoolPreparedStatementPerConnectionSize, 20));
        dataSource.setConnectionErrorRetryAttempts(ObjectUtil.getValue(connectionErrorRetryAttempts, 3));
        dataSource.setBreakAfterAcquireFailure(ObjectUtil.getValue(breakAfterAcquireFailure, true));
        dataSource.setDefaultAutoCommit(false);
        dataSource.setRemoveAbandoned(ObjectUtil.getValue(removeAbandoned, false));
        dataSource.setLogAbandoned(ObjectUtil.getValue(logAbandoned, true));
        dataSource.setRemoveAbandonedTimeout(ObjectUtil.getValue(removeAbandonedTimeout, 1800));
        dataSource.setFilters(ObjectUtil.getValue(filters, "stat"));
        return dataSource;
    }

    @Value("#{prop['db.initialSize']}")
    private Integer initialSize;

    @Value("#{prop['db.minIdle']}")
    private Integer minIdle;

    @Value("#{prop['db.maxActive']}")
    private Integer maxActive;

    @Value("#{prop['db.maxWait']}")
    private Integer maxWait;

    @Value("#{prop['db.timeBetweenEvictionRunsMillis']}")
    private Long timeBetweenEvictionRunsMillis;

    @Value("#{prop['db.minEvictableIdleTimeMillis']}")
    private Long minEvictableIdleTimeMillis;

    @Value("#{prop['db.validationQuery']}")
    private String validationQuery;

    @Value("#{prop['db.testWhileIdle']}")
    private Boolean testWhileIdle;

    @Value("#{prop['db.testOnBorrow']}")
    private Boolean testOnBorrow;

    @Value("#{prop['db.testOnReturn']}")
    private Boolean testOnReturn;

    @Value("#{prop['db.poolPreparedStatements']}")
    private Boolean poolPreparedStatements;

    @Value("#{prop['db.maxPoolPreparedStatementPerConnectionSize']}")
    private Integer maxPoolPreparedStatementPerConnectionSize;

    @Value("#{prop['db.connectionErrorRetryAttempts']}")
    private Integer connectionErrorRetryAttempts;

    @Value("#{prop['db.breakAfterAcquireFailure']}")
    private Boolean breakAfterAcquireFailure;

    @Value("#{prop['db.removeAbandoned']}")
    private Boolean removeAbandoned;

    @Value("#{prop['db.logAbandoned']}")
    private Boolean logAbandoned;

    @Value("#{prop['db.removeAbandonedTimeout']}")
    private Integer removeAbandonedTimeout;

    @Value("#{prop['db.filters']}")
    private String filters;

}
