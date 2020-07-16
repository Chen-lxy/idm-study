package com.chen.ch9;


import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

// 服务任务监听类
public class ServerTask implements JavaDelegate {
    Expression age;
    public void execute(DelegateExecution delegateExecution) {
        System.out.println(age + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(age.getExpressionText());
        delegateExecution.setVariable("chen","good");
    }
}
