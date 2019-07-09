package com.hisalari.db.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.UserTransaction;

@Configuration
public class AtomikosJtaConfiguration {

    @Bean(name = "atomikosJta")
    public JtaTransactionManager regTransactionManager () throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        UserTransaction userTransaction = new UserTransactionImp();
        userTransaction.setTransactionTimeout(10000);
        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }

}
