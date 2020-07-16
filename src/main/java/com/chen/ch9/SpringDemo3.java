package com.chen.ch9;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringDemo3 {

    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private DynamicBpmnService dynamicBpmnService;
    private FormService formService;
    private IdentityService identityService;
    private ManagementService managementService;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private HistoryService historyService;

    @Before
    public void buildProcessEngine(){
        // 1.获取流程引擎，通过该方法获取的时候，flowable.cfg.xml配置文件一定要放在根目录下
        processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取相关的服务类
        repositoryService = processEngine.getRepositoryService();
        dynamicBpmnService = processEngine.getDynamicBpmnService();
        formService = processEngine.getFormService();
        identityService = processEngine.getIdentityService();
        managementService = processEngine.getManagementService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
    }
    @Test
    public void deploy(){
        // 流程部署
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("servicetask")
                .name("服务任务")
                .addClasspathResource("服务任务.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);
    }
    @Test
    public void startProcess(){
        String key = "servicetask";
//        HashMap<String,Object> param = new HashMap<String, Object>();
//        param.put("userId","zhangsan");zhangsan
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }



}
