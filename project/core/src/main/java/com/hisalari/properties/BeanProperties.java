package com.hisalari.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class BeanProperties implements BeanPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(BeanProperties.class);


    @Autowired
    ReloadBeanProperties reloadBeanProperties;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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
                logger.info(beanName + " : " + fieldName + " : " + value);
                reloadBeanProperties.addValuebeansFields(value, beanName, fieldName);
            }
        }
        return bean;
    }
}
