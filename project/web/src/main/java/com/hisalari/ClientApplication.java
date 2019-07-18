package com.hisalari;


import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.hisalari.resource", "com.hisalari.properties", "com.hisalari.redis", "com.hisalari.db.distributed.aop.group"}, exclude = {XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableDubbo
public class ClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

}
