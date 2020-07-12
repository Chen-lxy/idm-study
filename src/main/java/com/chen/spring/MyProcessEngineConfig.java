package com.chen.spring;

import org.flowable.spring.SpringProcessEngineConfiguration;

public class MyProcessEngineConfig extends SpringProcessEngineConfiguration {
    @Override
    public void init() {
        System.out.println("myProcessEngineConfig  init执行。。。。。");
        super.init();
    }
}
