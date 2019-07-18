package com.hisalari.db;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.PlatformTransactionManager;

public class dbAop {

    protected Object invoke(ProceedingJoinPoint pjp, TransactionalThreadLocl transactional, PlatformTransactionManager platformTransactionManager) {
        Object obj = null;
        try {
            transactional.createTransactional(platformTransactionManager);
            obj = pjp.proceed();
            transactional.commit();
        } catch (Throwable e) {
            transactional.rollback();
            throw new Exception(e);
        } finally {
            return obj;
        }
    }

}
