package com.hisalari.resource.account;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hisalari.api.account.inter.IAccountService;
import com.hisalari.db.distributed.DistributedEnable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountResource {

    @Reference(retries = -1, timeout = 100000)
    IAccountService accountService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return accountService.sayHello();
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert() {
        return accountService.insert();
    }

    @RequestMapping(value = "/distributed", method = RequestMethod.GET)
    @DistributedEnable
    public String distributed(String groupId) {
        accountService.distributed1(groupId);
        accountService.distributed2(groupId);
        return "success";
    }

}
