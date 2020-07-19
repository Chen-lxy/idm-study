package com.chen.ch11;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史相关的操作
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringDemo5 {

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
                .category("historydata")
                .name("历史数据相关的测试")
                .key("historydata")
                .addClasspathResource("历史数据篇.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);
    }
    @Test
    public void startProcess(){
        String key = "historydata";
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void queryHiostoryProcessInstance(){
        /** 历史流程查询
         *   select distinct RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_,
         *   DEF.VERSION_ as PROC_DEF_VERSION_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_
         *   from ACT_HI_PROCINST RES
         *   left outer join ACT_RE_PROCDEF DEF
         *   on RES.PROC_DEF_ID_ = DEF.ID_
         *   WHERE RES.END_TIME_ is not NULL order by RES.ID_ asc
         */
        List<HistoricProcessInstance> list = historyService
                .createHistoricProcessInstanceQuery()
                .finished() //过滤条件
                .list();
        for (HistoricProcessInstance hpi : list){
            System.out.println(hpi.getId());
            System.out.println(hpi.getStartActivityId());
        }
    }

    @Test
    public void queryHistoryActivityInstance(){
        /** 查看历史活动实例
         * select RES.* from ACT_HI_ACTINST RES order by RES.ID_ asc
         */
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .list();
        for (HistoricActivityInstance hai : list){
            System.out.println(hai.getId());
            System.out.println(hai.getActivityId());
        }
    }

    @Test
    public void queryHistoryTaskInstance(){
        /** 查询历史任务
         * select distinct RES.* from ACT_HI_TASKINST RES order by RES.ID_ asc
         */
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .list();
        for (HistoricTaskInstance hti : list){
            System.out.println(hti.getId());
            System.out.println(hti.getAssignee());
            System.out.println(hti.getStartTime());
        }
    }



}
