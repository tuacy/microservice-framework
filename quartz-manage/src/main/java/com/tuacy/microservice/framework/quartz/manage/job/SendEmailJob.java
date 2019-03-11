package com.tuacy.microservice.framework.quartz.manage.job;

import com.tuacy.microservice.framework.quartz.manage.service.impl.QuartzActionServiceImpl;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时发送邮件任务 -- 只是一个模拟任务，大家根据实际情况编写
 */
public class SendEmailJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        if (jobDataMap != null && !jobDataMap.isEmpty()) {
            System.out.println("******************************");
            System.out.println("******************************");
            System.out.println("******************************");
            System.out.println("job name = " + jobDataMap.get(QuartzActionServiceImpl.TASK_ID_KEY));
            System.out.println("开始发送邮件了");
        }

    }
}
