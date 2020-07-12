package com.chen.bpmn;

import org.flowable.engine.*;
import org.junit.After;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;

public class ProcessEngineTest {

    private ProcessEngine processEngine;

    @Test
    public void testProcessEngine(){
        // 1.获取流程引擎，通过该方法获取的时候，flowable.cfg.xml配置文件一定要放在根目录下
        processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        // 2.获取相关的服务类
        RepositoryService repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
        String name = processEngine.getName();
        System.out.println(name);
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        System.out.println(dynamicBpmnService);
        FormService formService = processEngine.getFormService();
        System.out.println(formService);
        IdentityService identityService = processEngine.getIdentityService();
        System.out.println(identityService);
        ManagementService managementService = processEngine.getManagementService();
        System.out.println(managementService);
        RuntimeService runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        TaskService taskService = processEngine.getTaskService();
        System.out.println(taskService);
        HistoryService historyService = processEngine.getHistoryService();
        System.out.println(historyService);

        // 获取spring容器
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Map<Object, Object> beans = processEngineConfiguration.getBeans();
        Object testA = beans.get("testA");
        System.out.println(testA);

        ProcessEngine aDefault = ProcessEngines.getProcessEngine("default");
        System.out.println(aDefault);
    }

//    @After
//    public void close(){
//        // 关闭流程引擎，使用flowable风格的配置文件需要手动关闭，如果是spring风格的会自动关闭
//        processEngine.close();
//    }

    @Test
    public void test(){
        // 编码形式构造processEngine
        ProcessEngineConfiguration standaloneProcessEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        standaloneProcessEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        standaloneProcessEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable");
        standaloneProcessEngineConfiguration.setJdbcUsername("root");
        standaloneProcessEngineConfiguration.setJdbcPassword("root");
        processEngine = standaloneProcessEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void getProcessEngine(){
        InputStream inputStream = ProcessEngineTest
                .class.getClassLoader().getResourceAsStream("flowable.cfg.xml");
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromInputStream(inputStream).buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void getProcessEngine1(){
        String beanName = "processEngineConfiguration1";
        InputStream inputStream = ProcessEngineTest
                .class.getClassLoader().getResourceAsStream("flowable.cfg.xml1");
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromInputStream(inputStream,beanName)
                .buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void getProcessEngine2(){
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void getProcessEngine3(){
        String resource = "flowable.cfg.xml";
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(resource)
                .buildProcessEngine();
        System.out.println(processEngine);
    }
}
