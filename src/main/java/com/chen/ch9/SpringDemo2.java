package com.chen.ch9;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.DataObject;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringDemo2 {

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
                .category("grouptask")
                .name("组任务测试监听器")
                .addClasspathResource("组任务2.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);
    }
    @Test
    public void startProcess(){
        String key = "grouptask";
//        HashMap<String,Object> param = new HashMap<String, Object>();
//        param.put("userId","zhangsan");zhangsan
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void queryProcessInstance(){
        //查询执行实例
        /**
         *  select distinct RES.* , P.KEY_ as ProcessDefinitionKey, P.ID_ as ProcessDefinitionId
         *  from ACT_RU_EXECUTION RES
         *  inner join ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_
         *  WHERE RES.ACT_ID_ = ? and RES.IS_ACTIVE_ = ? order by RES.ID_ asc
         * Parameters: sid-6769243E-DB87-410E-97EC-316D2A4DFFA9(String), true(Boolean)
         */
        String activityId = "sid-308A13E2-7626-40DE-A5ED-0F3F2EBA9473";
        Execution execution = runtimeService
                .createExecutionQuery()
                .activityId(activityId)
                .singleResult();
        System.out.println(execution.getId());
    }

    @Test
    public void trigger(){
        // 完成接受任务
        String excutionId = "40002";
       runtimeService.trigger(excutionId);
    }

    @Test
    public void queryTask(){
        // 查询个人任务
        /**
         * select distinct RES.*
         * from ACT_RU_TASK RES
         * WHERE RES.ASSIGNEE_ = ? order by RES.ID_ asc
         * zhangsan(String)
         * 5
         */
        List<Task> list = taskService
                .createTaskQuery()
                .taskAssignee("chen")
                .list();
        for (Task task : list){
            System.out.println(task.getTaskDefinitionId());
            System.out.println(task.getName());
            System.out.println(task.getId());
        }
    }

    @Test
    public void completeTask(){
        // 完成任务
        /**
         * update ACT_RU_EXECUTION SET REV_ = ?, ACT_ID_ = ? where ID_ = ? and REV_ = ?
         * 2(Integer), _4(String), 22503(String), 1(Integer)
         * 1
         *
         * delete from ACT_RU_TASK where ID_ = ? and REV_ = ?
         * 22506(String), 1(Integer)
         */
        String taskId = "22506";
        taskService.complete(taskId);
    }

    @Test
    public void setAssignee(){
        String taskId = "60005";
        String userId = "曹操";
        taskService.setAssignee(taskId,userId);
    }

    @Test
    public void queryGroupTask(){
        // 查询组任务
        /**
         *   select distinct RES.*
         *   from ACT_RU_TASK RES
         *   WHERE RES.ASSIGNEE_ is null and
         *   exists(select LINK.ID_
         *   from ACT_RU_IDENTITYLINK LINK
         *   where LINK.TYPE_ = 'candidate' and LINK.TASK_ID_ = RES.ID_ and ( LINK.USER_ID_ = ? or LINK.GROUP_ID_ IN ( ? ) ) ) order by RES.ID_ asc
         *  Parameters: chen(String), 研发部(String)
         */
        List<Task> list = taskService
                .createTaskQuery()
                .taskCandidateUser("chen")
                .list();
        for (Task task : list){
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getCreateTime());
        }
    }

    @Test
    public void queryTaskUser(){
        // 查询任务处理人
        String taskId = "65005";
        List<IdentityLink> list = taskService
                .getIdentityLinksForTask(taskId);
        for (IdentityLink identityLink : list){
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            System.out.println(identityLink.getProcessDefinitionId());
            System.out.println(identityLink.getGroupId());
            System.out.println(identityLink.getUserId());
            System.out.println(identityLink.getTaskId());
        }
    }
    @Test
    public void receiveTask(){
        // 组任务之认领任务
        String taskId = "65005";
        String userId = "anna";
        taskService.claim(taskId,userId);
    }


}
