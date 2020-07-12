package com.chen.ch8;

import org.flowable.engine.impl.util.ProcessInstanceHelper;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;

import java.util.Map;

// 自定义流程助手类
public class MyProcessInstanceHelper extends ProcessInstanceHelper {
    @Override
    public ProcessInstance createProcessInstance(ProcessDefinition processDefinition, String businessKey, String processInstanceName, String overrideDefinitionTenantId, Map<String, Object> variables, Map<String, Object> transientVariables, String callbackId, String callbackType, boolean startProcessInstance) {
        System.out.println("MyProcessInstanceHelper>>>>>>>>>>>>>>>>:" + processDefinition);
        return super.createProcessInstance(processDefinition, businessKey, processInstanceName, overrideDefinitionTenantId, variables, transientVariables, callbackId, callbackType, startProcessInstance);
    }
}
