package com.tuacy.microservice.framework.quartz.manage.service.impl;

import com.tuacy.microservice.framework.quartz.manage.exception.QuartzException;
import com.tuacy.microservice.framework.quartz.manage.service.IQuartzActionService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @name: SafetyReportServiceImpl
 * @author: tuacy.
 * @date: 2019/3/6.
 * @version: 1.0
 * @Description: 告警 -- 定时报平安任务
 * <p>
 * <p>
 * <p>
 * Quartz大致可分为三个主要的核心：
 *     1、调度器Scheduler:是一个计划调度器容器，容器里面可以盛放众多的JobDetail和Trigger，当容器启动后，里面的每个JobDetail都会根据Trigger按部就班自动去执行.
 *     2、任务Job：要执行的具体内容。JobDetail:具体的可执行的调度程序，包含了这个任务调度的方案和策略。
 *     3、触发器Trigger：调度参数的配置，什么时候去执行调度。
 * 原理：
 * 可以这么理解它的原理：调度器就相当于一个容器，装载着任务和触发器。任务和触发器又是绑定在一起的，然而一个任务可以对应多个触发器，但一个触发器却只能对应一个任务。
 * 当JobDetail和Trigger在scheduler容器上注册后，形成了装配好的任务作业（JobDetail和Trigger所组成的一对儿），就可以伴随容器启动而调度执行了。
 * <p>
 * <p>
 * corn表达式格式为七个域，如：秒 分 时 日 月 周 年
 * <p>
 * 字段　　　　允许值　　　　　　　　允许的特殊字符
 * <p>
 * 秒　　　　　0-59 　　　　　　　　, - * /
 * 分　　　　　0-59 　　　　　　　　, - * /
 * 小时　　　　0-23 　　　　　　　　, - * /
 * 日期　　　　1-31 　　　　　　　　, - * ? / L W C
 * 月份　　　　1-12 或者 JAN-DEC 　, - * /
 * 星期　　　　1-7 或者 SUN-SAT　  , - * ? / L C #
 * 年（可选）　留空, 1970-2099 　　, - * /
 * <p>
 * 1、所有域均可用“,”，“-”，“*”，“/
 * 【1】,    x,y表示x和y
 * 【2】-    x-y表示x到y
 * 【3】*    表示每TIME
 * 【4】/    x/y表示从x起，每隔y
 * 2、“日”域另有“?”，“L”，“W”，“C”
 * 【1】?    表示不指定“日”域的值。规则是指定“周”域，则“日”域必须为“?”。反之，指定“日”域，则“周”域必须为“?”。如0 0 12 ? * MON 或 0 0 12 1 * ?
 * 【2】L    2种写法。一，表示月末几天，如2L， 表示月末的2天。二，表示月末倒数第几天，如L-3，表示月末倒数第3天。
 * 【3】W    表示临近某日的工作日，如15W，表示最接近15号的工作日，可能是15号（刚好是工作日）、15号前（刚好周六）或15号后（刚好周日）。
 * 【4】C    表示和Calendar计划关联的值，如1C表示，1日或1日后包括Carlendar的第一天。
 * 【5】LW    L和W的组合，只能出现在"日"域中。表示某月最后一个工作日，不一定是周五（详细见结尾PS）。
 * 3、“周”域另有“?”，“L”，“#”，“C”
 * 【1】?    表示不指定“周”域。规则是指定“日”域值，则“周”域值必须为“?”。反之，指定“周”域值，则“日”域值必须为“?”。如0 0 12 1 * ? 或 0 0 12 ？ * MON
 * 【2】L    表示某月的最后一个周几，如1L， 表示某月的最后一个周日（1表示周日），7L，表示某月的最后一个周六（7表示周六）。
 * 【3】#    只能出现在"周"域中，表示第几个周几，x#y，y表示第几个，x表示周的值，如6#2，表示第2个周五（6表示周五）。
 * 【4】C    表示和Calendar计划关联的值，如1C表示，周日或周日后包括Carlendar的第一天
 * <p>
 * <p>
 * 1、cron表达式在线生成工具http://www.pppet.net/
 * <p>
 * 2、cron表达式生成器http://www.pdtools.net/tools/becron.jsp
 * <p>
 * quartz数据库相关建表路径：org.quartz.impl.jdbcjobstore
 */
@Service
public class QuartzActionServiceImpl implements IQuartzActionService {


