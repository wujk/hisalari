package com.hisalari.properties;

import com.hisalari.util.json.JsonMapper;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Component
public class PropertiesHelper extends org.springframework.beans.factory.config.PropertiesFactoryBean implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    private ZooKeeper zookeeper;
    private CountDownLatch lantch;

    @Override
    protected Properties createProperties() throws IOException {
        Properties p = super.createProperties();
        if (p == null || p.keySet().size() == 0) {
            return p;
        }
        System.out.println(p.keySet());
        try {
            lantch = new CountDownLatch(1);
            zookeeper = new ZooKeeper(p.getProperty("zookeeper.address"), 10000000, (WatchedEvent event) -> {
                try {
                    if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                        logger.info("PropertiesHelper.zookeeper连接成功");
                        lantch.countDown();
                    } else if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
                        logger.info("PropertiesHelper.zookeeper关闭连接");
                        return;
                    }
                    if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                        String node = getNodeData("/myconfig");
                        System.out.println("node:" + node);
                        try {
                            Map<String, Object> pro = JsonMapper.AlwaysMapper().fromJson(node, Map.class);
                            ReloadBeanProperties reloadBeanProperties = applicationContext.getBean(ReloadBeanProperties.class);
                            reloadBeanProperties.changeValue(applicationContext, pro);
                        } catch (Exception e) {

                        }
                    }
                    zookeeper.exists("/myconfig", true);
                } catch (Exception e) {

                }
            });
            lantch.await();
            System.out.println("zookeeper.address:" + p.getProperty("zookeeper.address"));
            String node = getNodeData("/myconfig");
            System.out.println("node:" + node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public String getNodeData(String node) {
        try {
            Stat stat = zookeeper.exists(node, true);
            if (stat != null) {
                byte[] data = zookeeper.getData(node, true, stat);
                if (data != null) {
                    String nodeData = new String(data, "UTF-8");
                    return nodeData;
                }
            }
        } catch (Exception e) {
            logger.error("PropertiesHelper.getNodeData" + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
