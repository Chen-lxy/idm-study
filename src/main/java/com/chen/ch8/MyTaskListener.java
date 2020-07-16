package com.chen.ch8;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class MyTaskListener implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        // 设置任务的处理人
        delegateTask.setAssignee("曹操");
    }
}