    public static final String TASK_ID_KEY = "task_id";

    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    public void setSchedulerFactoryBean(SchedulerFactoryBean schedulerFactoryBean) {
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      这是每隔多少秒为一次任务
     * @param jobTimes     运行的次数 （<0:表示不限次数）
     */
    @Override
    public void addJob(Class<? extends QuartzJobBean> jobClass,
                       String jobName,
                       String jobGroupName,
                       int jobTime,
                       int jobTimes) throws QuartzException {
        this.addJob(jobClass, jobName, jobGroupName, jobTime, jobTimes, true);
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      这是每隔多少秒为一次任务
     * @param jobTimes     运行的次数 （<0:表示不限次数）
     * @param start        添加任务之后直接设置pause，resume状态
     */
    @Override
    public void addJob(Class<? extends QuartzJobBean> jobClass,
                       String jobName,
                       String jobGroupName,
                       int jobTime,
                       int jobTimes,
                       boolean start) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder
                    .newJob(jobClass)
                    .withIdentity(jobName, jobGroupName)// 任务名称和组构成任务key
                    .build();
            // job 信息
            JobDataMap map = jobDetail.getJobDataMap();
            map.put(TASK_ID_KEY, jobName);
            // 使用simpleTrigger规则
            Trigger trigger;
            if (jobTimes < 0) {
                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                        .startNow().build();
            } else {
                trigger = TriggerBuilder
                        .newTrigger()
                        .withIdentity(jobName, jobGroupName)
                        .withSchedule(SimpleScheduleBuilder
                                .repeatSecondlyForever(1)
                                .withIntervalInSeconds(jobTime)
                                .withRepeatCount(jobTimes))
                        .startNow()
                        .build();
            }
            scheduler.scheduleJob(jobDetail, trigger);
            if (!start) {
                // 暂停
                JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("添加告警定时任务失败!");
        }
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
     */
    @Override
    public void addJob(Class<? extends QuartzJobBean> jobClass,
                       String jobName,
                       String jobGroupName,
                       String jobTime) throws QuartzException {
        this.addJob(jobClass, jobName, jobGroupName, jobTime, true);
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
     * @param start        是否启用 true: 添加任务之后，job run，false: 添加任务之后，job pause
     */
    @Override
    public void addJob(Class<? extends QuartzJobBean> jobClass,
                       String jobName,
                       String jobGroupName,
                       String jobTime,
                       boolean start) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobDetail jobDetail = JobBuilder
                    .newJob(jobClass)
                    .withIdentity(jobName, jobGroupName)
                    .build();
            // 参数使用
            JobDataMap map = jobDetail.getJobDataMap();
            map.put(TASK_ID_KEY, jobName);
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobName, jobGroupName)// 触发器名,触发器组
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)) // // 触发器时间设定
                    .startNow()
                    .build();
            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(jobDetail, trigger);
            if (!start) {
                // 暂停
                JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
                scheduler.pauseJob(jobKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务运行失败");
        }
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @param jobTime      任务时间表达式
     */
    @Override
    public void updateJob(String jobName, String jobGroupName, String jobTime) throws QuartzException {
        this.updateJob(jobName, jobGroupName, jobTime, true);
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @param jobTime      任务时间表达式
     * @param start        修改任务之后直接设置pause，resume状态
     */
    @Override
    public void updateJob(String jobName,
                          String jobGroupName,
                          String jobTime,
                          boolean start) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            trigger = trigger
                    .getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobTime))
                    .build();
            // 重启触发器
            scheduler.rescheduleJob(triggerKey, trigger);
            if (!start) {
                // 暂停
                JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
                scheduler.pauseJob(jobKey);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务更新失败");
        }
    }

    /**
     * 删除任务一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    @Override
    public void deleteJob(String jobName, String jobGroupName) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务删除失败");
        }
    }

    /**
     * 暂停一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    @Override
    public void pauseJob(String jobName, String jobGroupName) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务暂停失败");
        }
    }

    /**
     * 恢复一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    @Override
    public void resumeJob(String jobName, String jobGroupName) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务恢复失败");
        }
    }

    /**
     * 立即执行一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    @Override
    public void runAJobNow(String jobName, String jobGroupName) throws QuartzException {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("告警定时任务运行失败");
        }
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return map 列表
     */
    @Override
    public List<Map<String, Object>> queryAllJob() throws QuartzException {
        List<Map<String, Object>> jobList = new ArrayList<>();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobName", jobKey.getName());
                    map.put("jobGroupName", jobKey.getGroup());
                    map.put("description", "触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    map.put("jobStatus", triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        map.put("jobTime", cronExpression);
                    }
                    jobList.add(map);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("查询告警定时任务列表失败");
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     *
     * @return list 列表
     */
    @Override
    public List<Map<String, Object>> queryRunJob() throws QuartzException {
        List<Map<String, Object>> jobList;
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            jobList = new ArrayList<>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                Map<String, Object> map = new HashMap<>();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                map.put("jobName", jobKey.getName());
                map.put("jobGroupName", jobKey.getGroup());
                map.put("description", "触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put("jobTime", cronExpression);
                }
                jobList.add(map);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new QuartzException("查询告警定时任务运行任务列表失败");
        }
        return jobList;
    }
}
