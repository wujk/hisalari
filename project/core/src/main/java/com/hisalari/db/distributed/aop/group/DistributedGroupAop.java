package com.hisalari.db.distributed.aop.group;

import com.hisalari.db.dbAop;
import com.hisalari.db.distributed.DistributedEnable;
import com.hisalari.db.distributed.tx.ZookeeperTx;
import com.hisalari.zookeeper.ZookeeperLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Aspect
public class DistributedGroupAop extends dbAop {

    private Logger logger = LoggerFactory.getLogger(DistributedGroupAop.class);

    private final static String clientId = "locked";


    @Pointcut("@annotation(com.hisalari.db.distributed.DistributedEnable) && args(groupId)")
    private void transactionalGroup(String groupId) {
    }

    @Around("transactionalGroup(groupId)")
    public Object createTransactionalGroup(ProceedingJoinPoint pjp, String groupId) {
        Object obj = null;
        ZookeeperLock lock = null;
        ZookeeperTx tx = null;
        try {
            DistributedEnable distributedEnable = ((MethodSignature)pjp.getSignature()).getMethod().getAnnotation(DistributedEnable.class);
            if (groupId == null) {
                groupId = distributedEnable.groupId();
                if ("".equals(groupId)) {
                    groupId = UUID.randomUUID().toString();
                }
            }
            logger.info("groupId:" + groupId);
            Object[] args = pjp.getArgs();
            args[0] = groupId;
            lock = new ZookeeperLock("127.0.0.1:2181",  groupId, clientId);
            tx = new ZookeeperTx("127.0.0.1:2181", lock.getGroup());
            obj = pjp.proceed(args);
            if(lock.lockWatcher()) {
                String nodeData = tx.getNodeData();
                if (ZookeeperTx.TxStatus.NORMAL.value.equals(nodeData)) {
                    tx.setNodeData(ZookeeperTx.TxStatus.SUCCESS.value);
                } else {
                    tx.setNodeData(ZookeeperTx.TxStatus.ERROR.value);
                }
            }
        } catch (Exception e) {
            String nodeData = tx.getNodeData();
            if (ZookeeperTx.TxStatus.ABNORMAL.value.equals(nodeData)) {
                tx.setNodeData(ZookeeperTx.TxStatus.ERROR.value);
            }
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
            tx.close();
            return obj;
        }
    }

}

