package com.tuacy.microservice.framework.quartz.manage.controller;

import com.tuacy.microservice.framework.common.controller.BaseController;
import com.tuacy.microservice.framework.common.entity.response.ResponseDataEntity;
import com.tuacy.microservice.framework.quartz.manage.api.IQuartzManageApi;
import com.tuacy.microservice.framework.quartz.manage.exception.QuartzException;
import com.tuacy.microservice.framework.quartz.manage.job.SendEmailJob;
import com.tuacy.microservice.framework.quartz.manage.param.QuartzJobAddParam;
import com.tuacy.microservice.framework.quartz.manage.param.QuartzJobDeleteParam;
import com.tuacy.microservice.framework.quartz.manage.service.IQuartzActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IQuartzManageController extends BaseController implements IQuartzManageApi {

    private static final String GROUP = "email_group";

    private IQuartzActionService quartzActionService;

    @Autowired
    public void setQuartzActionService(IQuartzActionService quartzActionService) {
        this.quartzActionService = quartzActionService;
    }

    /**
     * 添加一个job任务
     */
    @Override
    public ResponseDataEntity<Void> addQuartzJob(@RequestBody QuartzJobAddParam param) {
        ResponseDataEntity<Void> responseDataEntity = new ResponseDataEntity<>();
        if (param.getKeyId() != null && !param.getKeyId().isEmpty()) {
            // 模拟每隔5s执行一次
            try {
                quartzActionService.addJob(SendEmailJob.class, param.getKeyId(), GROUP, "0/5 * * * * ? ");
            } catch (QuartzException e) {
                e.printStackTrace();
            }
        }
        return responseDataEntity;
    }

    /**
     * 添加一个job任务
     */
    @Override
    public ResponseDataEntity<Void> deleteQuartzJob(@RequestBody QuartzJobDeleteParam param) {
        ResponseDataEntity<Void> responseDataEntity = new ResponseDataEntity<>();
        if (param.getKeyId() != null && !param.getKeyId().isEmpty()) {
            // 模拟每隔5s执行一次
            try {
                quartzActionService.deleteJob(param.getKeyId(), GROUP);
            } catch (QuartzException e) {
                e.printStackTrace();
            }
        }
        return responseDataEntity;
    }

}
