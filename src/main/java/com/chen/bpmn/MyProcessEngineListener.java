package com.chen.bpmn;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineLifecycleListener;

public class MyProcessEngineListener implements ProcessEngineLifecycleListener {
    // 监听引擎启动
    public void onProcessEngineBuilt(ProcessEngine processEngine) {
        System.out.println("MyProcessEngineListener open " + processEngine);
    }

    // 监听引擎关闭
    public void onProcessEngineClosed(ProcessEngine processEngine) {
        System.out.println("MyProcessEngineListener  closed " + processEngine);
    }
}
