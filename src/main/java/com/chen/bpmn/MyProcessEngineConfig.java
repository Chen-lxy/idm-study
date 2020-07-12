package com.chen.bpmn;

import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

public class MyProcessEngineConfig extends StandaloneProcessEngineConfiguration {

    @Override
    public void init() {
        System.out.println("MyProcessEngineConfig  inite方法执行。。。。");
        super.init();
    }
}
