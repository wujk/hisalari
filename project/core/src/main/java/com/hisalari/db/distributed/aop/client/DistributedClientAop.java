package com.hisalari.db.distributed.aop.client;

import com.hisalari.db.MybatisMutiManager;
import com.hisalari.db.MybatisMutiXAManager;
import com.hisalari.db.TransactionalThreadLocl;
import com.hisalari.db.dbAop;
import com.hisalari.db.distributed.DistributedClientEnable;
import com.hisalari.db.distributed.Transactional;
import com.hisalari.db.distributed.tx.ZookeeperTx;
import com.hisalari.spring.SpringContextUtils;
import com.hisalari.zookeeper.ZookeeperLock;
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

@Component
@Aspect
public class DistributedClientAop extends dbAop {

    private Logger logger = LoggerFactory.getLogger(DistributedClientAop.class);

    private final static String clientId = "locked";

    @Autowired
    private MybatisMutiManager mybatisMutiManager;

    @Pointcut("@annotation(com.hisalari.db.distributed.DistributedClientEnable) && args(groupId)")
    private void transactionalClient(String groupId) {
    }

    @Around("transactionalClient(groupId)")
    public Object createTransactionalClient(ProceedingJoinPoint pjp, String groupId) {
        Object obj = null;
        ZookeeperLock lock = null;
        ZookeeperTx tx = null;

        try {
            DistributedClientEnable distributedClientEnable = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(DistributedClientEnable.class);
            if (groupId == null) {
                groupId = distributedClientEnable.groupId();
            }
            logger.info("groupId:" + groupId);
            lock = new ZookeeperLock("127.0.0.1:2181",  groupId, clientId);
            if (lock.lockWatcher()) {
                tx = new ZookeeperTx("127.0.0.1:2181", lock.getGroup());
                obj = pjp.proceed();
                tx.setSessions(mybatisMutiManager.getSessionMap().get());
                tx.setNodeData(ZookeeperTx.TxStatus.NORMAL.value);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            tx.setNodeData(ZookeeperTx.TxStatus.ABNORMAL.value);
        } finally {
            lock.unlock();
            return obj;
        }
    }
}

