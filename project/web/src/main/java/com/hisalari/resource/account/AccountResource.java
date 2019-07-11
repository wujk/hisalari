package com.hisalari.resource.account;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hisalari.api.account.inter.IAccountService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountResource {

    @Reference
    IAccountService accountService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return accountService.sayHello();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert() {
        return accountService.insert();
    }

}
