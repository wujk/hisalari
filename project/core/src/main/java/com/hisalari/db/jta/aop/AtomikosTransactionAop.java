package com.hisalari.db.jta.aop;

import com.hisalari.db.MybatisMutiXAManager;
import com.hisalari.db.TransactionalThreadLocl;
import com.hisalari.db.dbAop;
import com.hisalari.db.jta.AtomikosEnable;
import com.hisalari.spring.SpringContextUtils;
import org.apache.ibatis.session.SqlSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component
@Aspect
public class AtomikosTransactionAop extends dbAop {
    private Logger logger = LoggerFactory.getLogger(AtomikosTransactionAop.class);

    @Resource(name = "atomikosJta")
    private PlatformTransactionManager platformTransactionManager;

    @Resource(name = "springContextUtils")
    private SpringContextUtils springContextUtils;

    @Autowired(required = false)
    MybatisMutiXAManager mybatisMutiXAManager;

    @Autowired
    private TransactionalThreadLocl transactional;

    @Pointcut("@annotation(com.hisalari.db.jta.AtomikosEnable)")
    private void transactional() {
    }

    @Around("transactional()")
    public Object beforeInsertMethods(ProceedingJoinPoint pjp) throws Exception {
        Map<String, SqlSession> sessions = null;
        Object obj = null;
        try {
            AtomikosEnable atomikosEnable = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(AtomikosEnable.class);
            String transactionManagerName = atomikosEnable.transactionManagerName();
            logger.info("transactionManagerName：" + transactionManagerName);
            if (!"atomikosJta".equals(transactionManagerName)) {
                platformTransactionManager = (PlatformTransactionManager)springContextUtils.getBeanById(transactionManagerName);
                if (platformTransactionManager == null) {
                    throw new RuntimeException(transactionManagerName + "事务管理器不存在");
                }
            }
            obj = invoke(pjp, transactional, platformTransactionManager);
            sessions = mybatisMutiXAManager.getSessionMap().get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (sessions != null) {
                Set<Map.Entry<String, SqlSession>> entries = sessions.entrySet();
                for (Map.Entry<String, SqlSession> entry : entries) {
                    SqlSession session = entry.getValue();
                    boolean sessionIsClosed = session.getConnection().isClosed();
                    logger.info(entry.getKey() + " session is closed: " + sessionIsClosed);
                    session.close();
                }
            }
            return obj;
        }
    }
}
