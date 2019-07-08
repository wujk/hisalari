package com.hisalari.properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class PropertiesConfiguration {

    private PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private List<Resource> common() {
        String[] propertiesFiles = new String[]{};
        List<Resource> resources = new ArrayList<Resource>();
        for (int i = 0; i < propertiesFiles.length; i++) {
            resources.add(resourcePatternResolver.getResource("classpath*:".concat(propertiesFiles[i])));
        }
        return resources;
    }

    @Bean
    @Profile("dev")
    public void dev() throws Exception {
        getProperties("classpath*:*_dev.properties");
    }

    @Bean
    @Profile("sit")
    public void sit() throws Exception {
        getProperties("classpath*:*_sit.properties");
    }

    @Bean
    @Profile("prod")
    public void prod() throws Exception {
        getProperties("classpath*:*_prod.properties");
    }

    private void getProperties(String location) throws Exception {
        List<Resource> list = common();
        Resource[] _resources = resourcePatternResolver.getResources(location);
        list.addAll(Arrays.asList(_resources));
        Resource[] resources = new Resource[list.size()];
        resources = list.toArray(resources);
        propertiesFactoryBean.setLocations(resources);
        propertiesFactoryBean.getObject();
    }

}
