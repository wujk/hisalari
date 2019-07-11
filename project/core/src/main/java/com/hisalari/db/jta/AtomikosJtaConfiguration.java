package com.hisalari.db.jta;

import com.atomikos.icatch.jta.JtaTransactionServicePlugin;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.hisalari.properties.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

@Configuration
@AutoConfigureAfter(PropertiesConfiguration.class)
public class AtomikosJtaConfiguration {

    @Bean(name = "atomikosJta")
    @DependsOn({ "userTransaction", "atomikosTransactionManager" })
    @Autowired
    public PlatformTransactionManager transactionManager (TransactionManager atomikosTransactionManager, UserTransaction userTransaction) throws Throwable {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }

    @Bean(name = "atomikosTransactionManager")
    public TransactionManager atomikosTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }


    @Bean(name = "userTransaction")
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

}
