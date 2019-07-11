package com.hisalari.service.account.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hisalari.api.account.inter.IAccountService;
import com.hisalari.dao.company.DeptMapper;
import com.hisalari.db.MybatisMutiXAManager;
import com.hisalari.db.jta.AtomikosEnable;
import com.hisalari.model.comapny.Dept;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    @Autowired
    MybatisMutiXAManager mybatisMutiXAManager;

    @Override
    public String sayHello() {
        return "hello";
    }

    @Override
    @AtomikosEnable
    public String insert() {
        DeptMapper deptMapper1 = mybatisMutiXAManager.getMapper(DeptMapper.class, "3be0fdd6b45f4d08858bc78ae853bd1d");
        Dept dept1 = new Dept();
        dept1.setUid(UUID.randomUUID().toString());
        dept1.setCompanyUid("1111");
        dept1.setName("1111");
        deptMapper1.insert(dept1);
        DeptMapper deptMapper2 = mybatisMutiXAManager.getMapper(DeptMapper.class, "c87c627e5f39406a8c188c5a5281b5a1");
        Dept dept2 = new Dept();
        dept2.setUid(UUID.randomUUID().toString());
        dept2.setCompanyUid("2222");
        dept2.setName("22222");
        deptMapper2.insert(dept2);
        int i = 0;
        return "success";
    }

}
