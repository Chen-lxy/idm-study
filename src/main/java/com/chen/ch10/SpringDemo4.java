package com.chen.ch10;

import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
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
 * 变量相关的操作
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringDemo4 {

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
                .category("variable")
                .name("变量相关的测试")
                .key("variable")
                .addClasspathResource("变量测试流程.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();
        System.out.println(deploy);
    }
    @Test
    public void startProcess(){
        String key = "variable";
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
    public void  setVariable(){
        // 查询任务 112505
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list){
            System.out.println(task.getId() + ">>>>>>>>>>>>>>>>>>");
            // 设置变量
            if(task.getId().equals("112505")){
                taskService.setVariable(task.getId(),"请假人","张三");
                // 这种方式设置的变量的task_id有值
                taskService.setVariableLocal(task.getId(),"请假天数",10);
                Map<String,Object> variable = new HashMap<String, Object>();
                variable.put("请假日期",new Date());
                variable.put("variable","taskService.setVariable");
                taskService.setVariables(task.getId(),variable);
            }
        }
    }

    @Test
    public void complete(){
        // 完成任务
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list){
            if (task.getId().equals("112505")){
                Map<String,Object> variable = new HashMap<String, Object>();
                variable.put("完成人","张三");
                taskService.complete(task.getId(),variable);
            }
        }
    }

    @Test
    public void queryVariable(){
        // 获取变量
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list){
            System.out.println(task.getId() + " >>>>>>>>>>>>>>>>>>>>>>>>>");
            Map<String, Object> variables = taskService.getVariables(task.getId());
            System.out.println(variables);
        }
    }



}
