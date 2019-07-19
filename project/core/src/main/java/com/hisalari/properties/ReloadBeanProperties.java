package com.hisalari.properties;

import com.hisalari.util.obj.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReloadBeanProperties {

    private Map<String, Map<String, List<String>>> valuebeansFields = new HashMap<String, Map<String, List<String>>>();

    public void addValuebeansFields(String valueName, String beanName, String fieldName) {
        Map<String, List<String>> beanFields = valuebeansFields.get(valueName);
        if (beanFields == null) {
            beanFields = new HashMap<String, List<String>>();
            valuebeansFields.put(valueName, beanFields);
        }
        List<String> fieldNames = beanFields.get(beanName);
        if (fieldNames == null) {
            fieldNames = new LinkedList<String>();
            beanFields.put(beanName, fieldNames);
        }
        fieldNames.add(fieldName);
    }


    public void changeValue(ApplicationContext applicationContext, Map<String, Object> pro) {
        Set<String> keys = pro.keySet();
        Set<String> valueKeys = valuebeansFields.keySet();
        for (String key : keys) {
            for (String valueKey : valueKeys) {
                if (valueKey.equals(key)) {
                    Map<String, List<String>> beanFields = valuebeansFields.get(valueKey);
                    if (beanFields != null) {
                        Set<Map.Entry<String, List<String>>> entries = beanFields.entrySet();
                        for (Map.Entry<String, List<String>> entry : entries) {
                            String beanName = entry.getKey();
                            List<String> fieldNames = entry.getValue();
                            Object obj = applicationContext.getBean(beanName);
                            Object value = pro.get(key);
                            for (String fieldName : fieldNames) {
                                Reflections.setFieldValue(obj, fieldName, value);
                            }
                        }
                    }
                }
            }
        }
    }
}
