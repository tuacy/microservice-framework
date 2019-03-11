package com.tuacy.microservice.framework.quartz.manage.service;

import com.tuacy.microservice.framework.quartz.manage.exception.QuartzException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;

public interface IQuartzActionService {

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      这是每隔多少秒为一次任务
     * @param jobTimes     运行的次数 （<0:表示不限次数）
     */
    void addJob(Class<? extends QuartzJobBean> jobClass,
                String jobName,
                String jobGroupName,
                int jobTime,
                int jobTimes) throws QuartzException;

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
    void addJob(Class<? extends QuartzJobBean> jobClass,
                String jobName,
                String jobGroupName,
                int jobTime,
                int jobTimes,
                boolean start) throws QuartzException;

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
     */
    void addJob(Class<? extends QuartzJobBean> jobClass,
                String jobName,
                String jobGroupName,
                String jobTime) throws QuartzException;

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
     * @param start        是否启用 true: 添加任务之后，job run，false: 添加任务之后，job pause
     */
    void addJob(Class<? extends QuartzJobBean> jobClass,
                String jobName,
                String jobGroupName,
                String jobTime,
                boolean start) throws QuartzException;

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @param jobTime      任务时间表达式
     */
    void updateJob(String jobName, String jobGroupName, String jobTime) throws QuartzException;

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     * @param jobTime      任务时间表达式
     * @param start        修改任务之后直接设置pause，resume状态
     */
    void updateJob(String jobName,
                   String jobGroupName,
                   String jobTime,
                   boolean start) throws QuartzException;

    /**
     * 删除任务一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void deleteJob(String jobName, String jobGroupName) throws QuartzException;

    /**
     * 暂停一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void pauseJob(String jobName, String jobGroupName) throws QuartzException;

    /**
     * 恢复一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void resumeJob(String jobName, String jobGroupName) throws QuartzException;

    /**
     * 立即执行一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */
    void runAJobNow(String jobName, String jobGroupName) throws QuartzException;

    /**
     * 获取所有计划中的任务列表
     *
     * @return map 列表
     */
    List<Map<String, Object>> queryAllJob() throws QuartzException;

    /**
     * 获取所有正在运行的job
     *
     * @return list 列表
     */

    List<Map<String, Object>> queryRunJob() throws QuartzException;
}
