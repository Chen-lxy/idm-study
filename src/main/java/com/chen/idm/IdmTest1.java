package com.chen.idm;


import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.common.engine.api.management.TablePage;
import org.flowable.idm.api.*;
import org.flowable.idm.engine.IdmEngine;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IdmTest1 {

    private IdmEngine idmEngine;
    private IdmIdentityService idmIdentityService;
    private IdmEngineConfiguration idmEngineConfiguration;
    private IdmManagementService idmManagementService;

    @Before
    public void init(){
        // 1.获取配置文件输入流
        InputStream inputStream = IdmTest1
                .class
                .getClassLoader()
                .getResourceAsStream("flowable.idm.cfg.xml");
        // 2. 获取IDM引擎
        idmEngine = IdmEngineConfiguration
                .createIdmEngineConfigurationFromInputStream(inputStream)
                .buildIdmEngine();
        // 3. 获取Idm相关的服务类
        idmIdentityService = idmEngine.getIdmIdentityService();
        idmEngineConfiguration = idmEngine.getIdmEngineConfiguration();
        idmManagementService = idmEngine.getIdmManagementService();
        String name = idmEngine.getName();
        System.out.println("引擎名称是：" + name);
    }

    @Test
    public void testInit(){

    }
    @Test
    public void addUser(){
        // 这个用户类是flowable内置的
        UserEntityImpl user = new UserEntityImpl();
        user.setId("chen000001");
        user.setPassword("chen");
        user.setEmail("chen@163.com");
        user.setRevision(0);    // 该值默认是1，代表数据库中已经存在该用户，会先查找。0代表新增用户
        idmIdentityService.saveUser(user);
    }

    @Test
    public void addGroup(){
        //添加组
        GroupEntityImpl group = new GroupEntityImpl();
        group.setId("买楼部");
        group.setName("买楼部");
        group.setRevision(0);
        idmIdentityService.saveGroup(group);
    }
    @Test
    public void createMemShip(){
        //为组添加成员
        String userId = "chen000001";
        String groupId = "买楼部";
        idmIdentityService.createMembership(userId,groupId);
    }

    @Test
    public void queryUser(){
        //查询组
        List<User> list = idmIdentityService.createUserQuery().list();
        for (User u : list){
            System.out.println(u.getId());
            System.out.println(u.getPassword());
        }
    }

    @Test
    public void queryGroup(){
        //查询权限
        List<Group> list = idmIdentityService.createGroupQuery().list();
        for (Group group : list){
            System.out.println(group.getId());
            System.out.println(group.getName());
        }
    }

    @Test
    public void queryPrivilege(){
        // 查询权限
        List<Privilege> list = idmIdentityService.createPrivilegeQuery().list();
        for (Privilege privilege : list){
            System.out.println(privilege.getId());
            System.out.println(privilege.getName());
        }
    }

    @Test
    public void addPrivilegeForUser(){
        //为用户添加权限
        String userId = "";
        String privilegeId = "";
        idmIdentityService.addUserPrivilegeMapping(privilegeId,userId);
    }

    @Test
    public void addPrivilegeForGroup(){
        // 为组添加权限
        String groupId = "";
        String privilegeId = "";
        idmIdentityService.addGroupPrivilegeMapping(privilegeId,groupId);
    }

    @Test
    public void queryTabelCount(){
        // 获取表相关的信息
        Map<String, Long> tableCount = idmManagementService.getTableCount();
        Set<Map.Entry<String, Long>> entries = tableCount.entrySet();
        for (Map.Entry<String,Long> entry : entries){
            System.out.println("key: " + entry.getKey());
            System.out.println("value : "  + entry.getValue());
        }
    }

    @Test
    public void queryTableName(){
        // 查询User类对应的表名称
        String tableName = idmManagementService.getTableName(User.class);
        System.out.println(tableName);
    }

    @Test
    public void queryTableName2(){
        // 查询Group类对应的表名称
        String tableName = idmManagementService.getTableName(Group.class);
        System.out.println(tableName);
    }

    @Test
    public void queryTableMetaData(){
        // 查询表的元数据信息
        TableMetaData metaData = idmManagementService.getTableMetaData("ACT_ID_GROUP");
        System.out.println(metaData.getTableName());
        System.out.println(metaData.getColumnNames());
        System.out.println(metaData.getColumnTypes());
    }

    @Test
    public void getProperties(){
        // 此处有框架bug
        Map<String, String> properties = idmManagementService.getProperties();
        Set<Map.Entry<String, String>> entries = properties.entrySet();
        for (Map.Entry<String,String> entry : entries){
            System.out.println("key :" + entry.getKey());
            System.out.println("value : " + entry.getValue());
        }
    }

    @Test
    public void createTablePageQuery(){
        // 分页查询表的信息
        TablePage tablePage = idmManagementService.createTablePageQuery()
                .tableName("ACT_ID_GROUP")
                .listPage(0, 10);
        System.out.println(tablePage.getRows());
        System.out.println(tablePage.getSize());
        System.out.println(tablePage.getTableName());
    }






}
