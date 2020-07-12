package com.chen.spring;

import ch.qos.logback.core.util.FileUtil;
import org.flowable.common.engine.impl.util.IoUtil;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:flowable-context.xml")
public class SpringTest {

    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private DynamicBpmnService dynamicBpmnService;
    private FormService formService;
    private IdentityService identityService;
    private ManagementService managementService;
    private RuntimeService runtimeService;

    @Before
    public void buildProcessEngine(){
        // 1.获取流程引擎，通过该方法获取的时候，flowable.cfg.xml配置文件一定要放在根目录下
        processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);
        // 2.获取相关的服务类
        repositoryService = processEngine.getRepositoryService();
        System.out.println(repositoryService);
        String name = processEngine.getName();
        System.out.println(name);
        dynamicBpmnService = processEngine.getDynamicBpmnService();
        System.out.println(dynamicBpmnService);
        formService = processEngine.getFormService();
        System.out.println(formService);
        identityService = processEngine.getIdentityService();
        System.out.println(identityService);
        managementService = processEngine.getManagementService();
        System.out.println(managementService);
        runtimeService = processEngine.getRuntimeService();
        System.out.println(runtimeService);
        TaskService taskService = processEngine.getTaskService();
        System.out.println(taskService);
        HistoryService historyService = processEngine.getHistoryService();
        System.out.println(historyService);
    }

    @Test
    public void getDeploymentBuilder(){
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                .addClasspathResource("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        Deployment deploy = deploymentBuilder.deploy();

        System.out.println(deploy.getId());
    }

    @Test
    public void getDeploymentBuilder1(){
        String text = IoUtil
                .readFileAsString("Complex_compensation.manualmodif.importInFlowable_NOSHAPE.bpmn20.xml");
        DeploymentBuilder deploymentBuilder = repositoryService
                .createDeployment()
                .category("测试分类")
                .name("名称")
                // 资源的名称必须是 bpmn20 或者bpmn20.xml结尾
                .addString("test.bpmn20.xml",text);
        Deployment deploy = deploymentBuilder.deploy();

        System.out.println(deploy.getId());
    }

    @Test
    public void getProcessDefinition(){
        List<ProcessDefinition> list = repositoryService
                .createProcessDefinitionQuery()
                .list();
        for (ProcessDefinition processDefinition : list){
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getCategory());
        }
    }

    @Test
    public void delProcessDefinition(){
        String deploymentoId = "";
        repositoryService.deleteDeployment(deploymentoId);
        // 第二个参数代表是否级联删除
        repositoryService.deleteDeployment(deploymentoId,true);
    }

    @Test
    public void getImagine(){
        String deploymentoId = "";
        List<String> deploymentResourceNames = repositoryService
                .getDeploymentResourceNames(deploymentoId);
        String imageName = null;
        for (String name : deploymentResourceNames){
            if (name.endsWith(".png")){
                imageName = name;
            }
        }
        if(imageName != null){
            File file = new File("path/" + imageName );
            InputStream resourceAsStream = repositoryService
                    .getResourceAsStream(deploymentoId, imageName);
        }
    }

    @Test
    public void queryBySql(){
        List<Deployment> list = repositoryService.createNativeDeploymentQuery()
                .sql("select * from xxxx").list();
    }

    public void deploy1(){
        Deployment deploy = repositoryService
                .createDeployment()
                .deploy();
    }
}
