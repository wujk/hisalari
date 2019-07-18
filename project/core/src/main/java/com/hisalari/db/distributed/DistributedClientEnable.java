package com.hisalari.db.distributed;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Component
@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedClientEnable {

    String groupId() default "";

    String transactionManagerName() default "atomikosJta";

}
