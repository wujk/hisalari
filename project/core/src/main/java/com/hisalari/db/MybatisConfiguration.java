package com.hisalari.db;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.hisalari.properties.PropertiesConfiguration;
import com.hisalari.util.obj.ObjectUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.SQLException;
import java.util.UUID;

@Configuration
@AutoConfigureAfter(PropertiesConfiguration.class)
public class MybatisConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean(value = "druidXADataSource", destroyMethod = "close", initMethod = "init")
    public DruidXADataSource dataSource() throws SQLException{
        DruidXADataSource druidXADataSource = new DruidXADataSource();
        druidXADataSource.setUrl(url);
        druidXADataSource.setUsername(username);
        druidXADataSource.setPassword(password);
        druidXADataSource.setInitialSize(ObjectUtil.getValue(initialSize, 10));
        druidXADataSource.setMinIdle(ObjectUtil.getValue(minIdle, 50));
        druidXADataSource.setMaxActive(ObjectUtil.getValue(maxActive, 50));
        druidXADataSource.setMaxWait(ObjectUtil.getValue(maxWait, 60000));
        druidXADataSource.setTimeBetweenEvictionRunsMillis(ObjectUtil.getValue(timeBetweenEvictionRunsMillis, 60000));
        druidXADataSource.setMinEvictableIdleTimeMillis(ObjectUtil.getValue(minEvictableIdleTimeMillis, 300000));
        druidXADataSource.setValidationQuery(ObjectUtil.getValue(validationQuery, "SELECT 'x'"));
        druidXADataSource.setTestWhileIdle(ObjectUtil.getValue(testWhileIdle, true));
        druidXADataSource.setTestOnBorrow(ObjectUtil.getValue(testOnBorrow, false));
        druidXADataSource.setTestOnReturn(ObjectUtil.getValue(testOnReturn, false));
        druidXADataSource.setPoolPreparedStatements(ObjectUtil.getValue(poolPreparedStatements, true));
        druidXADataSource.setMaxPoolPreparedStatementPerConnectionSize(
                ObjectUtil.getValue(maxPoolPreparedStatementPerConnectionSize, 20));
        druidXADataSource.setConnectionErrorRetryAttempts(ObjectUtil.getValue(connectionErrorRetryAttempts, 3));
        druidXADataSource.setBreakAfterAcquireFailure(ObjectUtil.getValue(breakAfterAcquireFailure, true));
        druidXADataSource.setDefaultAutoCommit(false);
        druidXADataSource.setRemoveAbandoned(ObjectUtil.getValue(removeAbandoned, false));
        druidXADataSource.setLogAbandoned(ObjectUtil.getValue(logAbandoned, true));
        druidXADataSource.setRemoveAbandonedTimeout(ObjectUtil.getValue(removeAbandonedTimeout, 1800));
        druidXADataSource.setFilters(ObjectUtil.getValue(filters, "stat"));
        return druidXADataSource;
    }

    @Bean(value = "atomikosDataSourceBean", destroyMethod = "close", initMethod = "init")
    @DependsOn({"atomikosJta"})
    public AtomikosDataSourceBean atomikosDataSourceBean(@Autowired @Qualifier("druidXADataSource") DruidXADataSource druidXADataSource) throws SQLException {
        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(druidXADataSource);
        atomikosDataSourceBean.setBorrowConnectionTimeout(60);
        atomikosDataSourceBean.setMaxPoolSize(20);
        atomikosDataSourceBean.setUniqueResourceName(UUID.randomUUID().toString());
        return atomikosDataSourceBean;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Autowired @Qualifier("atomikosDataSourceBean") AtomikosDataSourceBean atomikosDataSourceBean) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(atomikosDataSourceBean);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:mapper/*/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);

        if (!isProdEnv()) {
            Resource resource = resourcePatternResolver.getResource("classpath:mybatis-config.xml");
            sqlSessionFactoryBean.setConfigLocation(resource);
        }
        sqlSessionFactoryBean.setTypeHandlersPackage("com.hisalari.dao.*.*");
        return sqlSessionFactoryBean.getObject();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 判断当前环境是否为prod
    private boolean isProdEnv() {
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        if (activeProfiles.length < 1) {
            return false;
        }
        for (String activeProfile : activeProfiles) {
            //logger.info("【环境】：" + activeProfile);
            if ("prod".equals(activeProfile)) {
                return true;
            }
        }
        return false;
    }

    @Value("#{prop['db.url']}")
    private String url;

    @Value("#{prop['db.user']}")
    private String username;

    @Value("#{prop['db.password']}")
    private String password;

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
