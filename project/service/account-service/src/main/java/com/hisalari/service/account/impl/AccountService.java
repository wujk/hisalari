package com.hisalari.service.account.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hisalari.api.account.inter.IAccountService;
import com.hisalari.dao.company.DeptMapper;
import com.hisalari.db.MybatisMutiManager;
import com.hisalari.db.MybatisMutiXAManager;
import com.hisalari.db.distributed.DistributedClientEnable;
import com.hisalari.db.distributed.DistributedEnable;
import com.hisalari.db.jta.AtomikosEnable;
import com.hisalari.model.comapny.Dept;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {

    @Autowired(required = false)
    MybatisMutiXAManager mybatisMutiXAManager;

    @Autowired
    MybatisMutiManager mybatisMutiManager;

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

    @Override
    @DistributedClientEnable
    public void distributed1(String groupId) {
        DeptMapper deptMapper1 = mybatisMutiManager.getMapper(DeptMapper.class, "3be0fdd6b45f4d08858bc78ae853bd1d");
        Dept dept1 = new Dept();
        dept1.setUid(UUID.randomUUID().toString());
        dept1.setCompanyUid("333");
        dept1.setName("333");
        deptMapper1.insert(dept1);
        DeptMapper deptMapper2 = mybatisMutiManager.getMapper(DeptMapper.class, "c87c627e5f39406a8c188c5a5281b5a1");
        Dept dept2 = new Dept();
        dept2.setUid(UUID.randomUUID().toString());
        dept2.setCompanyUid("444");
        dept2.setName("444");
        deptMapper2.insert(dept2);
        int i = 0;
    }

    @Override
    @DistributedClientEnable
    public void distributed2(String groupId) {
        DeptMapper deptMapper1 = mybatisMutiManager.getMapper(DeptMapper.class, "3be0fdd6b45f4d08858bc78ae853bd1d");
        Dept dept1 = new Dept();
        dept1.setUid(UUID.randomUUID().toString());
        dept1.setCompanyUid("555");
        dept1.setName("555");
        int i = 1/0;
        deptMapper1.insert(dept1);
        DeptMapper deptMapper2 = mybatisMutiManager.getMapper(DeptMapper.class, "c87c627e5f39406a8c188c5a5281b5a1");
        Dept dept2 = new Dept();
        dept2.setUid(UUID.randomUUID().toString());
        dept2.setCompanyUid("666");
        dept2.setName("666");
        deptMapper2.insert(dept2);
    }

}
