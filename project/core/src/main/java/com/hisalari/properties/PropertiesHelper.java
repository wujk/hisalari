package com.hisalari.properties;

import com.hisalari.util.json.JsonMapper;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class PropertiesHelper extends org.springframework.beans.factory.config.PropertiesFactoryBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private AbstractEnvironment environment;

    private ZooKeeper zookeeper;
    private CountDownLatch lantch;

    private Properties properties;

    @Override
    protected Properties createProperties() throws IOException {
        properties = super.createProperties();
        if (properties == null || properties.keySet().size() == 0) {
            return properties;
        }
        System.out.println(properties.keySet());
        try {
            lantch = new CountDownLatch(1);
            zookeeper = new ZooKeeper(properties.getProperty("zookeeper.address"), 10000000, (WatchedEvent event) -> {
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
                            properties.putAll(pro);
                            refreshValues();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    zookeeper.exists("/myconfig", true);
                } catch (Exception e) {

                }
            });
            lantch.await();
            System.out.println("zookeeper.address:" + properties.getProperty("zookeeper.address"));
            String node = getNodeData("/myconfig");
            Map<String, Object> pro = JsonMapper.AlwaysMapper().fromJson(node, Map.class);
            properties.putAll(pro);
            System.out.println("node:" + node);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    private void refreshContxt() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = (AnnotationConfigApplicationContext)applicationContext;
        annotationConfigApplicationContext.stop();
    }

    public void refreshValues() throws Exception {
       DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        String[] names = defaultListableBeanFactory.getBeanDefinitionNames();
        for (String beanName : names) {
            System.out.println(beanName+"---------");
            Object bean = applicationContext.getBean(beanName);
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(Value.class);
                if (annotation != null) {
                    String value = ((Value) annotation).value();
                    if (value.startsWith("$")) {
                        SplExpress express = new SplExpress();
                        value = express.parseExpress(value);
                    } else {
                        SplExpress express = new SplExpress("#{prop['", "']}");
                        value = express.parseExpress(value);
                    }
                    String fieldName = field.getName();
                    field.setAccessible(true);
                    field.set(bean, properties.get(value));
                    field.setAccessible(false);
                }
            }
        }
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