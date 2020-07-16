package com.chen.ch9;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class GoupTaskListener implements TaskListener {

    public void notify(DelegateTask delegateTask) {
        // 为组任务添加候选人
        delegateTask.addCandidateUser("chen");
        delegateTask.addCandidateUser("小明");
    }
}
