package com.chen.ch8;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.DataObject;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ExecutionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringDemo {

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
                .category("leave")
                .name("请假流程")
                .addClasspathResource("leave.bpmn");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);
    }
    @Test
    public void startProcess(){
        String key = "leave";
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void getTask(){
        //查询个人任务
        String processKey = "leave";
        List<Task> list = taskService
                .createTaskQuery()
                .taskAssignee("wangwu")
                .processDefinitionKey(processKey)
                .list();
        for (Task task : list){
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getTaskDefinitionId());
        }
    }
    @Test
    public  void completeTask(){
        String taskId = "17502";
        taskService.complete(taskId);
        System.out.println("任务完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Test
    public void queryInstance(){
        // 查看流程状态
        String processInstanceId = "";
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (processInstance == null){
            System.out.println("当前流程运行结束");
        }else{
            System.out.println("当前实例正在运行");
        }
    }
    @Test
    public void queryProcssInstancec(){
        // 查询执行实例
        List<Execution> list = runtimeService
                .createExecutionQuery()
                .list();
        for (Execution execution : list){
            System.out.println(execution.getId());
        }
    }

    @Test
    public void queryExcution(){
        String processInstanceId = "0b3ef4a3-bc8c-11ea-a33f-005056c00001";
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        System.out.println("流程实例ID为："+historicProcessInstance.getId());
        System.out.println("流程定义ID为："+historicProcessInstance.getProcessDefinitionId());
        System.out.println(historicProcessInstance.getStartTime());
        System.out.println(historicProcessInstance.getStartActivityId());
        System.out.println(historicProcessInstance.getEndTime());
    }

    @Test
    public void queryHistorricActivityInstance(){
        // select RES.* from ACT_HI_ACTINST RES order by RES.ID_ asc
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .list();
        for (HistoricActivityInstance historicActivityInstance : list){
            System.out.println(historicActivityInstance.getId());
        }
    }

    @Test
    public void getHistoricTaskInstance(){
        // select distinct RES.* from ACT_HI_TASKINST RES order by RES.ID_ asc
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .list();
        for (HistoricTaskInstance historicTaskInstance : list){
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getId());
        }
    }

    @Test
    public void setAuthenticatedUser1(){
        // 设置流程实例的启动人的信息
        identityService.setAuthenticatedUserId("李云龙");
        String key = "leave";
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void setAuthenticatedUser2(){
        // 设置流程实例的启动人的信息
        Authentication.setAuthenticatedUserId("楚云飞");
        String key = "leave";
        // 启动流程实例
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(key);
        System.out.println(processInstance.getId());
        // 当前走到哪个节点了
        System.out.println(processInstance.getActivityId());
    }

    @Test
    public void deploy1(){
        Deployment deploy = repositoryService
                .createDeployment()
                .name("请假流程2")
                .category("leave2")
                //.addClasspathResource("leave.bpmn")
                .addClasspathResource("dataObject.bpmn20.xml")
                .deploy();
    }

    @Test
    public void startProcessDataObject(){
        String processDefinitionKey = "dataObject";
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processDefinitionKey);
        System.out.println(processInstance.getId() + ">>>>>" + processInstance.getActivityId());
    }

    @Test
    public void getDataObject(){
        /**
         * select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and NAME_= ? and TASK_ID_ is null
         * 32501(String), day(String)
         *  0
         */
        String executionId = "32501";
        String dataObject = "天数";
        DataObject dataObject1 = runtimeService
                .getDataObject(executionId, dataObject);
        if (dataObject1 != null){
            System.out.println(dataObject1.getDataObjectDefinitionKey());
            System.out.println(dataObject1.getDescription());
            System.out.println(dataObject1.getExecutionId());
            System.out.println(dataObject1.getName());
            System.out.println(dataObject1.getValue());
        }
    }

    @Test
    public void getDataObject2(){
        /**
         *  select * from ACT_RU_VARIABLE where EXECUTION_ID_ = ? and TASK_ID_ is null
         *  32501(String)
         *  3
         */
        String executionId = "32501";
        Map<String, DataObject> dataObjects = runtimeService
                .getDataObjects(executionId);

        Set<Map.Entry<String, DataObject>> entries = dataObjects.entrySet();
        for (Map.Entry<String,DataObject> entry : entries){
            DataObject dataObject = entry.getValue();
            if (dataObject != null){
                System.out.println(dataObject.getDataObjectDefinitionKey());
                System.out.println(dataObject.getDescription());
                System.out.println(dataObject.getExecutionId());
                System.out.println(dataObject.getName());
                System.out.println(dataObject.getValue());
            }
        }
    }

    @Test
    public void deletProcessInstance(){
        // 删除流程
        String processInstanceId="32501";
        String deleteReason="测试删除";
        runtimeService.deleteProcessInstance(processInstanceId,deleteReason);
    }

    @Test
    public void getActivity(){
        String executionId = "25003";
        List<String> activeActivityIds = runtimeService
                .getActiveActivityIds(executionId);
        for (String id : activeActivityIds){
            System.out.println(id);
        }
    }

    @Test
    public void startProcessById(){
        String processDefinitionId = "";
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceById(processDefinitionId);
    }
    @Test
    public void startProcessById2(){
        ProcessInstance leave = runtimeService
                .startProcessInstanceById("leave", "001");
    }

    @Test
    public void isProcessDefinitionSuspended(){
        String processDefinitionId = "";
        // 判断是否挂起
        boolean processDefinitionSuspended = repositoryService
                .isProcessDefinitionSuspended(processDefinitionId);
    }

    @Test
    public void suspendProcessDefinitionById(){
        String processDefinitionId = "";
        // 流程挂起，挂起后就不能启动流程了
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Test
    public void activateProcessDefinitionById(){
        String processDefinitionId = "";
        // 激活流程，可以指定日期（涉及定时器）
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Test
    public void suspendProcessInstanceById(){
        String processDefinitionId = "";
        // 流程实例挂起
        runtimeService.suspendProcessInstanceById(processDefinitionId);
    }



}
